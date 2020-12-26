package cn.dustlight.auth.services.resources;

import cn.dustlight.auth.ErrorEnum;
import cn.dustlight.auth.entities.GrantType;
import cn.dustlight.auth.generator.UniqueGenerator;
import cn.dustlight.auth.mappers.GrantTypeMapper;
import cn.dustlight.auth.services.GrantTypeService;

import java.util.Collection;

public class DefaultGrantTypeService implements GrantTypeService {

    private final GrantTypeMapper grantTypeMapper;
    private final UniqueGenerator<Long> idGenerator;

    public DefaultGrantTypeService(GrantTypeMapper grantTypeMapper,
                                   UniqueGenerator<Long> idGenerator) {
        this.grantTypeMapper = grantTypeMapper;
        this.idGenerator = idGenerator;
    }

    @Override
    public Collection<? extends GrantType> listGrantTypes() {
        return grantTypeMapper.listGrantTypes();
    }

    @Override
    public Collection<? extends GrantType> getGrantTypes(Collection<Long> tids) {
        return grantTypeMapper.selectGrantTypes(tids);
    }

    @Override
    public GrantType getGrantType(Long tid) {
        return grantTypeMapper.selectGrantType(tid);
    }

    @Override
    public void createGrantType(String name, String description) {
        if (!grantTypeMapper.insertType(idGenerator.generate(), name, description))
            ErrorEnum.CREATE_GRANT_TYPE_FAIL.throwException();
    }

    @Override
    public void createGrantTypes(Collection<? extends GrantType> grantTypes) {
        if (!grantTypeMapper.insertTypes(grantTypes))
            ErrorEnum.CREATE_GRANT_TYPE_FAIL.throwException();
    }

    @Override
    public void removeGrantType(Long tid) {
        if (!grantTypeMapper.deleteType(tid))
            ErrorEnum.DELETE_GRANT_TYPE_FAIL.throwException();
    }

    @Override
    public void removeGrantTypes(Collection<Long> tids) {
        if (!grantTypeMapper.deleteTypes(tids))
            ErrorEnum.DELETE_GRANT_TYPE_FAIL.throwException();
    }
}
