package oauth.gatekeeper;

public class GatekeeperInfo {
    private String userId;
    private UserType userType;

    public GatekeeperInfo(String userId, UserType userType){
        this.userId = userId;
        this.userType = userType;
    }

    public UserType getUserType() {
        return userType;
    }

    public String getUserId() {
        return userId;
    }
}
