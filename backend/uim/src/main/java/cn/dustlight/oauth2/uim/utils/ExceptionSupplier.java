package cn.dustlight.oauth2.uim.utils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.function.Supplier;

public class ExceptionSupplier implements Supplier<String> {

    private String str;

    public ExceptionSupplier(Exception e) throws UnsupportedEncodingException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(out);
        e.printStackTrace(printStream);
        str = new String(out.toByteArray(),"UTF-8");
    }

    @Override
    public String get() {
        return str;
    }
}
