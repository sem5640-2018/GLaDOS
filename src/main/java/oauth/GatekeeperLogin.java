package oauth;


import beans.OAuthBean;
import com.github.scribejava.core.model.*;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nimbusds.jwt.JWTClaimsSet;
import configuration.EnvironmentVariables;
import oauth.gatekeeper.GatekeeperInfo;
import oauth.gatekeeper.GatekeeperJsonTokenExtractor;
import oauth.gatekeeper.GatekeeperOAuth2AccessToken;
import oauth.gatekeeper.UserType;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.ExecutionException;

@Stateful
public class GatekeeperLogin implements Serializable {

    @EJB
    OAuthBean oAuthBean;

    /// The name used to store a token in a session
    private static final String TOKEN_STORAGE = "ACCESS_TOKEN";

    private GatekeeperOAuth2AccessToken userAccessToken;

    public GatekeeperLogin() {
    }

    /**
     * Attempts to get the access token for the current session,
     * if one exists
     * @param session The session to get the token from
     * @return True if it was obtained, else false
     */
    public boolean getSessionToken(HttpSession session){
        Object storedToken = session.getAttribute(TOKEN_STORAGE);
        if (storedToken == null){
            return false;
        }

        userAccessToken = (GatekeeperOAuth2AccessToken) storedToken;
        return true;
    }

    /**
     * Stores the current access token in the session given
     * @param session The session to store the token into
     */
    public void storeCurrentSessionToken(HttpSession session){
        session.setAttribute(TOKEN_STORAGE, userAccessToken);
    }

    /**
     * Invalidates the token in the given session
     */
    public void invalidateSessionToken(HttpSession session){
        if (session != null) {
            session.setAttribute(TOKEN_STORAGE, null);
        }
    }

    public void setupOauthCall(String callback, String nextState){
        oAuthBean.initGatekeeperService(callback, nextState, "openid profile offline_access");
    }

    public void redirectToGatekeeper(String callback, String state) throws IOException {
        setupOauthCall(callback, state);
        String url = oAuthBean.getAberfitnessService().getAuthorizationUrl();

        FacesContext.getCurrentInstance()
                .getExternalContext().redirect(url);
    }

    public boolean getGatekeeperAccessToken(String code) throws InterruptedException, ExecutionException, IOException {
         OAuth20Service aberfitnessService = oAuthBean.getAberfitnessService();
         OAuth2AccessToken inAccessToken = null;
         inAccessToken = aberfitnessService.getAccessToken(code);
         userAccessToken = (GatekeeperOAuth2AccessToken) inAccessToken;

         System.out.println("USER ID IN GATE AT: " + userAccessToken.getUserId());
         return true;
    }

    public boolean validateAccessToken() {
        try {
            JWTClaimsSet claimsSet = GatekeeperJsonTokenExtractor.instance().getJWTClaimSet(userAccessToken.getAccessToken());
            System.out.println("Token Issued By: " + claimsSet.getIssuer());

            return true;
        } catch (Exception e) {
            System.err.println("[GatekeeperLogin.validateAccessToken] Message:" + e.getMessage() + " Cause: " + e.getCause());
            return false;
        }
    }

    public GatekeeperInfo getUserInfo() throws RuntimeException {
        Response response = getUserInfoResponse(userAccessToken.getAccessToken());
        JsonParser parser = new JsonParser();

        JsonObject object = null;

        try {
            object = parser.parse(response.getBody()).getAsJsonObject();
        } catch (IOException e) {
            throw new RuntimeException(e.getCause());
        }

        if (object.has("user_type") && object.has("sub")) {
            // Replace extra " with empty space
            String userId = object.get("sub").toString().replace("\"", "");
            String userTypeString = object.get("user_type").toString().toLowerCase().replace("\"", "");
            UserType userType = UserType.valueOf(userTypeString);
            return new GatekeeperInfo(userId, userType);
        }

        // See why we failed
        if (!object.has("user_type")) {
            throw new RuntimeException("No user_type found");
        }

        throw new RuntimeException("No user ID found");
    }

    private Response getUserInfoResponse(String accessToken) throws RuntimeException {
        Response response;
        try {
            response = getUserData(accessToken);
        } catch (InterruptedException | ExecutionException | IOException e) {
            throw new RuntimeException(e.getCause());
        }

        if (!response.isSuccessful()) {
            throw new RuntimeException("Failed to get User Information. Response code was: " + response.getCode());
        }

        return response;
    }

    private Response getUserData(String accessToken) throws InterruptedException, ExecutionException, IOException {
        OAuthRequest oAuthRequest = new OAuthRequest(Verb.GET, EnvironmentVariables.getGatekeeperUserInfoUrl());
        OAuth20Service service = oAuthBean.getAberfitnessService();
        service.signRequest(accessToken, oAuthRequest);
        return service.execute(oAuthRequest);
    }
}
