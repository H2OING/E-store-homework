package com.example.demo.config;

import com.example.demo.Service.Web_User_Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration{
    private Web_User_Service webUserService;

    public SecurityConfiguration(@Lazy Web_User_Service webUserService) {
        this.webUserService = webUserService;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(AuthenticationManagerBuilder auth){
        return (web) -> auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(webUserService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeHttpRequests().antMatchers("/admin/**").hasRole("ADMIN").antMatchers("/user/**").hasRole("CUSTOMER").antMatchers("/").permitAll()
                .antMatchers("/**", "/register**", "/js/**", "/css/**", "/img/**").permitAll()
                .anyRequest().authenticated().and().formLogin().
                loginPage("/login").permitAll().and().logout().invalidateHttpSession(true).
                clearAuthentication(true).logoutRequestMatcher(new AntPathRequestMatcher("/logout")).
                logoutSuccessUrl("/login?logout").permitAll();
        return http.build();
    }
}