package org.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Properties;

import org.example.settings.RestockSettings;
import org.example.shirt.ButtonUpShirt;
import org.example.shirt.Shirt;
import org.example.user.User;
import org.example.clothing.Clothing;
import org.example.enums.CuffStyle;
import org.example.enums.NeckType;
import org.example.enums.SleeveType;
import org.example.enums.UserType;
import org.example.requests.AccountCreationRequest;
import org.example.shirt.TShirt;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/clothingstore";
    private static final String DB_USER = "root";
    
    private Connection connection;

    public DatabaseConnection() {
        try {
            connect();
        } catch (SQLException e) {
            System.err.println("Failed to establish database connection: " + e.getMessage());
        }
    }
    
    // Connects to the database.
    private void connect() throws SQLException {
        Properties connectionProps = new Properties();
        connectionProps.put("user", DB_USER);
        connectionProps.put("useSSL", "false");
        connectionProps.put("serverTimezone", "UTC");
        
        connection = DriverManager.getConnection(DB_URL, connectionProps);
        System.out.println("Database connection established successfully");
    }

    // Disconnects from the database.
    private void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed");
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
            }
        }
    }

    // Returns a connection to the database. If the connection is not already established, it will establish a new connection.
    private Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connect();
        }
        return connection;
    }
    
    // Creates a new user in the `users` table.
    private void createUser(String username, String passwordHash, UserType userType) {
        try {
            String query = "INSERT INTO users (username, password_hash, user_type) VALUES (?, ?, ?)";
            try (PreparedStatement statement = getConnection().prepareStatement(query)) {
                statement.setString(1, username);
                statement.setString(2, passwordHash);
                statement.setString(3, userType.toString());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Error creating user: " + e.getMessage());
        }
    }

    // Returns a list of all users in the `users` table.
    public ArrayList<User> getAllUsers() {
        try {
            String query = "SELECT * FROM users";
            try (PreparedStatement statement = getConnection().prepareStatement(query)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    ArrayList<User> users = new ArrayList<>();
                    while (resultSet.next()) {
                        users.add(new User(resultSet.getInt("user_id"), resultSet.getString("username"), null, UserType.valueOf(resultSet.getString("user_type"))));
                    }
                    return users;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting users: " + e.getMessage());
            return null;
        }
    }

    // Returns the type of user for the given user ID in the `users` table.
    public UserType getUserTypeOfUser(int userId) {
        try {
            String query = "SELECT user_type FROM users WHERE user_id = ?";
            try (PreparedStatement statement = getConnection().prepareStatement(query)) {
                statement.setInt(1, userId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next() ? UserType.valueOf(resultSet.getString("user_type")) : null;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting user type: " + e.getMessage());
            return null;
        }
    }

    // Sets the type of user for the given user ID in the `users` table.
    public void setUserTypeOfUser(int userId, UserType userType) {
        try {
            String query = "UPDATE users SET user_type = ? WHERE user_id = ?";
            try (PreparedStatement statement = getConnection().prepareStatement(query)) {
                statement.setString(1, userType.toString());
                statement.setInt(2, userId);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Error setting user type: " + e.getMessage());
        }
    }

    // Tests if a user exists with the given username and password hash in the `users` table. Returns the user ID if the user exists, -1 otherwise.
    public int validateLogin(String username, String passwordHash) {
        try {
            String query = "SELECT * FROM users WHERE username = ? AND password_hash = ?";
            try (PreparedStatement statement = getConnection().prepareStatement(query)) {
                statement.setString(1, username);
                statement.setString(2, passwordHash);
                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next() ? resultSet.getInt("user_id") : -1;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error validating login: " + e.getMessage());
            return -1;
        }
    }

    // Returns a list of all account creation requests in the `account_creation_requests` table. The password hash is null because it is not needed for the user to see it.
    public ArrayList<AccountCreationRequest> getAllAccountCreationRequests() {
        try {
            String query = "SELECT request_id, username FROM account_creation_requests";
            try (PreparedStatement statement = getConnection().prepareStatement(query)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    ArrayList<AccountCreationRequest> requests = new ArrayList<>();
                    while (resultSet.next()) {
                        int requestId = resultSet.getInt("request_id");
                        String username = resultSet.getString("username");
                        requests.add(new AccountCreationRequest(requestId, username, null));
                    }
                    return requests;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting account creation requests: " + e.getMessage());
            return null;
        }
    }

    // Returns the account creation request in the `account_creation_requests` table with the given request ID.
    private AccountCreationRequest getAccountCreationRequest(int requestId) {
        try {
            String query = "SELECT request_id, username, password_hash FROM account_creation_requests WHERE request_id = ?";
            try (PreparedStatement statement = getConnection().prepareStatement(query)) {
                statement.setInt(1, requestId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next() ? new AccountCreationRequest(resultSet.getInt("request_id"), resultSet.getString("username"), resultSet.getString("password_hash")) : null;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting account creation request: " + e.getMessage());
            return null;
        }
    }

    // Creates a new account creation request in the `account_creation_requests` table with the given username and password hash.
    public void createAccountCreationRequest(String username, String passwordHash) {
        try {
            String query = "INSERT INTO account_creation_requests (username, password_hash) VALUES (?, ?)";
            try (PreparedStatement statement = getConnection().prepareStatement(query)) {
                statement.setString(1, username);
                statement.setString(2, passwordHash);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Error creating account creation request: " + e.getMessage());
        }
    }

    // Deletes the account creation request in the `account_creation_requests` table with the given request ID.
    private void deleteAccountCreationRequest(int requestId) {
        try {
            String query = "DELETE FROM account_creation_requests WHERE request_id = ?";
            try (PreparedStatement statement = getConnection().prepareStatement(query)) {
                statement.setInt(1, requestId);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Error deleting account creation request: " + e.getMessage());
        }
    }

    // Approves the account creation request in the `account_creation_requests` table with the given request ID.
    public void approveAccountCreationRequest(int requestId) {
        AccountCreationRequest request = getAccountCreationRequest(requestId);
        if (request == null) {
            System.err.println("Account creation request not found");
            return;
        }

        createUser(request.username, request.passwordHash, UserType.STANDARD);
        deleteAccountCreationRequest(requestId);
    }

    // Denies the account creation request in the `account_creation_requests` table with the given request ID.
    public void denyAccountCreationRequest(int requestId) {
        deleteAccountCreationRequest(requestId);
    }

    private RestockSettings getRestockSettings(int itemId) {
        try {
            String query = "SELECT * FROM restock_settings WHERE item_id = ?";
            try (PreparedStatement statement = getConnection().prepareStatement(query)) {
                statement.setInt(1, itemId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next() ? new RestockSettings(resultSet.getInt("item_id"), resultSet.getBoolean("restock_automatically"), resultSet.getInt("minimum_stock_quantity")) : null;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting restock settings: " + e.getMessage());
            return null;
        }
    }

    public void setRestockSettings(int itemId, RestockSettings restockSettings) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private TShirt getTShirt(int itemId, String name, String brand, int size, String colour, String material, LocalDate dateLastBought, int stockQuantity, RestockSettings restockSettings, String imagePath, SleeveType sleeveType, NeckType neckType, String pattern, int numPockets) {
        try {
            String query = "SELECT * FROM t_shirts WHERE item_id = ?";
            try (PreparedStatement statement = getConnection().prepareStatement(query)) {
                statement.setInt(1, itemId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (!resultSet.next()) {
                        return null;
                    }

                    boolean hasGraphic = resultSet.getBoolean("has_graphic");

                    return new TShirt(
                        itemId,
                        name,
                        brand,
                        size,
                        colour,
                        material,
                        dateLastBought,
                        stockQuantity,
                        restockSettings,
                        imagePath,
                        sleeveType,
                        neckType,
                        pattern,
                        numPockets,
                        hasGraphic
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting t-shirt: " + e.getMessage());
            return null;
        }
    }

    private ButtonUpShirt getButtonUpShirt(int itemId, String name, String brand, int size, String colour, String material, LocalDate dateLastBought, int stockQuantity, RestockSettings restockSettings, String imagePath, SleeveType sleeveType, NeckType neckType, String pattern, int numPockets) {
        try {
            String query = "SELECT * FROM button_up_shirts WHERE item_id = ?";
            try (PreparedStatement statement = getConnection().prepareStatement(query)) {
                statement.setInt(1, itemId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (!resultSet.next()) {
                        return null;
                    }

                    CuffStyle cuffStyle = CuffStyle.valueOf(resultSet.getString("cuff_style"));

                    return new ButtonUpShirt(
                        itemId,
                        name,
                        brand,
                        size,
                        colour,
                        material,
                        dateLastBought,
                        stockQuantity,
                        restockSettings,
                        imagePath,
                        sleeveType,
                        neckType,
                        pattern,
                        numPockets,
                        cuffStyle
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting button up shirt: " + e.getMessage());
            return null;
        }
    }

    private Shirt getShirt(int itemId, String name, String brand, int size, String colour, String material, LocalDate dateLastBought, int stockQuantity, RestockSettings restockSettings, String imagePath) {
        try {
            String query = "SELECT * FROM shirts WHERE item_id = ?";
            try (PreparedStatement statement = getConnection().prepareStatement(query)) {
                statement.setInt(1, itemId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (!resultSet.next()) {
                        return null;
                    }

                    String shirtType = resultSet.getString("shirt_type");
                    SleeveType sleeveType = SleeveType.valueOf(resultSet.getString("sleeve_type"));
                    NeckType neckType = NeckType.valueOf(resultSet.getString("neck_type"));
                    String pattern = resultSet.getString("pattern");
                    int numPockets = resultSet.getInt("num_pockets");

                    switch (shirtType) {
                        case "T_SHIRT":
                            return getTShirt(
                                itemId,
                                name,
                                brand,
                                size,
                                colour,
                                material,
                                dateLastBought,
                                stockQuantity,
                                restockSettings,
                                imagePath,
                                sleeveType,
                                neckType,
                                pattern,
                                numPockets
                            );
                        case "BUTTON_UP":
                            return getButtonUpShirt(
                                itemId,
                                name,
                                brand,
                                size,
                                colour,
                                material,
                                dateLastBought,
                                stockQuantity,
                                restockSettings,
                                imagePath,
                                sleeveType,
                                neckType,
                                pattern,
                                numPockets
                            );
                        default:
                            return null;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting shirt: " + e.getMessage());
            return null;
        }
    }

    public ArrayList<Clothing> getAllItems() {
        try {
            String query = "SELECT * FROM items";
            try (PreparedStatement statement = getConnection().prepareStatement(query)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    ArrayList<Clothing> items = new ArrayList<>();
                    while (resultSet.next()) {
                        int itemId = resultSet.getInt("item_id");
                        String itemType = resultSet.getString("item_type");
                        String name = resultSet.getString("name");
                        String brand = resultSet.getString("brand");
                        int size = resultSet.getInt("size");
                        String colour = resultSet.getString("colour");
                        String material = resultSet.getString("material");
                        LocalDate dateLastBought = resultSet.getDate("date_last_bought").toLocalDate();
                        int stockQuantity = resultSet.getInt("stock_quantity");
                        String imagePath = resultSet.getString("image_path");

                        RestockSettings restockSettings = getRestockSettings(itemId);

                        switch (itemType) {
                            case "SHIRT":
                                Shirt shirt = getShirt(
                                    itemId,
                                    name,
                                    brand,
                                    size,
                                    colour,
                                    material,
                                    dateLastBought,
                                    stockQuantity,
                                    restockSettings,
                                    imagePath
                                );
                                if (shirt != null) {
                                    items.add(shirt);
                                }
                                break;
                            case "SHOES":
                                break;
                        }
                    }
                    return items;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting all items: " + e.getMessage());
            return null;
        }
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
