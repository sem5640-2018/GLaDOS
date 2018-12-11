package beans;

import beans.helpers.ServiceNames;
import configuration.EnvironmentVariables;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Startup
@Singleton
public class StatusSingleton {

    private static final Logger log = LogManager.getLogger(StatusSingleton.class);

    private CompletableFuture asyncQuery;
    private Map<String, Boolean> serviceStatuses = new HashMap<>();

    public StatusSingleton() {
        for (ServiceNames name : ServiceNames.values()) {
            String serviceName = name.toString().replace('_', '-').toLowerCase();
            serviceStatuses.put(serviceName, false);
        }
    }

    @PostConstruct
    public void init(){
        asyncQuery = CompletableFuture.runAsync(this::asyncQueryServices);
    }

    public Map<String, Boolean> getServiceStatuses() {
        return serviceStatuses;
    }

    // Query services every minute in the background
    @Schedule(hour = "*", minute = "*", second = "*/20", persistent = false)
    @SuppressWarnings("PMD.UnusedPrivateMethod")
    private void checkQueriesCompleted() {
        if (asyncQuery == null){
            return;
        }

        try {
            asyncQuery.get(1, TimeUnit.SECONDS);
        } catch (InterruptedException | TimeoutException | ExecutionException e) {
            log.warn("Cancelling missed future");
        }

        if (!asyncQuery.isDone()) {
            log.warn("Cancelling missed future");
            asyncQuery.cancel(true);
        }

        asyncQuery = CompletableFuture.runAsync(this::asyncQueryServices);
    }

    private void asyncQueryServices() {
        for (String names : serviceStatuses.keySet()) {
            queryIndividualServiceWrapper(names);
        }
    }

    private void queryIndividualServiceWrapper(String serviceToQuery) {
        try {
            queryIndividualService(serviceToQuery);
        } catch (Exception e) {
            log.warn("Exception querying service: " + e.getMessage());
            serviceStatuses.put(serviceToQuery, false);
        }
    }

    private void queryIndividualService(String serviceToQuery) throws IOException {
        URL url = new URL(EnvironmentVariables.getSystemBaseUrl() + '/' + serviceToQuery + "/api/status");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        connection.setConnectTimeout(500);
        connection.setReadTimeout(500);

        int responseCode = connection.getResponseCode();

        if (responseCode == 200 || responseCode == 204) {
            // Service is up
            serviceStatuses.put(serviceToQuery, true);
            return;
        }

        serviceStatuses.put(serviceToQuery, false);
    }
}
