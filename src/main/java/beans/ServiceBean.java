package beans;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Dependent
@Stateless
@Path("/status")
@Produces(MediaType.APPLICATION_JSON)
public class ServiceBean {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStatus(){
        return Response.noContent().build();
    }
}
