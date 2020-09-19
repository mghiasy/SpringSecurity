package com.example.demo.jwt;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

@Configuration
//convert secret key to byte and return
public class JwtSecretKey {
    //inject jwt config to this class
    private final JwtConfig jwtConfig;

    @Autowired
    public JwtSecretKey(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    @Bean
    // this bean refer to another bean getSecretKey=> getSecretKey should have @Bean
    public SecretKey secretKey() {
        //String key ="somethingSecureAndVeryLongThe specified key byte array is 208 bits which is not secure enough,So I made it longer";
        return Keys.hmacShaKeyFor(jwtConfig.getSecretKey().getBytes());
    }
}

