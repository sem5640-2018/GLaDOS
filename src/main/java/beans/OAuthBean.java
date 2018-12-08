package beans;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.oauth.OAuth20Service;
import configuration.EnvironmentVariables;
import oauth.GatekeeperApi;

import javax.ejb.Singleton;

@Singleton
public class OAuthBean {

    private OAuth20Service aberfitnessService;

    /// Default scopes requested for gatekeeper
    private static final String scopes = "openid profile offline_access glados";

    public void initGatekeeperService(String state) {
        if (aberfitnessService != null && state.equals(aberfitnessService.getState()))
            return;

        aberfitnessService = new ServiceBuilder(EnvironmentVariables.getAberfitnessClientId())
                .apiSecret(EnvironmentVariables.getAberfitnessClientSecret())
                .scope(scopes)
                .callback(EnvironmentVariables.getAppBaseUrl() + "/login.xhtml")
                .state(state)
                .build(GatekeeperApi.instance());
    }

    public boolean serviceIsInit(){
        return aberfitnessService != null;
    }

    public OAuth20Service getAberfitnessService() {
        return aberfitnessService;
    }
}