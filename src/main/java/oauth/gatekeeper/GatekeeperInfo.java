package oauth.gatekeeper;

public class GatekeeperInfo {
    private String userId;
    private String userName;
    private UserType userType;
    private boolean isClientCred;

    public GatekeeperInfo(boolean isClientCred){
        if (!isClientCred){
            throw new RuntimeException("Trying to set a user credential with the client cred constructor");
        }

        this.isClientCred = true;
        this.userType = UserType.CLIENT_CRED;
        // Ensure this is not null so we can still do .equals with a NPE
        this.userId = "";
    }

    public GatekeeperInfo(String userId, UserType userType, String userName){
        // If we have a user ID this has to be an access token
        this.isClientCred = false;
        this.userId = userId;
        this.userType = userType;
        this.userName = userName;
    }

    public UserType getUserType() {
        return userType;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() { return userName; }

    public boolean isClientCred() { return isClientCred; }
}
