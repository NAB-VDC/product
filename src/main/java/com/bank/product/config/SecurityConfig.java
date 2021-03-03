package com.bank.product.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableResourceServer
public class SecurityConfig extends ResourceServerConfigurerAdapter{

    @Autowired
    JwtAccessTokenConverter accessTokenConverter;
    
    @Autowired
    Environment environment;
    
    @Override
    public void configure(HttpSecurity http) throws Exception {

        http.cors().disable().csrf().disable()
                .authorizeRequests().antMatchers("/public/sims/vouchers/**").permitAll().and()
                .authorizeRequests().anyRequest().authenticated();
    }
    
    @Override
    public void configure(ResourceServerSecurityConfigurer config) {
        config.tokenServices(tokenServices());
    }

    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        return defaultTokenServices;
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter) {
            @Override
            public OAuth2Authentication readAuthentication(OAuth2AccessToken token) {
                OAuth2Authentication oAuth2Authentication = super.readAuthentication(token);
                oAuth2Authentication.setDetails(token.getAdditionalInformation());
                return oAuth2Authentication;
            }
        };
    }

    @Bean
    public SecurityFeignRequestInterceptor feignRequestInterceptor(
            OAuth2ClientContext oAuth2ClientContext, OAuth2ProtectedResourceDetails resource) {
        final ClientCredentialsResourceDetails clientCredentialsResource = new ClientCredentialsResourceDetails();
        clientCredentialsResource.setClientId(environment.getProperty("security.oauth2.client.client-id"));
        clientCredentialsResource.setClientSecret(environment.getProperty("security.oauth2.client.client-secret"));
        clientCredentialsResource.setAccessTokenUri(environment.getProperty("security.oauth2.resource.tokenInfoUri"));

        return new SecurityFeignRequestInterceptor(oAuth2ClientContext, resource, clientCredentialsResource);
    }
}
