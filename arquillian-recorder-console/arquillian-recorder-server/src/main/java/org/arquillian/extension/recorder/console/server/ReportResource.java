package org.arquillian.extension.recorder.console.server;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.arquillian.recorder.reporter.model.Report;

@Path("report")
public class ReportResource {

    private Map<String, Report> repository = new HashMap<String, Report>();
    
    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Report> all() {
        return repository.values();
    }
    
    @POST
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_XML)
    public void add(@PathParam("id") String id, Report report) {
        repository.put(id, report);
    }
}
