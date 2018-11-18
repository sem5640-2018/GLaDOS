package uk.ac.aber.dcs.aberfitness.glados.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

public class DatabaseConnection implements IDatabaseConnection{
    @PersistenceContext
    private EntityManager em;

    private static Logger log = LogManager.getLogger(DatabaseConnection.class.getName());

    public DatabaseConnection(EntityManager em){
        this.em = em;
    }


    @Override
    public void addLogData(AuditData newLogEntry) throws IOException {
        try {
            em.getTransaction().begin();
            em.persist(newLogEntry);
            em.getTransaction().commit();
        } catch (Exception e) {
            log.catching(e);
            e.printStackTrace();
            throw new IOException(e.getMessage());
        }

    }

    @Override
    public AuditData getLogEntry(String logId) throws IOException, NoSuchElementException {
        return null;
    }

    @Override
    public List<AuditData> findLogEntry(AuditData searchCriteria) throws IOException {
        return null;
    }

    @Override
    public List<AuditData> getAllLogEntries() throws IOException {
        return null;
    }

    @Override
    public void removeLogData(String logId) throws IOException {

    }
}
