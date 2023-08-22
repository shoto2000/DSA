package com.skillovilla.Service;

import com.skillovilla.Model.User;
import com.skillovilla.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User gindUserById(int userId) {
        return null;
    }
}
