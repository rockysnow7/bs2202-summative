package org.example.user;

import org.example.enums.UserType;

public class User {
    public int userId;
    public String username;
    public String passwordHash;
    public UserType userType;

    public User(int userId, String username, String passwordHash, UserType userType) {
        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.userType = userType;
    }
}
