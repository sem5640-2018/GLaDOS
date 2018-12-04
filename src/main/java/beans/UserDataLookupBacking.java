package beans;

import beans.helpers.LoginSession;
import entities.AuditData;
import oauth.gatekeeper.GatekeeperInfo;
import oauth.gatekeeper.UserType;
import persistence.DatabaseConnection;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Named;
import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Stateless
@Named
public class UserDataLookupBacking extends LoginSession {

    private Date startingTime;
    private Date endingTime;

    private List<AuditData> results;
    private boolean hasRequestedResults = false;

    private String userToLookup;
    private boolean currentUserIsAdmin = false;

    @EJB
    private DatabaseConnection db;


    // Invoked methods
    public void onLoad() throws IOException {
        // Ensure we have their access token validated for this page
        checkUserLogin();

        GatekeeperInfo userInfo = getUserInfo();

        currentUserIsAdmin = userInfo.getUserType() == UserType.administrator;

        if (userToLookup.isEmpty()) {
            userToLookup = userInfo.getUserId();
        }
    }

    public void lookupResults() {
        hasRequestedResults = true;
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
