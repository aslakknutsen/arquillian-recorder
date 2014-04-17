package org.arquillian.extension.recorder.console.client;

import org.jboss.arquillian.core.spi.LoadableExtension;

public class ConsoleClientExtension implements LoadableExtension {

    @Override
    public void register(ExtensionBuilder builder) {
        builder.observer(ConsoleReporter.class);
    }

}