package beans;

import beans.helpers.LoginSession;
import entities.AuditData;
import persistence.DatabaseConnection;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Named;
import java.io.IOException;
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
        checkUserLogin();
        userToLookup = super.getUserId();
    }

    public void lookupResults() {
        // TODO make this specific for the current user
        hasRequestedResults = true;
        results = db.getAllLogEntries();
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
