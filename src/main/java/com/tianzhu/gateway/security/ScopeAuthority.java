package com.tianzhu.gateway.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.stereotype.Component;

@Component
public class ScopeAuthority {

    @Autowired
    private ServerOAuth2AuthorizedClientRepository repository;


    public <T> ReactiveAuthorizationManager<T>  hasScope(String scope){
        return new ScopeAuthorityReactiveAuthorizationManager<T>(repository,scope);
    }


}
