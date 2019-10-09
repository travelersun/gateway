package com.tianzhu.gateway.security;

import org.springframework.security.authorization.AuthorityReactiveAuthorizationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class ScopeAuthorityReactiveAuthorizationManager<T> implements ReactiveAuthorizationManager<T> {


    private final String authority;

    private final ServerOAuth2AuthorizedClientRepository repository;

    public ScopeAuthorityReactiveAuthorizationManager(ServerOAuth2AuthorizedClientRepository repository,String authority) {
        this.repository = repository;
        this.authority = authority;
    }

    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, T object) {

        if(!(object instanceof AuthorizationContext)){
            return Mono.just(new AuthorizationDecision(false));
        }

        return authentication
                .flatMap((a) -> {
                    if(a instanceof OAuth2AuthenticationToken){
                        OAuth2AuthenticationToken auth = (OAuth2AuthenticationToken)a;
                        ServerWebExchange exchange = ((AuthorizationContext)object).getExchange();
                        return repository.loadAuthorizedClient(auth.getAuthorizedClientRegistrationId(),auth,exchange)
                                .map(OAuth2AuthorizedClient::getAccessToken)
                                .flatMapIterable(OAuth2AccessToken::getScopes)
                                .hasElement(this.authority);
                    }
                    else if(a instanceof JwtAuthenticationToken){
                        JwtAuthenticationToken jwt = (JwtAuthenticationToken) a;
                        Boolean result = false;
                        for(GrantedAuthority ga: jwt.getAuthorities()){
                            if(("SCOPE_"+this.authority).equals(ga.getAuthority())){
                               result = true;
                               break;
                            }
                        }
                        return Mono.just(result);

                    }else {
                        return Mono.just(false);
                    }
                })
                .map((hasAuthority) -> new AuthorizationDecision(hasAuthority))
                ;


    }
}
