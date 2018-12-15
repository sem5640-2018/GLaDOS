package beans.helpers;

import configuration.EnvironmentVariables;
import oauth.GatekeeperLogin;
import oauth.gatekeeper.GatekeeperInfo;
import rest.helpers.AuthStates;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginCheck {

    @EJB
    private GatekeeperLogin loginBean;

    public LoginCheck() {
    }

    public boolean checkUserLogin() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();


        HttpSession session = request.getSession();

        if (session == null ||
                !loginBean.getSessionToken(session) ||
                loginBean.validateInternalJwtToken() != AuthStates.Authorized) {

            loginBean.invalidateSessionToken(session);
            return false;
        }
        return true;
    }

    public void startLoginFlow() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

        HttpSession userSession = request.getSession();

        try {
            userSession.setAttribute("redirectTo", EnvironmentVariables.getAppBaseUrl() + "/userDataLookup.xhtml");
            response.sendRedirect(EnvironmentVariables.getAppBaseUrl() + "/login.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public GatekeeperInfo getUserInfo() {
        return loginBean.getUserInfo();
    }
}
