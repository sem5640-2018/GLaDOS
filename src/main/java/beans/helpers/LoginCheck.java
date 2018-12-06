package beans.helpers;

import oauth.GatekeeperLogin;
import oauth.gatekeeper.GatekeeperInfo;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class LoginCheck {

    @EJB
    private GatekeeperLogin loginBean;

    public LoginCheck() {
    }

    public boolean checkUserLogin() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();


        HttpSession session = request.getSession();

        if(session== null ||
                !loginBean.getSessionToken(session) ||
                !loginBean.validateAccessToken()){

            loginBean.invalidateSessionToken(session);
            return false;
        }
        return true;
    }

    public GatekeeperInfo getUserInfo() {
        return loginBean.getUserInfo();
    }
}
