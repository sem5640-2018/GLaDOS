package beans;

import beans.helpers.LoginCheck;
import oauth.gatekeeper.GatekeeperInfo;

import javax.ejb.Stateless;
import javax.inject.Named;

@Named
@Stateless
public class AberFitnessTemplateBacking extends LoginCheck {
    private boolean isLoggedIn;
    private String userEmail;

    public AberFitnessTemplateBacking(){
        super();
    }

    public void onLoad(){
        isLoggedIn = checkUserLogin();

        if (isLoggedIn){
            GatekeeperInfo userInfo = getUserInfo();
            userEmail = userInfo.getUserName();
        } else {
            userEmail = null;
        }
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public String getUserEmail() {
        return userEmail;
    }
}
