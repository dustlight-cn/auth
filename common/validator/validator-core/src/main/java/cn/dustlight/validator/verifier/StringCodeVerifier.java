package cn.dustlight.validator.verifier;

import cn.dustlight.validator.ValidatorException;
import cn.dustlight.validator.core.Code;

import java.util.Map;

public class StringCodeVerifier implements CodeVerifier<String> {

    private StringVerifierHandler handler;

    public StringCodeVerifier(StringVerifierHandler handler) {
        this.handler = handler;
    }

    public StringCodeVerifier() {
        this.handler = new StringEqualsVerifierHandler(false, true, false);
    }

    @Override
    public void verify(Code<String> code, String target, Map<String, Object> parameters) throws VerifyCodeException {
        try {
            String c, t;
            c = code == null || code.getValue() == null ? null : code.getValue();
            t = target;
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
        private boolean caseSensitive;

        public StringEqualsVerifierHandler(boolean enabledNull, boolean trim, boolean caseSensitive) {
            this.enabledNull = enabledNull;
            this.trim = trim;
            this.caseSensitive = caseSensitive;
        }

        @Override
        public boolean verify(String code, String target) {
            if (code == null || target == null)
                return enabledNull ? code == target : false;
            return trim ? code.trim().equals(target.trim()) : code.equals(target);
        }

        public void setCaseSensitive(boolean caseSensitive) {
            this.caseSensitive = caseSensitive;
        }

        public boolean isCaseSensitive() {
            return caseSensitive;
        }

        public void setTrim(boolean trim) {
            this.trim = trim;
        }

        public boolean isTrim() {
            return trim;
        }

        public void setEnabledNull(boolean enabledNull) {
            this.enabledNull = enabledNull;
        }

        public boolean isEnabledNull() {
            return enabledNull;
        }
    }
}
