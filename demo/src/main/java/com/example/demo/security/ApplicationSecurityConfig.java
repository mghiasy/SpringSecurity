package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import static com.example.demo.security.ApplicationUserRole.*;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) //to enable preAuthorize
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
                .csrf().disable()//later
                .authorizeRequests()
                //here Order has matter => they executed line by line
                .antMatchers("/","index").permitAll() //this antMatchers for all the users
                .antMatchers("/api/**").hasRole(STUDENT.name()) //everything after api just accessible by student
//                .antMatchers(HttpMethod.DELETE,"/management/api/**").hasAuthority(COURDE_WRITE.getPermission()) //return Course:wite
//                .antMatchers(HttpMethod.POST,"/management/api/**").hasAuthority(COURDE_WRITE.getPermission())
//                .antMatchers(HttpMethod.PUT,"/management/api/**").hasAuthority(COURDE_WRITE.getPermission())
//                .antMatchers(HttpMethod.GET,"/management/api/**").hasAnyRole(ADMIN.name(),ADMINTRAINEE.name())
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }

    @Override
    @Bean //? why
    protected UserDetailsService userDetailsService() {
        UserDetails newUser = User.builder()// has Authorities as Collection<? extends GrantedAuthority> getAuthorities();
                .username("Maryam")
                .password(passwordEncoder.encode("123")) //to encode the password to bcrypt
                //.roles("STUDENT") //will be matched to ROLE_STUDENT
                //.roles(STUDENT.name())
                    // inside Roles method we have :
                    // List<GrantedAuthority> authorities = new ArrayList(roles.length);
                    //authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
                    // if we want to add authorities to user directly we should do the same


                .authorities(STUDENT.getGrantedAuthority())
                .build();

        UserDetails adminUser = User.builder()
                .username("Admin")
                .password(passwordEncoder.encode("123")) //to encode the password to bcrypt
                //.roles(ADMIN.name()) //will be matched to ROLE_STUDENT
                .authorities(ADMIN.getGrantedAuthority())
                .build();

        UserDetails adminTraineeUser = User.builder()
                .username("AdminT")
                .password(passwordEncoder.encode("123")) //to encode the password to bcrypt
//                .roles(ADMINTRAINEE.name()) //will be matched to ROLE_STUDENT
                .authorities(ADMINTRAINEE.getGrantedAuthority())
                .build();

        return new InMemoryUserDetailsManager(newUser,adminUser,adminTraineeUser);
    }
}
