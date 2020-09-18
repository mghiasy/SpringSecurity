package com.example.demo.jwt;

import com.google.common.base.Strings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

//TO VERIFY THE TOKEN SENT BY CLIENT
public class JwtTokenVerifier extends OncePerRequestFilter {//this filter should be executed exactly once per req
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //1)Get token from request: request.getHeader("tokenName") = Bearer +realToken
        String token= request.getHeader("authorization"); //authorization=the tokenName

        //from com.google.common.base.Strings library
        if(Strings.isNullOrEmpty(token) || !token.startsWith("Bearer ")){
            //some thing is wrong => reject the request
            filterChain.doFilter(request,response); //dofilter = reject
        }

        String exactToken = token.replace("Bearer ",""); //remove "Bearer " from beginning of token
        //CHECK HEADER VERIFICATION
        try {
            //with same key
            String key ="somethingSecureAndVeryLongThe specified key byte array is 208 bits which is not secure enough,So I made it longer";
            //VERIFY JWT
            //Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws).getBody().getSubject().equals("Joe");
            Jws<Claims> Jws = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(key.getBytes())) //same as creating token
                    .build()
                    .parseClaimsJws(exactToken);//A signed JWT is called a 'JWS'
            //get the body of Jws of type "Claim"
            Claims body = Jws.getBody();
            String username=body.getSubject(); //subject in token = username according to time we create it
            //here we wanna get Authorities which is in body
            //Authorities is LIST of MAP<String,String> => key ="authority" , value = "Course:write"
            List<Map<String,String>> authorities = (List<Map<String,String>>) body.get("authorities");//get by value and convert it to List<Map<String,String>>
            //now we want to get every "authority" of list and convert them to type "SimpleGrantAuthority"
            //to convert every item in list we use Stream and Map (x->new TYPE(x))
            Set<SimpleGrantedAuthority> authority = authorities
                    .stream()
                    .map(m -> new SimpleGrantedAuthority(m.get("authority"))) //get from map by key
                    .collect(Collectors.toSet());//change list to set


            //to validate the token => Create an object of type UsernamePasswordAuthenticationToken with values of token
            //if you could create it with out error => token in valid
            Authentication auth = new UsernamePasswordAuthenticationToken(
                    username, //principle
                    null,//credential
                    authority//authorities
            );
            //This line authenticates the token
            SecurityContextHolder.getContext().setAuthentication(auth);

        }
        //IF GOES TO CATCH BLOCK MEANS TOKEN IS NOT VALID
        //OR IS NOT CORRECT OR IS EXPIRED OR ...
        catch (JwtException e){
            throw new IllegalStateException("Token can not be trust");
        }
    }

}
