package beans;

import beans.helpers.LoginCheck;
import entities.AuditData;
import oauth.gatekeeper.GatekeeperInfo;
import oauth.gatekeeper.UserType;
import persistence.DatabaseConnection;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Named;
import java.io.IOException;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Stateless
@Named
public class UserDataLookupBacking extends LoginCheck {

    private Date startingTime;
    private Date endingTime;

    private List<AuditData> results;
    private boolean hasRequestedResults = false;

    private String userToLookup;
    private boolean currentUserIsAdmin = false;

    @EJB
    private DatabaseConnection db;

    public UserDataLookupBacking(){
        super();
    }

    @PostConstruct
    public void init(){
        setRangeToNow();
    }

    private void setRangeToNow() {
        Calendar cal = Calendar.getInstance();
        endingTime = cal.getTime();
        // Subtract one year from now
        cal.add(Calendar.YEAR, -1);
        startingTime = cal.getTime();
    }

    // Invoked methods
    public void onLoad() throws IOException {
        // Ensure we have their access token validated for this page
        boolean loggedIn = checkUserLogin();

        if (!loggedIn) {
            startLoginFlow();

            return;
        }

        GatekeeperInfo userInfo = getUserInfo();

        currentUserIsAdmin = userInfo.getUserType() == UserType.administrator;

        if (userToLookup == null || userToLookup.isEmpty()) {
            userToLookup = userInfo.getUserId();
        }
    }

    public String convertToDate(String millisSinceEpoch){
        long millis = Long.parseLong(millisSinceEpoch);
        return Date.from(Instant.ofEpochMilli(millis)).toString();
    }

    public void lookupResults() {
        hasRequestedResults = true;

        if (startingTime == null || endingTime == null){
            setRangeToNow();
        }

        results = db.findLogEntry(userToLookup, startingTime.toInstant(), endingTime.toInstant());
    }

    // ---- Getters only -----

    public boolean getHasRequestedResults() {
        return hasRequestedResults;
    }

    public boolean getCurrentUserIsAdmin() {
        return currentUserIsAdmin;
    }

    public List<AuditData> getResults() {
        return results;
    }

    // ----- Setters and Getters ------

    public String getUserToLookup() {
        return userToLookup;
    }

    public void setUserToLookup(String userToLookup) {
        if (currentUserIsAdmin) {
            // Only Admins can lookup other users
            this.userToLookup = userToLookup;
        }
    }

    public Date getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(Date startingTime) {
        this.startingTime = startingTime;
    }

    public Date getEndingTime() {
        return endingTime;
    }

    public void setEndingTime(Date endingTime) {
        this.endingTime = endingTime;
    }
}
