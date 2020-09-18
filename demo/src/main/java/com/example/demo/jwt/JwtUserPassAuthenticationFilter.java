package com.example.demo.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

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

            return authenticationManager.authenticate(authentication); // if = true => is Authenticated or isValid

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String key ="somethingSecureAndVeryLongThe specified key byte array is 208 bits which is not secure enough,So I made it longer";
        //Generate the token of type String
        //String jws = Jwts.builder().setSubject("Joe").signWith(key).compact();
        String token=Jwts.builder()
                .setSubject(authResult.getName()) //header => subject in token = username
                .claim("Authorities",authResult.getAuthorities()) //body => convert it to type claim
                .setIssuedAt(new Date()) //now
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusWeeks(2))) //convert LocalDate.now() to java.sql.Date
                .signWith(Keys.hmacShaKeyFor(key.getBytes())) //signature
                .compact();// compacting it into its final String form. A signed JWT is called a 'JWS'.
        //add token to responseHeader and send it to client
        //authorization = token name , Bearer = token type
        response.addHeader("authorization","Bearer "+token);
    }
}
