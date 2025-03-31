package org.example.database;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.example.user.UserType;
import org.example.settings.RestockSettings;
import org.example.clothing.Clothing;

public class DatabaseConnection {
    private boolean createUser(String name, String passwordHash) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public UserType getUserTypeOfUser(int userId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void setUserTypeOfUser(int userId, UserType userType) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public boolean validateLogin(String name, String passwordHash) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void createAccountCreationRequest(String name, String passwordHash) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void approveAccountCreationRequest(int requestId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void denyAccountCreationRequest(int requestId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void setItemRestockSettings(int id, RestockSettings restockSettings) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public Clothing getItemById(int id) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void setItemStockQuantity(int id, int stockQuantity) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void setItemDateLastBought(int id, LocalDate dateLastBought) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
