package com.tianzhu.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Bean
    public RouterFunction<ServerResponse> indexRouter(
            @Value("classpath:/static/index.html") final Resource indexHtml) {
        return route(GET("/index"), request -> ok().contentType(MediaType.TEXT_HTML).syncBody(indexHtml));
    }

    /*@Bean
    UserInfoRestTemplateCustomizer userInfoRestTemplateCustomizer(LoadBalancerInterceptor loadBalancerInterceptor) {
        return template -> {
            List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>(template.getInterceptors());
            interceptors.add(loadBalancerInterceptor);
            AccessTokenProviderChain accessTokenProviderChain = Stream
                    .of(new AuthorizationCodeAccessTokenProvider(), new ImplicitAccessTokenProvider(),
                            new ResourceOwnerPasswordAccessTokenProvider(), new ClientCredentialsAccessTokenProvider())
                    .peek(tp -> tp.setInterceptors(interceptors))
                    .collect(Collectors.collectingAndThen(Collectors.toList(), AccessTokenProviderChain::new));
            template.setAccessTokenProvider(accessTokenProviderChain);
        };
    }*/
/*
    @Bean
    public CookieHttpSessionStrategy cookieHttpSessionStrategy() {
        CookieHttpSessionStrategy strategy = new CookieHttpSessionStrategy();
        //strategy.setCookieSerializer(newCustomerCookieSerializer());
        strategy.setCookieName("JSESSIONID");
        return strategy;
    }*/

}
