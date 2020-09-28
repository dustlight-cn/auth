package cn.dustlight.oauth2.uim.services.clients;

import cn.dustlight.oauth2.uim.mappers.ClientMapper;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.NoSuchClientException;

public class DefaultUimClientDetailsService implements UimClientDetailsService {

    private ClientMapper mapper;

    public DefaultUimClientDetailsService(ClientMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public ClientDetails loadClientByClientId(String s) throws ClientRegistrationException {
        ClientDetails details = mapper.loadClient(s);
        if (details == null)
            throw new NoSuchClientException("no such client");
        return details;
    }
}
