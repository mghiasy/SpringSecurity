package com.example.demo.jwt;

import com.google.common.net.HttpHeaders;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
//all of the configuration of this class starts with :application.jwt
@ConfigurationProperties(prefix = "application.jwt")
//This class holds configuration for Jwt
//we want to configure jwt => get the key from property file
public class JwtConfig {
    //every setting related to token will be injected to this class
    private String secretKey;
    private String tokenPrefix;
    private Integer tokenExpirationAfterDays;

    public JwtConfig() {
    }

////It returns a bean to be managed by Spring context.
//// Is usually declared in Configuration classes methods.
//// In this case, bean methods may reference other @Bean methods in the same class by calling them directly.
    //@Bean
    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getTokenPrefix() {
        return tokenPrefix;
    }

    public void setTokenPrefix(String tokenPrefix) {
        this.tokenPrefix = tokenPrefix;
    }

    public Integer getTokenExpirationAfterDays() {
        return tokenExpirationAfterDays;
    }

    public void setTokenExpirationAfterDays(Integer tokenExpirationAfterDays) {
        this.tokenExpirationAfterDays = tokenExpirationAfterDays;
    }
    public String getAuthorizationHeader(){
        //from google.common
        return HttpHeaders.AUTHORIZATION;
    }
}
