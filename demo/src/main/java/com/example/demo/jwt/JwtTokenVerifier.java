package com.example.demo.jwt;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//TO VERIFY THE TOKEN SENT BY CLIENT
public class JwtTokenVerifier extends OncePerRequestFilter {//this filter should be executed exactly once per req
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //1)Get token from request:
        String authorizarionHeader= request.getHeader("Authorization"); //the tokenName
    }

}
