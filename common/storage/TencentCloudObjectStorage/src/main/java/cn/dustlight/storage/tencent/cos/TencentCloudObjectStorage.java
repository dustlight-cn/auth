package cn.dustlight.storage.tencent.cos;

import cn.dustlight.storage.core.*;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.model.Bucket;
import com.qcloud.cos.model.CannedAccessControlList;
import com.qcloud.cos.model.GeneratePresignedUrlRequest;
import com.qcloud.cos.model.PutObjectRequest;
import org.apache.http.impl.io.EmptyInputStream;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TencentCloudObjectStorage implements RestfulStorage {

    COSClient cosClient;
    String bucket;
    ExecutorService pool;

    public TencentCloudObjectStorage(COSClient cosClient, String bucket) {
        this(cosClient, bucket, Runtime.getRuntime().availableProcessors() + 1);
    }

    public TencentCloudObjectStorage(COSClient cosClient, String bucket, int threadCount) {
        pool = Executors.newFixedThreadPool(threadCount);
        this.cosClient = cosClient;
        this.bucket = bucket;
        if (bucket == null) {
            List<Bucket> buckets = cosClient.listBuckets();
            if (buckets != null && buckets.size() > 0)
                this.bucket = buckets.get(0).getName();
        }
    }

    @Override
    public String generateGetUrl(String key, Long expiration) {
        return cosClient.generatePresignedUrl(bucket, key, new Date(System.currentTimeMillis() + expiration), HttpMethodName.GET).toExternalForm();
    }

    @Override
    public String generatePutUrl(String key, int permission, Long expiration) {
        GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(bucket, key, HttpMethodName.PUT);
        req.withExpiration(new Date(System.currentTimeMillis() + expiration));
        return cosClient.generatePresignedUrl(req).toExternalForm();
    }

    @Override
    public String generateRemoveUrl(String key, Long expiration) {
        return cosClient.generatePresignedUrl(bucket, key, new Date(System.currentTimeMillis() + expiration), HttpMethodName.DELETE).toExternalForm();
    }

    @Override
    public TencentCloudStorableObject create(String key, int permission) {
        PutObjectRequest req = new PutObjectRequest(bucket, key, EmptyInputStream.INSTANCE, null);
        req.withCannedAcl(getACL(permission));
        cosClient.putObject(req);
        return new TencentCloudStorableObject(this, key);
    }

    @Override
    public TencentCloudStorableObject get(String key) {
        return new TencentCloudStorableObject(this, key);
    }

    @Override
    public TencentCloudStorableObject put(String key, StorableObject source) throws IOException {
        return put(key, source, source.getPermission());
    }

    @Override
    public TencentCloudStorableObject put(String key, StorableObject source, int permission) throws IOException {
        PutObjectRequest req = new PutObjectRequest(bucket, key, source.getInputStream(), null);
        req.withCannedAcl(getACL(permission));
        cosClient.putObject(req);
        return new TencentCloudStorableObject(this, key);
    }

    @Override
    public void remove(String key) {
        cosClient.deleteObject(bucket, key);
    }

    @Override
    public void setPermission(String key, int permission) {
        cosClient.setObjectAcl(bucket, key, getACL(permission));
    }

    @Override
    public boolean isExist(String key) {
        return cosClient.doesObjectExist(bucket, key);
    }

    protected static CannedAccessControlList getACL(int permission) {
        switch (permission) {
            case Permission.PUBLIC:
            case Permission.WRITABLE:
                return CannedAccessControlList.PublicReadWrite;
            case Permission.PRIVATE:
                return CannedAccessControlList.Private;
            case Permission.READABLE:
                return CannedAccessControlList.PublicRead;
            default:
                return CannedAccessControlList.Default;
        }
    }
}
