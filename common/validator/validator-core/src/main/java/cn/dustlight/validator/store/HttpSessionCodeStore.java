package cn.dustlight.validator.store;

import cn.dustlight.validator.ValidatorException;
import cn.dustlight.validator.annotation.Duration;
import cn.dustlight.validator.core.Code;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.util.Map;

public class HttpSessionCodeStore<T> implements CodeStore<T> {

    @Override
    public void store(Code<T> code, Duration duration, Map<String, Object> parameters) throws StoreCodeException {
        if (code == null || code.getName() == null)
            throw new StoreCodeException("Code is null");
        try {
            HttpSession session = getSession(true);
            if (duration != null && duration.enabled())
                session.setMaxInactiveInterval((int) (duration.value() / 1000));
            session.setAttribute(code.getName(), code);
        } catch (Exception e) {
            throw new StoreCodeException("Store code fail", e);
        }
    }

    @Override
    public Code<T> load(String key, Map<String, Object> parameters) throws LoadCodeException {
        try {
            HttpSession session = getSession(false);
            Object val = null;
            if (session == null || (val = session.getAttribute(key)) == null)
                throw new CodeNotExistsException("Code not exists");
            return (Code) val;
        } catch (Exception e) {
            if (e instanceof ValidatorException)
                throw e;
            throw new LoadCodeException("Load code fail", e);
        }
    }

    @Override
    public void remove(String key) throws RemoveCodeException {
        try {
            HttpSession session = getSession(false);
            if (session == null)
                throw new CodeNotExistsException("Code not exists");
            session.removeAttribute(key.toString());
        } catch (Exception e) {
            if (e instanceof ValidatorException)
                throw e;
            throw new RemoveCodeException("Remove code fail", e);
        }
    }

    private HttpSession getSession(boolean createIfNull) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes.getRequest().getSession(createIfNull);
    }
}
