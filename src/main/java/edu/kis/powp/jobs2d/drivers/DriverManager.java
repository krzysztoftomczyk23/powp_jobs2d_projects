package edu.kis.powp.jobs2d.drivers;


import edu.kis.powp.jobs2d.drivers.packet_composite.CompositeDriver;
import edu.kis.powp.jobs2d.drivers.visitor.VisitableDriver;
import edu.kis.powp.observer.Publisher;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;


/**
 * Driver manager provides means to setup the driver. It also enables other
 * components and features of the application to react on configuration changes.
 */
public class DriverManager {

    private VisitableDriver baseDriver;
    private final Map<String, VisitableDriver> extensions = new LinkedHashMap<>();
    private Publisher changePublisher = new Publisher();

    /**
     * @param driver Set the driver as current.
     */
    public synchronized void setCurrentDriver(VisitableDriver driver) {
        this.baseDriver = driver;
        changePublisher.notifyObservers();
    }

    /**
     * Adds the given extension to extensions list.
     */
    public synchronized void addExtension(String name, VisitableDriver extension) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Extension name must not be blank.");
        }
        if (extension == null) {
            throw new IllegalArgumentException("Extension driver must not be null.");
        }
        extensions.put(name, extension);
        changePublisher.notifyObservers();
    }


    /**
     * Removes the extension registered under {@code name}. Does nothing if no
     * extension with that name exists.
     */
    public synchronized void removeExtension(String name) {
        if (extensions.remove(name) != null) {
            changePublisher.notifyObservers();
        }
    }


    public synchronized boolean hasExtension(String name) {
        return extensions.containsKey(name);
    }


    /**
     * @return Current driver as composite of base driver and all extension drivers.
     */
    public synchronized VisitableDriver getCurrentDriver() {
        if (baseDriver == null && extensions.isEmpty()) {
            return new CompositeDriver("Empty driver");
        }

        CompositeDriver composite = new CompositeDriver("Active Driver + Extensions");

        if (baseDriver != null) {
            composite.getDrivers().add(baseDriver);
        }

        composite.getDrivers().addAll(extensions.values());

        return composite;
    }


    /**
     * @return changePublisher.
     */
    public Publisher getChangePublisher() {
        return changePublisher;
    }
}
