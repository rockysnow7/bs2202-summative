package org.example.requests;

/**
 * Represents an account creation request.
 */
public class AccountCreationRequest {
    public int requestId;
    public String username;
    public String passwordHash;

    public AccountCreationRequest(int requestId, String username, String passwordHash) {
        this.requestId = requestId;
        this.username = username;
        this.passwordHash = passwordHash;
    }
}
