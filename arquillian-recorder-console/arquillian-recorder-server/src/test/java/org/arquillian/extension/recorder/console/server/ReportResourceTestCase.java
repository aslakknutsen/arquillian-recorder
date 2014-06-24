package org.arquillian.extension.recorder.console.server;


import static com.jayway.restassured.RestAssured.given;

import java.io.File;
import java.net.URL;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.arquillian.recorder.reporter.model.Report;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.filter.log.RequestLoggingFilter;
import com.jayway.restassured.filter.log.ResponseLoggingFilter;

@RunWith(Arquillian.class)
public class ReportResourceTestCase {

    @Deployment(testable = false)
    public static WebArchive deploy() {
        return ShrinkWrap.create(WebArchive.class)
                .addPackage(ReportResource.class.getPackage())
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsLibraries(
                    Maven.resolver()
                        .loadPomFromFile(new File("pom.xml"))
                        .resolve(
                                "org.arquillian.extension:arquillian-recorder-reporter-api",
                                "com.google.code.gson:gson")
                        .withTransitivity()
                        .asFile()
                );
    }
    
    @ArquillianResource
    private URL baseURL;
    
    @BeforeClass
    public static void setup() {
        RestAssured.filters(
                ResponseLoggingFilter.responseLogger(),
                new RequestLoggingFilter());
    }

    @Test @InSequence(0)
    public void shouldBeAbleToPOST() throws Exception {
        Report report = new Report();
        
        given().
            contentType(MediaType.APPLICATION_XML).
            content(report).
        then().
            statusCode(Response.Status.OK.getStatusCode()).
        when().
            post(new URL(baseURL, "console/report/"));
    }

    @Test @InSequence(1)
    public void shouldBeAbleToGET() throws Exception {
        given().
        then().
            contentType(MediaType.APPLICATION_JSON).
            statusCode(Response.Status.OK.getStatusCode()).
        when().
            get(new URL(baseURL, "console/report/all"));
    }
}
