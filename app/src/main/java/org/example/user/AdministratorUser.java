package org.example.user;

public class AdministratorUser extends StandardUser {
    public AdministratorUser(String name) {
        super(name);
    }

    public void approveAccountCreationRequest(int requestId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void denyAccountCreationRequest(int requestId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void promoteUser(int userId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void demoteUser(int userId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
