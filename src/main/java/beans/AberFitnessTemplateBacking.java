package beans;

import beans.helpers.LoginCheck;
import oauth.gatekeeper.GatekeeperInfo;

import javax.annotation.PostConstruct;
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

    @PostConstruct
    public void init(){
        isLoggedIn = checkUserLogin();

        if (isLoggedIn){
            GatekeeperInfo userInfo = getUserInfo();
            userEmail = userInfo.getUserName();
        }
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public String getUserEmail() {
        return userEmail;
    }
}
