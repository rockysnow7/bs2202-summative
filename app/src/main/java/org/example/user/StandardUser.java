package org.example.user;

import org.example.database.DatabaseConnection;

public class StandardUser {
    public String name;

    public StandardUser(String name) {
        this.name = name;
    }

    public boolean logIn(String name, String password, DatabaseConnection databaseConnection) {
        return databaseConnection.validateLogin(name, password);
    }

    public void logOut() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void requestAccountCreation(String name, String password) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
