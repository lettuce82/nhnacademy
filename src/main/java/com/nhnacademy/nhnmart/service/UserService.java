package com.nhnacademy.nhnmart.service;

import com.nhnacademy.nhnmart.domain.User;
import com.nhnacademy.nhnmart.repository.user.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
       this.userRepository = userRepository;
    }

    public User getUser(String sessionId) {
        return userRepository.getUser(sessionId);
    }

    public boolean match(String id, String password) {
        return userRepository.match(id, password);
    }
}
