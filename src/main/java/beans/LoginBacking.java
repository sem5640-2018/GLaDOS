package beans;

import configuration.EnvironmentVariables;
import oauth.GatekeeperLogin;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Stateless
@Named
public class LoginBacking {

    @EJB
    private GatekeeperLogin loginBean;

    private static final String ACCESS_STATE = "access";
    private final static String TOKEN_STATE="token";

    public void onLoad() throws IOException, ExecutionException, InterruptedException {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

        Map<String, String[]> paramMap = request.getParameterMap();
        String state = request.getParameter("state");

        if (state == null) {
            redirectToLoginPage();
            return;
        } else if (state.equals(ACCESS_STATE)) {
            postToken(request.getSession(), response, paramMap);
        } else if (state.equals(TOKEN_STATE)){
            response.sendRedirect(EnvironmentVariables.getAppBaseUrl());
            return;
        }
    }

    private void redirectToLoginPage() throws IOException {
        String redirectUrl = getCallbackUrl();
        loginBean.redirectToGatekeeper(redirectUrl, ACCESS_STATE);
    }

    private void postToken(HttpSession session, HttpServletResponse response, Map<String, String[]> paramMap) throws IOException, InterruptedException, ExecutionException {
        if (!paramMap.containsKey("code") ||
                !paramMap.containsKey("scope") ||
                !paramMap.containsKey("state")) {
            // We are in an inconsistent state - send them back
            response.sendRedirect(EnvironmentVariables.getAppBaseUrl());
            return;
        }

        // URL to redirect back to

        loginBean.setupOauthCall(getCallbackUrl(), TOKEN_STATE);
        if (!loginBean.getGatekeeperAccessToken(paramMap.get("code")[0])){
            // Failed to login
            response.sendError(401);
            return;
        }

        // Logged in
        loginBean.storeCurrentSessionToken(session);
        response.sendRedirect(EnvironmentVariables.getAppBaseUrl());
    }

    private String getCallbackUrl(){
        return EnvironmentVariables.getAppBaseUrl() + "/login.xhtml";
    }


}
