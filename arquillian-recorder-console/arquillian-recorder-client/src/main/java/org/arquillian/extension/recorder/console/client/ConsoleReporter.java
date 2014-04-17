package org.arquillian.extension.recorder.console.client;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import javax.xml.bind.JAXBContext;

import org.arquillian.recorder.reporter.event.ExportReport;
import org.arquillian.recorder.reporter.model.Report;
import org.jboss.arquillian.config.descriptor.api.ArquillianDescriptor;
import org.jboss.arquillian.config.descriptor.api.ExtensionDef;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.core.api.event.ManagerStopping;
import org.jboss.arquillian.core.api.threading.ExecutorService;

public class ConsoleReporter {

    public static final String CONSOLE_REPORTER_EXT = "reporter";
    public static final String BASE_URL = "baseURL";
    
    @Inject
    private Instance<ExecutorService> executorInst;
    
    private List<Future<Boolean>> published = new ArrayList<Future<Boolean>>();
    
    private String baseURL;
    
    public void configure(@Observes ArquillianDescriptor desc) {
        
        for(ExtensionDef ext : desc.getExtensions()) {
            if(CONSOLE_REPORTER_EXT.equals(ext.getExtensionName())) {
                baseURL = ext.getExtensionProperties().get(BASE_URL);
            }
        }
    }

    public void publishToConsole(@Observes ExportReport exportReport) {
        published.add(executorInst.get().submit(new Publish(baseURL, exportReport)));
    }

    public void stop(@Observes ManagerStopping event) throws Exception {
        for(Future<Boolean> publish : published) {
            Boolean ok = publish.get();
            if(!ok) {
                throw new RuntimeException("Could not publish all results");
            }
        }
    }

    private static class Publish implements Callable<Boolean> {

        private ExportReport exportReport;
        private String baseURL;

        public Publish(String baseURL, ExportReport exportReport) {
            this.baseURL = baseURL;
            this.exportReport = exportReport;
        }

        @Override
        public Boolean call() throws Exception {
            HttpURLConnection con = (HttpURLConnection)new URL(baseURL).openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/xml");
            OutputStream out = con.getOutputStream();
            
            JAXBContext context = JAXBContext.newInstance(Report.class);
            context.createMarshaller().marshal(exportReport.getReport(), out);
            out.flush();
            out.close();
            return con.getResponseCode() == 200;
        }
    }
}