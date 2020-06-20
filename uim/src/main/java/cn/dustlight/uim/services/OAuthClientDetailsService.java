package cn.dustlight.uim.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OAuthClientDetailsService implements ClientDetailsService, ClientRegistrationService {

    @Autowired
    private ClientMapper mapper;

    @Override
    public ClientDetails loadClientByClientId(String s) throws ClientRegistrationException {
        ClientDetails clientDetails = mapper.loadClientByClientId(s);
        if (clientDetails == null)
            throw new NoSuchClientException("No client with requested id: " + s);
        return clientDetails;
    }

    @Override
    public void addClientDetails(ClientDetails clientDetails) throws ClientAlreadyExistsException {

    }

    @Override
    public void updateClientDetails(ClientDetails clientDetails) throws NoSuchClientException {

    }

    @Override
    public void updateClientSecret(String s, String s1) throws NoSuchClientException {

    }

    @Override
    public void removeClientDetails(String s) throws NoSuchClientException {

    }

    @Override
    public List<ClientDetails> listClientDetails() {
        return null;
    }
}
