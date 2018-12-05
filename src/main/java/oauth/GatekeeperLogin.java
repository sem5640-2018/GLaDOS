package oauth;


import beans.OAuthBean;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
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
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.ExecutionException;

@Stateful
public class GatekeeperLogin implements Serializable {

    @EJB
    OAuthBean oAuthBean;

    private GatekeeperOAuth2AccessToken userAccessToken;

    public GatekeeperLogin() {
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

    public void getGatekeeperGetAccessToken(String code) throws InterruptedException, ExecutionException, IOException {
         OAuth20Service aberfitnessService = oAuthBean.getAberfitnessService();
         OAuth2AccessToken inAccessToken = aberfitnessService.getAccessToken(code);
         userAccessToken = (GatekeeperOAuth2AccessToken) inAccessToken;

         System.out.println("USER ID IN GATE AT: " + userAccessToken.getUserId());
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
            String userId = object.get("sub").toString();
            UserType userType = UserType.valueOf(object.get("user_type").toString().toLowerCase());
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
            throw new RuntimeException("Response code was: " + response.getCode());
        }

        return response;
    }

    private Response getUserData(String accessToken) throws InterruptedException, ExecutionException, IOException {
        OAuthRequest oAuthRequest = new OAuthRequest(Verb.GET, EnvironmentVariables.getGatekeeperUserInfoUrl());
        OAuth20Service service = oAuthBean.getAberfitnessService();
        service.signRequest(accessToken, oAuthRequest);
        return service.execute(oAuthRequest);
    }


    public String getUser_id() {
        if (userAccessToken != null)
            return userAccessToken.getUserId();
        return null;
    }
}
