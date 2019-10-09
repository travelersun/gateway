package com.tianzhu.gateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.security.oauth2.gateway.TokenRelayGatewayFilterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.session.data.redis.config.annotation.web.server.EnableRedisWebSession;

import java.util.List;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@EnableRedisWebSession(maxInactiveIntervalInSeconds = 1800,redisNamespace = "spring:session:gateway")
public class SecurityConfiguration {


    public SecurityConfiguration() {

    }

    @Autowired
    ScopeAuthority scopeAuthority;


    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {

        http
                .authorizeExchange().pathMatchers("/uaa/**","/xxl-job-admin/api/**").permitAll()
                .pathMatchers("/idcenter/**").access(scopeAuthority.hasScope("scim.userids"))
                .pathMatchers("/xxl-job-admin/**").access(scopeAuthority.hasScope("scim.userids"))
                //.pathMatchers("/idcenter/**").hasAuthority("SCOPE_scim.userids")
                .anyExchange().authenticated();
        http.oauth2ResourceServer().jwt();
        http.oauth2Login();
        http.oauth2Client();
        http.csrf().disable();
        //http.logout().logoutUrl("/logout").logoutHandler(null).logoutSuccessHandler(null);
        return http.build();


    }


}
