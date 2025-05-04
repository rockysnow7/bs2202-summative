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
import org.example.enums.ClosureType;
import org.example.enums.CuffStyle;
import org.example.enums.HeelHeight;
import org.example.enums.NeckType;
import org.example.enums.SleeveType;
import org.example.enums.SoleType;
import org.example.enums.ToeStyle;
import org.example.enums.UserType;
import org.example.requests.AccountCreationRequest;
import org.example.shirt.TShirt;
import org.example.shoes.AthleticShoes;
import org.example.shoes.DressShoes;
import org.example.shoes.Shoes;

/**
 * A class that handles the connection to and interaction with the database.
 */
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
    
    /**
     * Opens a connection to the database.
     * @throws SQLException if the connection fails
     */
    private void connect() throws SQLException {
        Properties connectionProps = new Properties();
        connectionProps.put("user", DB_USER);
        connectionProps.put("useSSL", "false");
        connectionProps.put("serverTimezone", "UTC");
        
        connection = DriverManager.getConnection(DB_URL, connectionProps);
        System.out.println("Database connection established successfully");
    }

    /**
     * Gets the connection to the database. If the connection is closed, it will attempt to reconnect.
     * @return a connection to the database
     * @throws SQLException if the connection fails
     */
    private Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connect();
        }
        return connection;
    }
    
    /**
     * Creates a new user in the `users` table.
     * @param username the username of the user
     * @param passwordHash the password hash of the user
     * @param userType the type of user
     */
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

    /**
     * Returns a list of all users in the `users` table.
     * @return a list of all users
     */
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

    /**
     * Returns the type of user for the given user ID in the `users` table.
     * @param userId the ID of the user
     * @return the type of user
     */
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

    /**
     * Sets the type of user for the given user ID in the `users` table.
     * @param userId the ID of the user
     * @param userType the type of user
     */
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

    /**
     * Tests if a user exists with the given username and password hash in the `users` table.
     * @param username the username of the user
     * @param passwordHash the password hash of the user
     * @return the user ID if the user exists, -1 otherwise
     */
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

    /**
     * Gets all account creation requests from the `account_creation_requests` table.
     * @return a list of all account creation requests
     */
    public ArrayList<AccountCreationRequest> getAllAccountCreationRequests() {
        try {
            String query = "SELECT request_id, username FROM account_creation_requests";
            try (PreparedStatement statement = getConnection().prepareStatement(query)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    ArrayList<AccountCreationRequest> requests = new ArrayList<>();
                    while (resultSet.next()) {
                        int requestId = resultSet.getInt("request_id");
                        String username = resultSet.getString("username");
                        String passwordHash = resultSet.getString("password_hash");
                        requests.add(new AccountCreationRequest(requestId, username, passwordHash));
                    }
                    return requests;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting account creation requests: " + e.getMessage());
            return null;
        }
    }

    /**
     * Gets the account creation request from the `account_creation_requests` table with the given request ID.
     * @param requestId the ID of the account creation request
     * @return the account creation request
     */
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

    /**
     * Creates a new account creation request in the `account_creation_requests` table with the given username and password hash.
     * @param username the username of the user
     * @param passwordHash the password hash of the user
     */
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

    /**
     * Deletes the account creation request in the `account_creation_requests` table with the given request ID.
     * @param requestId the ID of the account creation request
     */
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

    /**
     * Creates a new user in the `users` table from the account creation request in the `account_creation_requests` table with the given request ID and deletes the account creation request.
     * @param requestId the ID of the account creation request
     */
    public void approveAccountCreationRequest(int requestId) {
        AccountCreationRequest request = getAccountCreationRequest(requestId);
        if (request == null) {
            System.err.println("Account creation request not found");
            return;
        }

        createUser(request.username, request.passwordHash, UserType.STANDARD);
        deleteAccountCreationRequest(requestId);
    }

    /**
     * Deletes the account creation request in the `account_creation_requests` table with the given request ID without creating a user.
     * @param requestId the ID of the account creation request
     */
    public void denyAccountCreationRequest(int requestId) {
        deleteAccountCreationRequest(requestId);
    }

    /**
     * Gets the restock settings for the given item ID in the `restock_settings` table.
     * @param itemId the ID of the item
     * @return the restock settings
     */
    public RestockSettings getRestockSettings(int itemId) {
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

    /**
     * Sets the given restock settings in the `restock_settings` table.
     * @param restockSettings the restock settings to set
     */
    public void setRestockSettings(RestockSettings restockSettings) {
        try {
            String query = "UPDATE restock_settings SET restock_automatically = ?, minimum_stock_quantity = ? WHERE item_id = ?";
            try (PreparedStatement statement = getConnection().prepareStatement(query)) {
                statement.setBoolean(1, restockSettings.restockAutomatically);
                statement.setInt(2, restockSettings.minimumStockQuantity);
                statement.setInt(3, restockSettings.itemId);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Error setting restock settings: " + e.getMessage());
        }
    }

    /**
     * Sets the stock quantity of the item with the given item ID in the `items` table to the minimum stock quantity if the stock quantity is less than the minimum stock quantity.
     * @param itemId the ID of the item to restock
     * @return the new stock quantity if the item was restocked, -1 otherwise
     */
    public int restockItem(int itemId) {
        RestockSettings restockSettings = getRestockSettings(itemId);
        if (restockSettings == null) {
            return -1;
        }
        if (!restockSettings.restockAutomatically) {
            return -1;
        }

        Clothing item = getItemById(itemId);
        if (item == null) {
            return -1;
        }
        if (item.stockQuantity >= restockSettings.minimumStockQuantity) {
            return -1;
        }

        buyItem(itemId, restockSettings.minimumStockQuantity - item.stockQuantity);
        return restockSettings.minimumStockQuantity;
    }

    /**
     * Gets the `TShirt` object for the given item ID in the `t_shirts` table, using the given parameters.
     * @param itemId the ID of the item
     * @param name the display name of the item
     * @param brand the name of the brand of the item
     * @param size the size of the item
     * @param colour the colour of the item
     * @param material the material of the item
     * @param dateLastBought the date the item was last bought
     * @param stockQuantity the stock quantity of the item
     * @param cost the cost price of the item
     * @param price the selling price of the item
     * @param restockSettings the restock settings of the item
     * @param imagePath the path to the image of the item
     * @param sleeveType the sleeve type of the item
     * @param neckType the neck type of the item
     * @param pattern the pattern of the item
     * @param numPockets the number of pockets on the item
     * @return the `TShirt` object
     */
    private TShirt getTShirt(
        int itemId,
        String name,
        String brand,
        int size,
        String colour,
        String material,
        LocalDate dateLastBought,
        int stockQuantity,
        double cost,
        double price,
        RestockSettings restockSettings,
        String imagePath,
        SleeveType sleeveType,
        NeckType neckType,
        String pattern,
        int numPockets
    ) {
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
                        cost,
                        price,
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

    /**
     * Gets the `ButtonUpShirt` object for the given item ID in the `button_up_shirts` table, using the given parameters.
     * @param itemId the ID of the item
     * @param name the display name of the item
     * @param brand the name of the brand of the item
     * @param size the size of the item
     * @param colour the colour of the item
     * @param material the material of the item
     * @param dateLastBought the date the item was last bought
     * @param stockQuantity the stock quantity of the item
     * @param cost the cost price of the item
     * @param price the selling price of the item
     * @param restockSettings the restock settings of the item
     * @param imagePath the path to the image of the item
     * @param sleeveType the sleeve type of the item
     * @param neckType the neck type of the item
     * @param pattern the pattern of the item
     * @param numPockets the number of pockets on the item
     * @return the `ButtonUpShirt` object
     */
    private ButtonUpShirt getButtonUpShirt(
        int itemId,
        String name,
        String brand,
        int size,
        String colour,
        String material,
        LocalDate dateLastBought,
        int stockQuantity,
        double cost,
        double price,
        RestockSettings restockSettings,
        String imagePath,
        SleeveType sleeveType,
        NeckType neckType,
        String pattern,
        int numPockets
    ) {
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
                        cost,
                        price,
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

    /**
     * Gets the `Shirt` object for the given item ID in the `shirts` table, using the given parameters.
     * @param itemId the ID of the item
     * @param name the display name of the item
     * @param brand the name of the brand of the item
     * @param size the size of the item
     * @param colour the colour of the item
     * @param material the material of the item
     * @param dateLastBought the date the item was last bought
     * @param stockQuantity the stock quantity of the item
     * @param cost the cost price of the item
     * @param price the selling price of the item
     * @param restockSettings the restock settings of the item
     * @param imagePath the path to the image of the item
     * @return the `Shirt` object
     */
    private Shirt getShirt(
        int itemId,
        String name,
        String brand,
        int size,
        String colour,
        String material,
        LocalDate dateLastBought,
        int stockQuantity,
        double cost,
        double price,
        RestockSettings restockSettings,
        String imagePath
    ) {
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
                                cost,
                                price,
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
                                cost,
                                price,
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

    /**
     * Gets the `DressShoes` object for the given item ID in the `dress_shoes` table, using the given parameters.
     * @param itemId the ID of the item
     * @param name the display name of the item
     * @param brand the name of the brand of the item
     * @param size the size of the item
     * @param colour the colour of the item
     * @param material the material of the item
     * @param dateLastBought the date the item was last bought
     * @param stockQuantity the stock quantity of the item
     * @param cost the cost price of the item
     * @param price the selling price of the item
     * @param restockSettings the restock settings of the item
     * @param imagePath the path to the image of the item
     * @param soleType the sole type of the item
     * @param closureType the closure type of the item
     * @param heelHeight the heel height of the item
     * @return the `DressShoes` object
     */
    private DressShoes getDressShoes(
        int itemId,
        String name,
        String brand,
        int size,
        String colour,
        String material,
        LocalDate dateLastBought,
        int stockQuantity,
        double cost,
        double price,
        RestockSettings restockSettings,
        String imagePath,
        SoleType soleType,
        ClosureType closureType,
        HeelHeight heelHeight
    ) {
        try {
            String query = "SELECT * FROM dress_shoes WHERE item_id = ?";
            try (PreparedStatement statement = getConnection().prepareStatement(query)) {
                statement.setInt(1, itemId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (!resultSet.next()) {
                        return null;
                    }

                    ToeStyle toeStyle = ToeStyle.valueOf(resultSet.getString("toe_style"));

                    return new DressShoes(
                        itemId,
                        name,
                        brand,
                        size,
                        colour,
                        material,
                        dateLastBought,
                        stockQuantity,
                        cost,
                        price,
                        restockSettings,
                        imagePath,
                        soleType,
                        closureType,
                        heelHeight,
                        toeStyle
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting dress shoes: " + e.getMessage());
            return null;
        }
    }

    /**
     * Gets the `AthleticShoes` object for the given item ID in the `athletic_shoes` table, using the given parameters.
     * @param itemId the ID of the item
     * @param name the display name of the item
     * @param brand the name of the brand of the item
     * @param size the size of the item
     * @param colour the colour of the item
     * @param material the material of the item
     * @param dateLastBought the date the item was last bought
     * @param stockQuantity the stock quantity of the item
     * @param cost the cost price of the item
     * @param price the selling price of the item
     * @param restockSettings the restock settings of the item
     * @param imagePath the path to the image of the item
     * @param soleType the sole type of the item
     * @param closureType the closure type of the item
     * @param heelHeight the heel height of the item
     * @return the `AthleticShoes` object
     */
    private AthleticShoes getAthleticShoes(
        int itemId,
        String name,
        String brand,
        int size,
        String colour,
        String material,
        LocalDate dateLastBought,
        int stockQuantity,
        double cost,
        double price,
        RestockSettings restockSettings,
        String imagePath,
        SoleType soleType,
        ClosureType closureType,
        HeelHeight heelHeight
    ) {
        try {
            String query = "SELECT * FROM athletic_shoes WHERE item_id = ?";
            try (PreparedStatement statement = getConnection().prepareStatement(query)) {
                statement.setInt(1, itemId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (!resultSet.next()) {
                        return null;
                    }

                    String sport = resultSet.getString("sport");

                    return new AthleticShoes(
                        itemId,
                        name,
                        brand,
                        size,
                        colour,
                        material,
                        dateLastBought,
                        stockQuantity,
                        cost,
                        price,
                        restockSettings,
                        imagePath,
                        soleType,
                        closureType,
                        heelHeight,
                        sport
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting athletic shoes: " + e.getMessage());
            return null;
        }
    }

    /**
     * Gets the `Shoes` object for the given item ID in the `shoes` table, using the given parameters.
     * @param itemId the ID of the item
     * @param name the display name of the item
     * @param brand the name of the brand of the item
     * @param size the size of the item
     * @param colour the colour of the item
     * @param material the material of the item
     * @param dateLastBought the date the item was last bought
     * @param stockQuantity the stock quantity of the item
     * @param cost the cost price of the item
     * @param price the selling price of the item
     * @param restockSettings the restock settings of the item
     * @param imagePath the path to the image of the item
     * @return the `Shoes` object
     */
    private Shoes getShoes(
        int itemId,
        String name,
        String brand,
        int size,
        String colour,
        String material,
        LocalDate dateLastBought,
        int stockQuantity,
        double cost,
        double price,
        RestockSettings restockSettings,
        String imagePath
    ) {
        try {
            String query = "SELECT * FROM shoes WHERE item_id = ?";
            try (PreparedStatement statement = getConnection().prepareStatement(query)) {
                statement.setInt(1, itemId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (!resultSet.next()) {
                        return null;
                    }

                    String shoeType = resultSet.getString("shoe_type");
                    SoleType soleType = SoleType.valueOf(resultSet.getString("sole_type"));
                    ClosureType closureType = ClosureType.valueOf(resultSet.getString("closure_type"));
                    HeelHeight heelHeight = HeelHeight.valueOf(resultSet.getString("heel_height"));

                    switch (shoeType) {
                        case "DRESS_SHOES":
                            return getDressShoes(
                                itemId,
                                name,
                                brand,
                                size,
                                colour,
                                material,
                                dateLastBought,
                                stockQuantity,
                                cost,
                                price,
                                restockSettings,
                                imagePath,
                                soleType,
                                closureType,
                                heelHeight
                            );
                        case "ATHLETIC_SHOES":
                            return getAthleticShoes(
                                itemId,
                                name,
                                brand,
                                size,
                                colour,
                                material,
                                dateLastBought,
                                stockQuantity,
                                cost,
                                price,
                                restockSettings,
                                imagePath,
                                soleType,
                                closureType,
                                heelHeight
                            );
                        default:
                            return null;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting shoes: " + e.getMessage());
            return null;
        }
    }

    /**
     * Returns a list of all items in the `items` table.
     * @return a list of all items
     */
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
                        double cost = resultSet.getDouble("cost");
                        double price = resultSet.getDouble("price");
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
                                    cost,
                                    price,
                                    restockSettings,
                                    imagePath
                                );
                                if (shirt != null) {
                                    items.add(shirt);
                                }
                                break;
                            case "SHOES":
                                Shoes shoes = getShoes(
                                    itemId,
                                    name,
                                    brand,
                                    size,
                                    colour,
                                    material,
                                    dateLastBought,
                                    stockQuantity,
                                    cost,
                                    price,
                                    restockSettings,
                                    imagePath
                                );
                                if (shoes != null) {
                                    items.add(shoes);
                                }
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

    /**
     * Returns the `Clothing` object for the given item ID in the `items` table.
     * @param id the ID of the item
     * @return the `Clothing` object
     */
    public Clothing getItemById(int id) {
        String query = "SELECT * FROM items WHERE item_id = ?";
        try (PreparedStatement statement = getConnection().prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return null;
                }

                int itemId = resultSet.getInt("item_id");
                String itemType = resultSet.getString("item_type");
                String name = resultSet.getString("name");
                String brand = resultSet.getString("brand");
                int size = resultSet.getInt("size");
                String colour = resultSet.getString("colour");
                String material = resultSet.getString("material");
                LocalDate dateLastBought = resultSet.getDate("date_last_bought").toLocalDate();
                int stockQuantity = resultSet.getInt("stock_quantity");
                double cost = resultSet.getDouble("cost");
                double price = resultSet.getDouble("price");
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
                            cost,
                            price,
                            restockSettings,
                            imagePath
                        );
                        return shirt;
                    case "SHOES":
                        Shoes shoes = getShoes(
                            itemId,
                            name,
                            brand,
                            size,
                            colour,
                            material,
                            dateLastBought,
                            stockQuantity,
                            cost,
                            price,
                            restockSettings,
                            imagePath
                        );
                        return shoes;
                    default:
                        return null;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting item by ID: " + e.getMessage());
            return null;
        }
    }

    /**
     * Sets the price of the item with the given item ID in the `items` table to the given price.
     * @param itemId the ID of the item
     * @param price the new price of the item
     */
    public void updateItemPrice(int itemId, double price) {
        try {
            String query = "UPDATE items SET price = ? WHERE item_id = ?";
            try (PreparedStatement statement = getConnection().prepareStatement(query)) {
                statement.setDouble(1, price);
                statement.setInt(2, itemId);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Error updating item price: " + e.getMessage());
        }
    }

    /**
     * Increases the stock quantity of the item with the given item ID in the `items` table by the given quantity.
     * @param itemId the ID of the item
     * @param quantity the quantity to increase the stock by
     */
    public void buyItem(int itemId, int quantity) {
        try {
            String quantityQuery = "UPDATE items SET stock_quantity = stock_quantity + ? WHERE item_id = ?";
            try (PreparedStatement quantityStatement = getConnection().prepareStatement(quantityQuery)) {
                quantityStatement.setInt(1, quantity);
                quantityStatement.setInt(2, itemId);
                quantityStatement.executeUpdate();
            }

            String dateLastBoughtQuery = "UPDATE items SET date_last_bought = CURRENT_DATE() WHERE item_id = ?";
            try (PreparedStatement dateLastBoughtStatement = getConnection().prepareStatement(dateLastBoughtQuery)) {
                dateLastBoughtStatement.setInt(1, itemId);
                dateLastBoughtStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Error buying item: " + e.getMessage());
        }
    }

    /**
     * Decreases the stock quantity of the item with the given item ID in the `items` table by the given quantity.
     * @param itemId the ID of the item
     * @param quantity the quantity to decrease the stock by
     */
    public void sellItem(int itemId, int quantity) {
        try {
            String quantityQuery = "UPDATE items SET stock_quantity = stock_quantity - ? WHERE item_id = ?";
            try (PreparedStatement quantityStatement = getConnection().prepareStatement(quantityQuery)) {
                quantityStatement.setInt(1, quantity);
                quantityStatement.setInt(2, itemId);
                quantityStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Error selling item: " + e.getMessage());
        }
    }
}
