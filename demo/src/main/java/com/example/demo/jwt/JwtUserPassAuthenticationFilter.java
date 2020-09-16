package com.example.demo.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//Job of this class is to verify the credentials
//Spring does it by default via UsernamePasswordAuthenticationFilter class but we can customize it
public class JwtUserPassAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    public JwtUserPassAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    //This method takes both request and response
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            //1) Grab the username & password sent by client
            //put InputStream into UserPassAuthenticationRequest
            UserPassAuthenticationRequest authenticationRequest = new ObjectMapper()
                    .readValue(request.getInputStream(),UserPassAuthenticationRequest.class);
                    //for this authenticationRequest we wanna check whether user/pass are correct

            //2)Validate the credentials
//Authentication is interface, we need UsernamePasswordAuthenticationToken
            //get authReq and validate is based on principles
            Authentication authentication= new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(), // username to be existed
                    authenticationRequest.getPassword() //password to be valid
            );

            return authenticationManager.authenticate(authentication);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
