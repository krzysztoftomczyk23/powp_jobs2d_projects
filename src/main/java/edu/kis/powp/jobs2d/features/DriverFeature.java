package edu.kis.powp.jobs2d.features;

import edu.kis.powp.appbase.Application;

import edu.kis.powp.jobs2d.drivers.CurrentDriverInfoObserver;
import edu.kis.powp.jobs2d.drivers.DriverManager;
import edu.kis.powp.jobs2d.drivers.SelectDriverMenuOptionListener;
import edu.kis.powp.jobs2d.drivers.visitor.VisitableDriver;
import edu.kis.powp.jobs2d.events.SelectToggleExtensionOptionListener;

public class DriverFeature implements IFeature {

    private static DriverManager driverManager = new DriverManager();
    private static Application app;

    @Override
    public void setup(Application application) {
        setupDriverPlugin(application);
    }

    @Override
    public String getName() {
        return "Driver";
    }

    public static DriverManager getDriverManager() {
        return driverManager;
    }

    /**
     * Setup jobs2d drivers Plugin and add to application.
     *
     * @param application Application context.
     */
    public static void setupDriverPlugin(Application application) {
        app = application;
        app.addComponentMenu(DriverFeature.class, "Drivers");
        CurrentDriverInfoObserver currentDriverInfoObserver = new CurrentDriverInfoObserver();
        driverManager.getChangePublisher().addSubscriber(currentDriverInfoObserver);
        updateDriverInfo();
    }

    /**
     * Add driver to context, create button in driver menu.
     *
     * @param name   Button name.
     * @param driver VisitableDriver object.
     */
    public static void addDriver(String name, VisitableDriver driver) {
        SelectDriverMenuOptionListener listener = new SelectDriverMenuOptionListener(driver, driverManager);
        app.addComponentMenuElement(DriverFeature.class, name, listener);
    }

    /**
     * Update driver info.
     */
    public static void updateDriverInfo() {
        app.updateInfo(driverManager.getCurrentDriver().toString());
    }


    /**
     * Add extension driver to context, create toggle checkbox button in driver menu.
     * Extension can be enabled or disabled at runtime by the user.
     *
     * @param name      Button name displayed in the menu.
     * @param key       Unique key identifying the extension in the driver manager.
     * @param extension VisitableDriver extension object.
     */
    public static void addExtension(String name, String key, VisitableDriver extension) {
        SelectToggleExtensionOptionListener listener = new SelectToggleExtensionOptionListener(
                driverManager,
                key,
                extension,
                false
        );
        app.addComponentMenuElementWithCheckBox(DriverFeature.class, name, listener, false);
    }

}
