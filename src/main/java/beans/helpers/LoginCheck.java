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

    public LoginCheck() {
    }

    public boolean checkUserLogin()
            throws IOException {
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

        if(!loginBean.validateAccessToken()){
            response.sendRedirect(EnvironmentVariables.getAppBaseUrl() + "/login.xhtml");
            return false;
        }
        return true;
    }

    public GatekeeperInfo getUserInfo() {
        return loginBean.getUserInfo();
    }
}
