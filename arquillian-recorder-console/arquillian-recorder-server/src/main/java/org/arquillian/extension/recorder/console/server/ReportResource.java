package org.arquillian.extension.recorder.console.server;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.arquillian.recorder.reporter.model.Report;

import com.google.gson.Gson;

@Path("report")
@RequestScoped
public class ReportResource {

    @Inject
    private Repository repository;
    
    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response all() {
        return Response.ok(new Gson().toJson(repository.all())).build();
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_XML)
    public Response add(Report report) {
        repository.put(report.getId(), report);
        return Response.ok().build();
    }
}