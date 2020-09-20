package cn.dustlight.validator.annotation;

public @interface DeleteCode {
    boolean onSuccess() default true;

    int onFail() default 3;
}
