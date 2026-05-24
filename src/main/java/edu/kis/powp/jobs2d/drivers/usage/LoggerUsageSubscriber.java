package edu.kis.powp.jobs2d.drivers.usage;

import edu.kis.powp.observer.Subscriber;
import java.util.logging.Logger;

public class LoggerUsageSubscriber implements Subscriber {
    private static final Logger logger = Logger.getLogger("global");

    private final IUsageMonitor monitor;

    public LoggerUsageSubscriber(IUsageMonitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public void update() {
        logger.info("head distance: " + (int)Math.round(monitor.getHeadDistance()) + " units");
        logger.info("op. distance: " + (int)Math.round(monitor.getOperatingDistance()) + " units");
    }
}