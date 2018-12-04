package configuration;

import oauth.gatekeeper.GatekeeperOAuth2AccessToken;

/**
 * Static class for holding the Application's AccessToken
 * @author James H Britton
 */
public final class AuthStorage {

    private static GatekeeperOAuth2AccessToken applicationToken;

    /**
     * Function for checking if the access token has expired or not.
     * @return true still valid, false not valid
     */
    public static boolean isAppTokenValid() {
        //TODO implement
        return false;
    }

    public static GatekeeperOAuth2AccessToken getApplicationToken() {
        return applicationToken;
    }

    public static void setApplicationToken(GatekeeperOAuth2AccessToken applicationToken) {
        AuthStorage.applicationToken = applicationToken;
    }
}
