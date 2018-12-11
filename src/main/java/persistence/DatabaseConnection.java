package persistence;

import entities.AuditData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ejb.Stateless;
import javax.persistence.*;
import java.io.IOException;
import java.time.Instant;
import java.util.List;

@Stateless
public class DatabaseConnection {
    @PersistenceContext(unitName = "gladosPU")
    private EntityManager em;

    private final static Logger log = LogManager.getLogger(DatabaseConnection.class.getName());

    public DatabaseConnection() {
    }

    public void addLogData(AuditData newLogEntry) throws IOException {
        try {
            em.persist(newLogEntry);
        } catch (Exception e) {
            log.catching(e);
            e.printStackTrace();
            throw new IOException(e.getMessage());
        }

    }

    public AuditData getLogEntry(String logId) throws NoResultException {
        AuditData foundRecord;
        TypedQuery<AuditData> query = em.createNamedQuery("AuditData.getLogEntry", AuditData.class);
        query.setParameter("logId", logId);
        foundRecord = query.getSingleResult();
        return foundRecord;
    }


    public List<AuditData> findLogEntry(String userId, Instant fromTime, Instant toTime) throws NoResultException {
        TypedQuery<AuditData> query = em.createNamedQuery("findLogEntry", AuditData.class);
        query.setParameter("userId", userId);
        query.setParameter("minTime", fromTime.toString());
        query.setParameter("maxTime", toTime.toString());

        return query.getResultList();
    }

    public List<AuditData> getAllLogEntries() throws NoResultException {
        // TODO add limits
        TypedQuery<AuditData> query = em.createNamedQuery("getAllLogEntries", AuditData.class);
        return query.getResultList();
    }

    public void removeLogData(String logId) throws NoResultException {
        AuditData foundEntry = getLogEntry(logId);
        em.remove(foundEntry);
    }
}
