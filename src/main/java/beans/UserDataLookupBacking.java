package beans;

import entities.AuditData;
import persistence.DatabaseConnection;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Named;
import java.util.Date;
import java.util.List;

@Stateless
@Named
public class UserDataLookupBacking {

    private Date startingTime;
    private Date endingTime;

    private List<AuditData> results;
    private boolean hasRequestedResults = false;

    // TODO
    private String userId = "TODO";
    private boolean currentUserIsAdmin = false;

    @EJB
    private DatabaseConnection db;

    // Invoked methods
    public void lookupResults(){
        // TODO make this specific for the current user
        hasRequestedResults = true;
        results = db.getAllLogEntries();
    }

    // ---- Getters only -----

    public boolean getHasRequestedResults(){
        return hasRequestedResults;
    }

    public boolean getCurrentUserIsAdmin(){
        return currentUserIsAdmin;
    }

    public List<AuditData> getResults(){
        return results;
    }

    // ----- Setters and Getters ------

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        if (currentUserIsAdmin){
            // Only Admins can lookup other users
            this.userId = userId;
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
