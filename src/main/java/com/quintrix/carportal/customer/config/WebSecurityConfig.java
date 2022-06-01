package com.quintrix.carportal.customer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  // hardcoded user login
  @Override
  protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
    auth.inMemoryAuthentication().withUser("user1").password(passwordEncoder().encode("pass1"))
        .roles("USER").and().withUser("user2").password(passwordEncoder().encode("pass2"))
        .roles("USER").and().withUser("admin").password(passwordEncoder().encode("passwordAdmin"))
        .roles("ADMIN");
  }

  // configure authorize requests
  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http.cors().and().csrf().disable().authorizeRequests()
        .antMatchers(HttpMethod.GET, "/customer/**").hasAnyRole("ADMIN", "USER")
        .antMatchers(HttpMethod.POST, "/customer/**").hasAnyRole("ADMIN")
        .antMatchers(HttpMethod.PUT, "/customer/**").hasRole("ADMIN")
        .antMatchers(HttpMethod.DELETE, "/customer/**").hasRole("ADMIN")
        .antMatchers("login", "/oauth/**").permitAll().anyRequest().authenticated().and()
        .formLogin().permitAll().and().oauth2Login();
  }
}


