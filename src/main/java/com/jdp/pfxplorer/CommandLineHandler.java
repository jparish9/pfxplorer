package com.jdp.pfxplorer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Handle command-line invocations with parameters.
 */
@Component
public class CommandLineHandler implements CommandLineRunner {

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * Run will be invoked automatically on startup.
     * Delegate to the appropriate handler based on the first argument.
     * Do nothing (normal startup) if no arguments supplied.
     **/
    @Override
    public void run(String... args) throws Exception {
        if (args.length == 0) {
            return;
        }
        if (args[0].equals("loadpfx")) {
            ((ImportPFX)applicationContext.getBean("importPFX")).run(args);
            return;
        }
        if (args[0].equals("loadpitchers")) {
            ((ImportPlayerMap)applicationContext.getBean("importPlayerMap")).run(args);
            return;
        }
    }
}
