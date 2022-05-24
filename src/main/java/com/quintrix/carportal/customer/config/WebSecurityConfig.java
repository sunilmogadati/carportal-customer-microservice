package com.quintrix.carportal.customer.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

  @Autowired
  private UserDetailsService userDetailsService;

  AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailsService);
    provider.setPasswordEncoder(new BCryptPasswordEncoder());
    return provider;
  }

  // responsible for locking unauthorized users from accessing specific pages
  /*
   * @Bean public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
   * http.authorizeRequests().antMatchers("/").permitAll().antMatchers("/login").permitAll()
   * .antMatchers("/signup").permitAll().antMatchers("/home").hasAuthority("CUSTOMER")
   * .anyRequest().authenticated().and().httpBasic(); }
   */

  /*
   * @Bean public WebSecurityCustomizer webSecurityCustomizer() { return (web) ->
   * web.ignoring().antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "images/**"); }
   */
}
