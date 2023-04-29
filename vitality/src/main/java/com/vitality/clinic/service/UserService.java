package com.vitality.clinic.service;


import com.vitality.clinic.model.User;
import com.vitality.clinic.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserByLogin(String login) {
        return userRepository.findByLogin(login).orElse(null);
    }
}
