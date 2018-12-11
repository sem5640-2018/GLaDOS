package beans;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Named
@Stateless
public class StatusBacking {


    private List<Map.Entry<String, Boolean>> serviceStatus;

    @EJB
    StatusSingleton statusGetter;

    @PostConstruct
    void init(){
        serviceStatus = new ArrayList<>(statusGetter.getServiceStatuses().entrySet());
        serviceStatus.sort(Map.Entry.comparingByKey());
    }

    public List<Map.Entry<String, Boolean>> getServiceStatus() {
        return serviceStatus;
    }
}
