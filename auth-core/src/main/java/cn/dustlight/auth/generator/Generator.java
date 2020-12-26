package cn.dustlight.auth.generator;

/**
 * 生成器
 *
 * @param <T>
 */
public interface Generator<T> {

    /**
     * 生成
     *
     * @return
     */
    T generate();
}
