package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import static com.example.demo.security.ApplicationUserRole.*;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {
    //wire this class with passwordEncoder to use it for password
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/","index").permitAll() //this antMatchers for all the users
                .antMatchers("/api/**").hasRole(STUDENT.name()) //everything after api just accessible by student
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }

    @Override
    @Bean //? why
    protected UserDetailsService userDetailsService() {
        UserDetails newUser = User.builder()
                .username("Maryam")
                .password(passwordEncoder.encode("123")) //to encode the password to bcrypt
                //.roles("STUDENT") //will be matched to ROLE_STUDENT
                .roles(STUDENT.name())
                .build();

        UserDetails adminUser = User.builder()
                .username("Admin")
                .password(passwordEncoder.encode("123")) //to encode the password to bcrypt
                .roles(ADMIN.name()) //will be matched to ROLE_STUDENT
                .build();

        UserDetails adminTraineeUser = User.builder()
                .username("AdminT")
                .password(passwordEncoder.encode("123")) //to encode the password to bcrypt
                .roles(ADMINTRAINEE.name()) //will be matched to ROLE_STUDENT
                .build();

        return new InMemoryUserDetailsManager(newUser,adminUser,adminTraineeUser);
    }
}
