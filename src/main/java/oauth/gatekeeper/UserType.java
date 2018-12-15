package oauth.gatekeeper;

public enum UserType {
    administrator,
    coordinator,
    member,

    // Special Enum for client credentials which have no associated user
    CLIENT_CRED
}
