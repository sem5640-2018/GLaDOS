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
    public void initGatekeeperService(String callback, String state, String scope) {
        if (aberfitnessService != null && aberfitnessService.getCallback().equals(callback) && aberfitnessService.getScope().equals(scope))
            return;

        if (EnvironmentVariables.isAberfitnessDataPresent()) {
            aberfitnessService = new ServiceBuilder(EnvironmentVariables.getAberfitnessClientId())
                    .apiSecret(EnvironmentVariables.getAberfitnessClientSecret())
                    .scope(scope)
                    .callback(callback)
                    .state(state)
                    .build(GatekeeperApi.instance());
        }
    }

    public OAuth20Service getAberfitnessService() {
        return aberfitnessService;
    }
}