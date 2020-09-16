package com.example.demo.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//this is a custom class for fetch data from db
@Service
public class ApplicationUserService implements UserDetailsService {//UserDetailsService throws UsernameNotFoundException
    //here we should specify which class of ApplicationUserDAO we are injecting
    private final ApplicationUserDAO applicationUserDAO;
    @Autowired
    //@Qualifier is used when there is more than 1 implementation, and we can switch in them
    // and means that the bean by the name of "fake" will be injected
    public ApplicationUserService(@Qualifier("fake") ApplicationUserDAO applicationUserDAO) {
        this.applicationUserDAO = applicationUserDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return applicationUserDAO
                .selectApplicationUserByUsername(username)
                .orElseThrow(()->
                        new UsernameNotFoundException(String.format("Username %s not found",username)) );
    }
}
