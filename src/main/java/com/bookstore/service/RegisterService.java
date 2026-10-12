package com.bookstore.service;

import com.bookstore.entity.Cart;
import com.bookstore.entity.User;
import com.bookstore.exception.DuplicateUsernameException;
import com.bookstore.repository.CartRepository;
import com.bookstore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Cart register(User user){

        boolean userNameExists = userRepository.existsByUsername(user.getUsername());

        if(userNameExists){
            throw new DuplicateUsernameException("Username Already Exists");
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        User savedUser = userRepository.save(user);

        Cart cart = new Cart();
        cart.setUser(savedUser);
        cartRepository.save(cart);

        return cart;
    }
}
