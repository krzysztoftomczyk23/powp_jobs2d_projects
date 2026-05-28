package edu.kis.powp.jobs2d.drivers.bounds;

import java.awt.Point;

public interface MissingCanvasStrategy {

    /**
     * Decides which coordinates should be passed to the wrapped driver when no canvas is selected.
     *
     * @param x original x coordinate requested by the caller
     * @param y original y coordinate requested by the caller
     * @param driverName display name of the clamping driver for diagnostics
     * @return coordinates that should be forwarded to the wrapped driver
     */
    Point resolveCoordinatesWithoutCanvas(int x, int y, String driverName);

    /**
     * Notification hook called when a canvas becomes available again.
     * Implementations can reset one-time warning flags or temporary state here.
     */
    default void onCanvasAvailable(String driverName) {
    }
}
