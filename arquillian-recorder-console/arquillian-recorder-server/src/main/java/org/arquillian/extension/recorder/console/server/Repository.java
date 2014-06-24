package org.arquillian.extension.recorder.console.server;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import org.arquillian.recorder.reporter.model.Report;

@ApplicationScoped
public class Repository {

    private Map<String, Report> repository = new HashMap<String, Report>();
    
    public void put(String id, Report report) {
        repository.put(id, report);
    }
    
    public Collection<Report> all() {
        return repository.values();
    }
}
