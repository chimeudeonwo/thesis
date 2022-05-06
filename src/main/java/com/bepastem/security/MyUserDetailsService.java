package com.bepastem.security;

import com.bepastem.jpadao.AuthJpaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

  @Autowired
  private final AuthJpaDao authJpaDao;

  // constructor ...
  public MyUserDetailsService(AuthJpaDao userRepository) {
    this.authJpaDao = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    var user = authJpaDao.getUserByUsername(username);
    if (user == null) {
      throw new UsernameNotFoundException(username);
    }
    return new MyUserDetails(user);
  }
}