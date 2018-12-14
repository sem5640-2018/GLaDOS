package beans;

import beans.helpers.LoginCheck;
import oauth.gatekeeper.GatekeeperInfo;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class AberFitnessTemplateBacking extends LoginCheck implements Serializable {
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
