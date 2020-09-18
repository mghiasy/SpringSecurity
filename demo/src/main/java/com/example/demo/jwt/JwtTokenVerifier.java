package com.example.demo.jwt;

import com.google.common.base.Strings;
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
        //1)Get token from request: request.getHeader("tokenName") = Bearer +realToken
        String token= request.getHeader("authorization"); //authorization=the tokenName

        //from com.google.common.base.Strings library
        if(Strings.isNullOrEmpty(token) || !token.startsWith("Bearer ")){
            //some thing is wrong => reject the request
            filterChain.doFilter(request,response); //dofilter = reject
        }

        String exactToken = token.replace("Bearer ",""); //remove "Bearer " from beginning of token
    }

}
