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

    @EJB
    private DatabaseConnection db;

    // Invoked methods
    public void lookupResults(){
        // TODO make this specific for the current user
        hasRequestedResults = true;
        results = db.getAllLogEntries();
    }

    public boolean getHasRequestedResults(){
        return hasRequestedResults;
    }

    public List<AuditData> getResults(){
        return results;
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
