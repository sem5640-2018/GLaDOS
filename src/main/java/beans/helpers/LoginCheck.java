package beans.helpers;

import oauth.GatekeeperLogin;
import oauth.gatekeeper.GatekeeperInfo;

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
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

        HttpSession session = request.getSession();

        if(session== null ||
                !loginBean.getSessionToken(session) ||
                !loginBean.validateAccessToken()){

            loginBean.invalidateSessionToken(session);

            try {
                response.sendRedirect("/login.xhtml");
            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;
        }
        return true;
    }

    public GatekeeperInfo getUserInfo() {
        return loginBean.getUserInfo();
    }
}
