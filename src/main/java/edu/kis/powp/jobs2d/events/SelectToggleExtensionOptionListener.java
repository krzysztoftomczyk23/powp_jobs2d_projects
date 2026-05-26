package edu.kis.powp.jobs2d.events;

import edu.kis.powp.jobs2d.drivers.DriverManager;
import edu.kis.powp.jobs2d.drivers.visitor.VisitableDriver;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Toggles a named driver extension on/off inside {@link DriverManager}.
 */
public class SelectToggleExtensionOptionListener implements ActionListener {

    private final DriverManager driverManager;
    private final String extensionName;
    private final VisitableDriver extension;

    /**
     * @param driverManager   The manager that owns the extension registry.
     * @param extensionName   Stable key used to add/remove the extension.
     * @param extension       The extension driver instance to toggle.
     * @param initiallyActive If {@code true} the extension is registered immediately.
     */
    public SelectToggleExtensionOptionListener(DriverManager driverManager, String extensionName, VisitableDriver extension, boolean initiallyActive) {
        this.driverManager = driverManager;
        this.extensionName = extensionName;
        this.extension = extension;

        if (initiallyActive) {
            driverManager.addExtension(extensionName, extension);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (driverManager.hasExtension(extensionName)) {
            driverManager.removeExtension(extensionName);
        } else {
            driverManager.addExtension(extensionName, extension);
        }
    }
}