package com.remitano.movieapplication.service;

import com.remitano.movieapplication.exception.CustomException;
import com.remitano.movieapplication.model.User;
import com.remitano.movieapplication.model.UserDetailsImpl;
import com.remitano.movieapplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new CustomException("05"));

        return UserDetailsImpl.build(user);
    }
}
