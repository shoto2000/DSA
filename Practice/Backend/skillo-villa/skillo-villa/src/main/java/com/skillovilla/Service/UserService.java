package com.skillovilla.Service;

import com.skillovilla.Model.User;

public interface UserService {
    public User addUser(User user);

    public User gindUserById(int userId);
}
