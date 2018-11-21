package persistence;

import beans.AuditDataBean ;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.*;
import java.io.IOException;
import java.util.List;

public class DatabaseConnection {
    @PersistenceContext
    private EntityManager em;

    private static Logger log = LogManager.getLogger(DatabaseConnection.class.getName());

    public DatabaseConnection() {
    }

    public void addLogData(AuditDataBean  newLogEntry) throws IOException {
        try {
            em.persist(newLogEntry);
        } catch (Exception e) {
            log.catching(e);
            e.printStackTrace();
            throw new IOException(e.getMessage());
        }

    }

    public AuditDataBean  getLogEntry(String logId) throws NoResultException {
        AuditDataBean  foundRecord;
        TypedQuery<AuditDataBean > query = em.createQuery(
                "SELECT record FROM beans.AuditDataBean   record " +
                        "WHERE record.logId = :logId",
                AuditDataBean .class);

        query.setParameter("logId", logId);
        foundRecord = query.getSingleResult();
        return foundRecord;
    }


    public List<AuditDataBean > findLogEntry(String userId, String fromTime, String toTime) throws NoResultException {
        TypedQuery<AuditDataBean > query = em.createQuery(
                "SELECT records FROM beans.AuditDataBean   record " +
                        "WHERE record.userId = :userId " +
                        "AND record.timestamp > :minTime " +
                        "AND record.timestamp < :maxTime ",
                AuditDataBean .class);
        query.setParameter("userId", userId);
        query.setParameter("minTime", fromTime);
        query.setParameter("maxTime", toTime);

        return query.getResultList();
    }

    public List<AuditDataBean > getAllLogEntries() throws NoResultException {
        // TODO add limits
        TypedQuery<AuditDataBean > query = em.createQuery(
                "SELECT records from beans.AuditDataBean  ",
                AuditDataBean .class);

        return query.getResultList();
    }

    public void removeLogData(String logId) throws NoResultException {
        AuditDataBean  foundEntry = getLogEntry(logId);
        em.remove(foundEntry);
    }
}
