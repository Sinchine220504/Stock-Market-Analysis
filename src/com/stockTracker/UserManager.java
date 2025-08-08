package com.stockTracker;

import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private static List<User> users = new ArrayList<>();
    private static int nextId = 1;

    public User registerUser(String username, String password) {
        User user = new User();
        user.setUserId(nextId++);
        user.setUsername(username);
        user.setPasswordHash(password);  // In future, hash this
        users.add(user);
        return user;
    }

    public User findUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public boolean loginUser(String username, String password) {
        User user = findUserByUsername(username);
        return user != null && user.getPasswordHash().equals(password);
    }
}

