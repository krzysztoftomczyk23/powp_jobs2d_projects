package edu.kis.powp.jobs2d.drivers.bounds;

import java.awt.Point;
import java.util.logging.Logger;

public class LoggingPassThroughMissingCanvasStrategy implements MissingCanvasStrategy {

    private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private boolean loggedMissingCanvas;

    @Override
    public Point resolveCoordinatesWithoutCanvas(int x, int y, String driverName) {
        if (!loggedMissingCanvas) {
            logger.info("CanvasClampingDriver (\"" + driverName + "\"): no canvas format selected - "
                    + "coordinates pass through without clamping. Choose a format in the Canvas menu.");
            loggedMissingCanvas = true;
        }
        return new Point(x, y);
    }

    @Override
    public void onCanvasAvailable(String driverName) {
        loggedMissingCanvas = false;
    }
}
