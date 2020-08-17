package cn.dustlight.storage.core;

public class Permission {

    public static final int READABLE = 1;
    public static final int WRITABLE = 2;
    public static final int PRIVATE = 0;
    public static final int PUBLIC = READABLE | WRITABLE;

    public static boolean isReadable(int value) {
        return (value & READABLE) > 0;
    }

    public static boolean isWritable(int value) {
        return (value & WRITABLE) > 0;
    }

    public static int compute(boolean readable, boolean writable) {
        return (readable ? 1 : 0) | (writable ? 1 : 0);
    }
}
