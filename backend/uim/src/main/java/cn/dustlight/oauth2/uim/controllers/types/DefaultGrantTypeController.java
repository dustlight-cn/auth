package cn.dustlight.oauth2.uim.controllers.types;

import cn.dustlight.generator.UniqueGenerator;
import cn.dustlight.oauth2.uim.entities.v1.types.DefaultGrantType;
import cn.dustlight.oauth2.uim.entities.v1.types.GrantType;
import cn.dustlight.oauth2.uim.services.clients.ClientService;
import cn.dustlight.oauth2.uim.services.types.GrantTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;


@RestController
public class DefaultGrantTypeController implements GrantTypeController<DefaultGrantType> {

    @Autowired
    private GrantTypeService grantTypeService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private UniqueGenerator<Long> idGenerator;

    @Override
    public Collection<? extends GrantType> getGrantTypes(Collection<Long> tid) {
        if (tid == null || tid.size() == 0)
            return grantTypeService.listGrantTypes();
        return grantTypeService.getGrantTypes(tid);
    }


    @Override
    public void setGrantTypes(Collection<DefaultGrantType> types) {
        if (types != null)
            for (DefaultGrantType type : types)
                if (type.getTid() == null)
                    type.setTid(idGenerator.generate());
        grantTypeService.createGrantTypes(types);
    }

    @Override
    public Collection<? extends GrantType> getClientGrantTypes(String cid) {
        return clientService.listGrantTypes(cid);
    }

    @Override
    public Collection<? extends GrantType> getUserClientGrantTypes(Long uid, String cid) {
        return clientService.listGrantTypes(cid, uid);
    }

    @Override
    public void deleteUserClientGrantTypes(Long uid, String cid, Collection<Long> tid) {
        clientService.removeGrantTypes(cid, uid, tid);
    }

    @Override
    public void addUserClientGrantTypes(Long uid, String cid, Collection<Long> tid) {
        clientService.addGrantTypes(cid, uid, tid);
    }

    @Override
    public void deleteClientGrantTypes(String cid, Collection<Long> tid) {
        clientService.removeGrantTypes(cid, tid);
    }

    @Override
    public void addClientGrantTypes(String cid, Collection<Long> tid) {
        clientService.addGrantTypes(cid, tid);
    }

    @Override
    public void deleteGrantTypes(Collection<Long> tid) {
        grantTypeService.removeGrantTypes(tid);
    }
}
