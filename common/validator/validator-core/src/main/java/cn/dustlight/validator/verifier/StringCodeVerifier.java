package cn.dustlight.validator.verifier;

import cn.dustlight.validator.ValidatorException;
import cn.dustlight.validator.core.Code;

import java.util.Map;

public class StringCodeVerifier implements CodeVerifier {

    private StringVerifierHandler handler;

    public StringCodeVerifier(StringVerifierHandler handler) {
        this.handler = handler;
    }

    public StringCodeVerifier() {
        this.handler = new StringEqualsVerifierHandler(false, true);
    }

    @Override
    public void verify(Code code, Object key, Map<String, Object> parameters) throws VerifyCodeException {
        try {
            Object target = parameters.get(key);
            String c, t;
            c = code == null || code.getCode() == null ? null : code.getCode().toString();
            t = target == null ? null : target.toString();
            if (!handler.verify(c, t)) {
                throw new VerifyFailException("Fail to verify code.");
            }
        } catch (Exception e) {
            if (e instanceof ValidatorException)
                throw e;
            throw new VerifyCodeException("Fail to verify code.", e);
        }
    }

    public StringVerifierHandler getHandler() {
        return handler;
    }

    public void setHandler(StringVerifierHandler handler) {
        this.handler = handler;
    }

    public interface StringVerifierHandler {
        boolean verify(String code, String target);
    }

    public static class StringEqualsVerifierHandler implements StringVerifierHandler {

        private boolean enabledNull;
        private boolean trim;

        public StringEqualsVerifierHandler(boolean enabledNull, boolean trim) {
            this.enabledNull = enabledNull;
            this.trim = trim;
        }

        @Override
        public boolean verify(String code, String target) {
            if (code == null || target == null)
                return enabledNull ? code == target : false;
            return trim ? code.trim().equals(target.trim()) : code.equals(target);
        }

        public void setEnabledNull(boolean enabledNull) {
            this.enabledNull = enabledNull;
        }

        public boolean isEnabledNull() {
            return enabledNull;
        }
    }
}
