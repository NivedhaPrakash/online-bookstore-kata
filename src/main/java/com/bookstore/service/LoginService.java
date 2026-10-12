package com.bookstore.service;

import com.bookstore.entity.User;
import com.bookstore.exception.BadCredentialsException;
import com.bookstore.exception.InvalidFieldException;
import com.bookstore.repository.UserRepository;
import com.bookstore.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public String login(String username, String password) {

        if(!userRepository.existsByUsername(username)){
            throw new InvalidFieldException("Invalid username or password");
        }

        User user = userRepository.findByUsername(username);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        return jwtTokenUtil.generateToken(username);
    }
}

