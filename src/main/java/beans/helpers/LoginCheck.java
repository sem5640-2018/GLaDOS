package beans.helpers;

import configuration.EnvironmentVariables;
import oauth.GatekeeperLogin;
import oauth.gatekeeper.GatekeeperInfo;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class LoginCheck {

    @EJB
    private GatekeeperLogin loginBean;

    private static final String userParam = "userId";
    private static final String ACCESS_STATE = "access";

    private String userId;

    public LoginCheck() {
    }

    public LoginState checkUserLogin()
            throws IOException {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

        String authorization = request.getHeader("Authorization");
        Map<String, String[]> paramMap = request.getParameterMap();

        if (authorization != null && authorization.startsWith("Bearer")) {
            String[] authHead = authorization.split(" ", 2);

            if (authHead[1] == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No Access Token Parameter!");
                return LoginState.ERROR;
            }

            if (!paramMap.containsKey(userParam)) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No User ID Parameter!");
                return LoginState.ERROR;
            }

            if (!loginBean.validateAccessToken()) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Access Token!");
                return LoginState.UNAUTHORISED;
            }
            // Logged in
            userId = loginBean.getUser_id();
            return LoginState.LOGGED_IN;

        }
        response.sendRedirect(EnvironmentVariables.getAppBaseUrl() + "/login.xhtml");

        return LoginState.REDIRECT;
    }



    public GatekeeperInfo getUserInfo() {
        return loginBean.getUserInfo();
    }

    protected String getUserId() {
        return userId;
    }
}
