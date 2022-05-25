package com.quintrix.carportal.customer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.quintrix.carportal.customer.entity.CustomerUser;
import com.quintrix.carportal.customer.repository.UserRepository;

public class CustomUserDetailsService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    CustomerUser user = userRepository.findByUsername(username);
    if (user == null) {
      throw new UsernameNotFoundException(username);
    }

    return new CustomUserDetails(user);
  }
}
