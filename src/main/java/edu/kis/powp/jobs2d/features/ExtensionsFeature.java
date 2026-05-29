package edu.kis.powp.jobs2d.features;

import edu.kis.powp.appbase.Application;
import edu.kis.powp.jobs2d.drivers.visitor.VisitableDriver;
import edu.kis.powp.jobs2d.events.SelectToggleExtensionOptionListener;
import edu.kis.powp.jobs2d.drivers.DriverManager;

public class ExtensionsFeature implements IFeature {

    private static Application app;

    @Override
    public void setup(Application application) {
        app = application;
        app.addComponentMenu(ExtensionsFeature.class, "Extensions");
    }

    @Override
    public String getName() {
        return "Extensions";
    }

    public static Application getApp() {
        return app;
    }


    /**
     * Add extension driver to context, create toggle checkbox button in "Extensions" menu.
     * Extension can be enabled or disabled at runtime by the user.
     *
     * @param name      Button name displayed in the menu.
     * @param key       Unique key identifying the extension in the driver manager.
     * @param extension VisitableDriver extension object.
     * @param driverManager DriverManager used to register and toggle the extension.
     */
    public static void addExtension(String name, String key, VisitableDriver extension, DriverManager driverManager) {
        if (app == null) {
            throw new IllegalStateException(
                    "Application is not initialized. Ensure ExtensionsFeature is registered before adding extensions."
            );
        }
        SelectToggleExtensionOptionListener listener = new SelectToggleExtensionOptionListener(
                driverManager,
                key,
                extension,
                false
        );
        app.addComponentMenuElementWithCheckBox(ExtensionsFeature.class, name, listener, false);
    }
}