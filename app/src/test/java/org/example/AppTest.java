/*
 * Unit tests for the stock management system
 */
package org.example;

import org.example.clothing.Clothing;
import org.example.database.DatabaseConnection;
import org.example.enums.UserType;
import org.example.main.App;
import org.example.requests.AccountCreationRequest;
import org.example.settings.RestockSettings;
import org.example.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;

import java.lang.reflect.Method;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AppTest {
    /**
     * Tests for App class validation methods using reflection
     */
    @Nested
    @DisplayName("App Validation Method Tests")
    class AppValidationTests {
        private App app;
        private Method hashPasswordMethod;
        private Method isUsernameValidMethod;
        private Method isPasswordValidMethod;
        private Method isValidQuantityMethod;

        @BeforeEach
        void setUp() throws Exception {
            app = new App();

            // use reflection to access private methods
            hashPasswordMethod = App.class.getDeclaredMethod("hashPassword", String.class);
            hashPasswordMethod.setAccessible(true);
            
            isUsernameValidMethod = App.class.getDeclaredMethod("isUsernameValid", String.class);
            isUsernameValidMethod.setAccessible(true);
            
            isPasswordValidMethod = App.class.getDeclaredMethod("isPasswordValid", String.class);
            isPasswordValidMethod.setAccessible(true);

            isValidQuantityMethod = App.class.getDeclaredMethod("isValidQuantity", int.class);
            isValidQuantityMethod.setAccessible(true);
        }

        @Test
        @DisplayName("Password hashing should be consistent")
        void passwordHashingShouldBeConsistent() throws Exception {
            String password = "testPassword123";
            String hash1 = (String) hashPasswordMethod.invoke(null, password);
            String hash2 = (String) hashPasswordMethod.invoke(null, password);

            assertNotNull(hash1);
            assertEquals(hash1, hash2, "Hash should be consistent for the same input");
        }

        @Test
        @DisplayName("Different passwords should hash to different values")
        void differentPasswordsShouldHashDifferently() throws Exception {
            String password1 = "password1";
            String password2 = "password2";

            String hash1 = (String) hashPasswordMethod.invoke(null, password1);
            String hash2 = (String) hashPasswordMethod.invoke(null, password2);

            assertNotEquals(hash1, hash2, "Different passwords should hash to different values");
        }

        @ParameterizedTest
        @DisplayName("Username validation should work for different inputs")
        @CsvSource({
            "validUser, true",
            "a, true",
            "'', false",
            "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrst, false" // 41 chars
        })
        void usernameShouldValidateCorrectly(String username, boolean expected) throws Exception {
            boolean result = (boolean) isUsernameValidMethod.invoke(null, username);
            assertEquals(expected, result);
        }

        @ParameterizedTest
        @DisplayName("Password validation should work for different inputs")
        @CsvSource({
            "password123, true",
            "a, true",
            "'', false"
        })
        void validPasswordsShouldPassValidation(String password, boolean expected) throws Exception {
            boolean result = (boolean) isPasswordValidMethod.invoke(null, password);
            assertEquals(expected, result);
        }

        @ParameterizedTest
        @DisplayName("Quantity validation should work for different inputs")
        @CsvSource({
            "1, true",
            "0, false",
            "-1, false"
        })
        void quantityShouldValidateCorrectly(int quantity, boolean expected) throws Exception {
            boolean result = (boolean) isValidQuantityMethod.invoke(null, quantity);
            assertEquals(expected, result);
        }
    }

    /**
     * Tests for database operations on the `test` database
     */
    @Nested
    @DisplayName("Database Operation Tests")
    class DatabaseOperationTests {
        private DatabaseConnection databaseConnection = new DatabaseConnection("test");
        private Method deleteUserMethod;

        @BeforeEach
        void setUp() throws Exception {
            deleteUserMethod = DatabaseConnection.class.getDeclaredMethod("deleteUser", int.class);
            deleteUserMethod.setAccessible(true);
        }

        @Test
        @DisplayName("Should be able to get all users")
        void shouldBeAbleToGetAllUsers() {
            ArrayList<User> users = databaseConnection.getAllUsers();
            assertEquals(2, users.size());

            User finn = users.get(0);
            assertEquals(1, finn.userId);
            assertEquals("finn", finn.username);
            assertNull(finn.passwordHash);
            assertEquals(UserType.ADMIN, finn.userType);

            User stan = users.get(1);
            assertEquals(2, stan.userId);
            assertEquals("stan", stan.username);
            assertNull(stan.passwordHash);
            assertEquals(UserType.STANDARD, stan.userType);
        }

        @Test
        @DisplayName("Should be able to get user type of user")
        void shouldBeAbleToGetUserTypeOfUser() {
            UserType userType = databaseConnection.getUserTypeOfUser(1);
            assertEquals(UserType.ADMIN, userType);

            userType = databaseConnection.getUserTypeOfUser(2);
            assertEquals(UserType.STANDARD, userType);
        }

        @Test
        @DisplayName("Should be able to set user type of user")
        void shouldBeAbleToSetUserTypeOfUser() {
            databaseConnection.setUserTypeOfUser(1, UserType.STANDARD);
            assertEquals(UserType.STANDARD, databaseConnection.getUserTypeOfUser(1));

            // reset user type
            databaseConnection.setUserTypeOfUser(1, UserType.ADMIN);
            assertEquals(UserType.ADMIN, databaseConnection.getUserTypeOfUser(1));
        }

        @ParameterizedTest
        @DisplayName("Should be able to validate login")
        @CsvSource({
            "finn, 5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8, 1",
            "stan, 1a5e497a2bfa7bfd8aab38a1d576ed882f4a82e855ec610880b4c186ec3f4e73, 2",
            "addie, nope, -1"
        })
        void shouldBeAbleToValidateLogin(String username, String passwordHash, int expectedUserId) {
            int userId = databaseConnection.validateLogin(username, passwordHash);
            assertEquals(expectedUserId, userId);
        }

        @Test
        @DisplayName("Should be able to get all account creation requests")
        void shouldBeAbleToGetAllAccountCreationRequests() {
            ArrayList<AccountCreationRequest> requests = databaseConnection.getAllAccountCreationRequests();
            assertEquals(1, requests.size());

            AccountCreationRequest request = requests.get(0);
            assertEquals(1, request.requestId);
            assertEquals("addie", request.username);
            assertNull(request.passwordHash);
        }

        @Test
        @DisplayName("Should be able to create account creation request")
        void shouldBeAbleToCreateAccountCreationRequest() {
            databaseConnection.createAccountCreationRequest("testUser", "testPasswordHash");
            ArrayList<AccountCreationRequest> requests = databaseConnection.getAllAccountCreationRequests();
            assertEquals(2, requests.size());

            AccountCreationRequest request = requests.get(1);
            assertEquals("testUser", request.username);

            // delete the request
            databaseConnection.denyAccountCreationRequest(request.requestId);
            requests = databaseConnection.getAllAccountCreationRequests();
            assertEquals(1, requests.size());
        }

        @Test
        @DisplayName("Should be able to approve account creation request")
        void shouldBeAbleToApproveAccountCreationRequest() throws Exception {
            // create a new request
            databaseConnection.createAccountCreationRequest("testUser", "testPasswordHash");
            ArrayList<AccountCreationRequest> requests = databaseConnection.getAllAccountCreationRequests();
            assertEquals(2, requests.size());

            // get the size of the users table before approving the request
            int usersSizeBefore = databaseConnection.getAllUsers().size();

            // approve the request
            databaseConnection.approveAccountCreationRequest(requests.get(1).requestId);
            requests = databaseConnection.getAllAccountCreationRequests();
            assertEquals(1, requests.size());

            // check that the user was created
            ArrayList<User> users = databaseConnection.getAllUsers();
            assertEquals(usersSizeBefore + 1, users.size());

            // check that the user was created with the correct details
            User user = users.get(users.size() - 1);
            assertEquals("testUser", user.username);
            assertEquals(UserType.STANDARD, user.userType);

            // delete the user
            deleteUserMethod.invoke(databaseConnection, user.userId);
            users = databaseConnection.getAllUsers();
            assertEquals(usersSizeBefore, users.size());
        }

        @Test
        @DisplayName("Should be able to deny account creation request")
        void shouldBeAbleToDenyAccountCreationRequest() {
            // create a new request
            databaseConnection.createAccountCreationRequest("testUser", "testPasswordHash");
            ArrayList<AccountCreationRequest> requests = databaseConnection.getAllAccountCreationRequests();
            assertEquals(2, requests.size());

            // deny the request
            databaseConnection.denyAccountCreationRequest(requests.get(1).requestId);
            requests = databaseConnection.getAllAccountCreationRequests();
            assertEquals(1, requests.size());
        }

        @Test
        @DisplayName("Should be able to get restock settings")
        void shouldBeAbleToGetRestockSettings() {
            RestockSettings restockSettings = databaseConnection.getRestockSettings(1);
            System.out.println("itemId: " + restockSettings.itemId);
            System.out.println("restockAutomatically: " + restockSettings.restockAutomatically);
            System.out.println("minimumStockQuantity: " + restockSettings.minimumStockQuantity);
            assertEquals(1, restockSettings.itemId);
            assertEquals(false, restockSettings.restockAutomatically);
            assertEquals(0, restockSettings.minimumStockQuantity);
        }

        @Test
        @DisplayName("Should be able to set restock settings")
        void shouldBeAbleToSetRestockSettings() {
            // get original restock settings
            RestockSettings originalRestockSettings = databaseConnection.getRestockSettings(1);

            // set new restock settings
            RestockSettings restockSettings = new RestockSettings(1, true, 10);
            databaseConnection.setRestockSettings(restockSettings);

            RestockSettings newRestockSettings = databaseConnection.getRestockSettings(1);
            assertEquals(restockSettings.itemId, newRestockSettings.itemId);
            assertEquals(restockSettings.restockAutomatically, newRestockSettings.restockAutomatically);
            assertEquals(restockSettings.minimumStockQuantity, newRestockSettings.minimumStockQuantity);

            // reset restock settings
            databaseConnection.setRestockSettings(originalRestockSettings);
            newRestockSettings = databaseConnection.getRestockSettings(1);
            assertEquals(originalRestockSettings.itemId, newRestockSettings.itemId);
            assertEquals(originalRestockSettings.restockAutomatically, newRestockSettings.restockAutomatically);
            assertEquals(originalRestockSettings.minimumStockQuantity, newRestockSettings.minimumStockQuantity);
        }
    }
}
