package cn.dustlight.storage.tencent.cos;

import cn.dustlight.storage.core.StorableObject;
import cn.dustlight.storage.core.Permission;
import com.qcloud.cos.model.*;

import java.io.*;

public class TencentCloudStorableObject implements StorableObject {

    private COSObject cosObject;
    private TencentCloudObjectStorage storage;
    private String key;

    TencentCloudStorableObject(TencentCloudObjectStorage storage, String key) {
        this.storage = storage;
        this.key = key;
        this.cosObject = storage.cosClient.getObject(storage.bucket, key);
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return cosObject.getObjectContent();
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        PipedOutputStream outputStream = new PipedOutputStream();
        PipedInputStream inputStream = new PipedInputStream();
        outputStream.connect(inputStream);
        storage.pool.execute(new PipedStreamTask(inputStream));
        return outputStream;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public int getPermission() {
        AccessControlList acl = storage.cosClient.getObjectAcl(storage.bucket, key);
        CannedAccessControlList cannedAccessControlList = acl.getCannedAccessControl();
        if (cannedAccessControlList == CannedAccessControlList.Default)
            cannedAccessControlList = storage.cosClient.getBucketAcl(storage.bucket).getCannedAccessControl();
        return getPermission(cannedAccessControlList);
    }

    protected static int getPermission(CannedAccessControlList cannedAccessControlList) {
        switch (cannedAccessControlList) {
            case PublicRead:
                return Permission.READABLE;
            case PublicReadWrite:
                return Permission.PUBLIC;
            case Default:
            case Private:
            default:
                return Permission.PRIVATE;
        }
    }

    protected class PipedStreamTask implements Runnable {

        private InputStream inputStream;

        PipedStreamTask(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        public void run() {
            storage.cosClient.putObject(storage.bucket, key, inputStream, null);
        }
    }
}
