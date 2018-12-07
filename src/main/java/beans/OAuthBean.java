package beans;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.oauth.OAuth20Service;
import configuration.EnvironmentVariables;
import oauth.GatekeeperApi;

import javax.ejb.Singleton;

@Singleton
public class OAuthBean {

    private OAuth20Service aberfitnessService;


    //"openid profile offline_access"
    public void initGatekeeperService(String state, String scope) {
        if (aberfitnessService != null && aberfitnessService.getScope().equals(scope))
            return;

        aberfitnessService = new ServiceBuilder(EnvironmentVariables.getAberfitnessClientId())
                .apiSecret(EnvironmentVariables.getAberfitnessClientSecret())
                .scope(scope)
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