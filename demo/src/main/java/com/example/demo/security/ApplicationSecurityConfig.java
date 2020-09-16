package com.example.demo.security;

import com.example.demo.auth.ApplicationUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.concurrent.TimeUnit;

import static com.example.demo.security.ApplicationUserRole.*;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) //to enable preAuthorize
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {
    //wire this class with passwordEncoder to use it for password
    private final PasswordEncoder passwordEncoder;
    private final ApplicationUserService applicationUserService; //inject our custom USER_DETAIL_SERVICE
    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder, ApplicationUserService applicationUserService) {
        this.passwordEncoder = passwordEncoder;
        this.applicationUserService = applicationUserService;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()//later
                .authorizeRequests()
                //here Order has matter => they executed line by line
                    .antMatchers("/","index").permitAll() //this antMatchers for all the users
                    .antMatchers("/api/**").hasRole(STUDENT.name()) //everything after api just accessible by student
//                  .antMatchers(HttpMethod.DELETE,"/management/api/**").hasAuthority(COURDE_WRITE.getPermission()) //return Course:wite
//                  .antMatchers(HttpMethod.POST,"/management/api/**").hasAuthority(COURDE_WRITE.getPermission())
//                  .antMatchers(HttpMethod.PUT,"/management/api/**").hasAuthority(COURDE_WRITE.getPermission())
//                  .antMatchers(HttpMethod.GET,"/management/api/**").hasAnyRole(ADMIN.name(),ADMINTRAINEE.name())
                    .anyRequest()
                    .authenticated()
                .and()
                //.httpBasic(); //is used for Basic-Auth
                .formLogin()//is used for Form-Authentication
                    .loginPage("/login")
                    .permitAll()// use custom login page and permit all the users to it.
                    .defaultSuccessUrl("/courses",true)//redirect to this Url after login
                    .passwordParameter("MyPassword")//customized names for user/pass
                    .usernameParameter("MyUserName") //should be exactlu same aas "name" in login.html
                .and()
                .rememberMe() //remember credentials as default for 2 weeks
                    .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21)) //change the defult value of rememberMe to 21 Days
                    .key("somethingverysecure") //Key is used to hash the content of Cookie (userName &  ExpirationDate)
                    .rememberMeParameter("MyRemember-Me")
                .and()
                .logout()
                    .logoutUrl("/logout") //customized logout endpoint
                    //because we have disabled csrf => otherwise we should delete this line => because should be POST
                    //means we wanna go to /logout with GET method
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout","GET")) //to set logout as a GET method
                    .clearAuthentication(true)
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID","remember-me")
                    .logoutSuccessUrl("/login");
    }

//FOR IN MEMORY USERS
//    @Override
//    @Bean //? why
//    protected UserDetailsService userDetailsService() {
//        UserDetails newUser = User.builder()// has Authorities as Collection<? extends GrantedAuthority> getAuthorities();
//                .username("Maryam")
//                .password(passwordEncoder.encode("123")) //to encode the password to bcrypt
//                //.roles("STUDENT") //will be matched to ROLE_STUDENT
//                //.roles(STUDENT.name())
//                    // inside Roles method we have :
//                    // List<GrantedAuthority> authorities = new ArrayList(roles.length);
//                    //authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
//                    // if we want to add authorities to user directly we should do the same
//
//
//                .authorities(STUDENT.getGrantedAuthority())
//                .build();
//
//        UserDetails adminUser = User.builder()
//                .username("Admin")
//                .password(passwordEncoder.encode("123")) //to encode the password to bcrypt
//                //.roles(ADMIN.name()) //will be matched to ROLE_STUDENT
//                .authorities(ADMIN.getGrantedAuthority())
//                .build();
//
//        UserDetails adminTraineeUser = User.builder()
//                .username("AdminT")
//                .password(passwordEncoder.encode("123")) //to encode the password to bcrypt
//               // .roles(ADMINTRAINEE.name()) //will be matched to ROLE_STUDENT
//                .authorities(ADMINTRAINEE.getGrantedAuthority())
//                .build();
//
//        return new InMemoryUserDetailsManager(newUser,adminUser,adminTraineeUser);
//    }

    @Bean //?why
    //FOR USERS CREATED IN CUSTOM USER_DETAIL_SERVICE ==> IN DAO
    //CREATE PROVIDER
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder); //allaws password to be encoded
        provider.setUserDetailsService(applicationUserService); //set our custom USER_DETAIL_SERVICE
        return provider;
    }

    //here we should override Configure(AuthenticationManagerBuilder)
    // to set the authenticationProvider for Auth

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider()); //use our custom authenticationProvider
    }
}
