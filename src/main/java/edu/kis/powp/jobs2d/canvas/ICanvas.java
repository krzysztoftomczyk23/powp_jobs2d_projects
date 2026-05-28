package edu.kis.powp.jobs2d.canvas;

import java.awt.Point;

import edu.kis.powp.jobs2d.command.ICompoundCommand;

/**
 * Represents a drawing area (canvas) with optional margin.
 * Implementations define the geometry of the drawable region.
 */
public interface ICanvas {

    /**
     * Check whether the given point lies within the drawable area
     * (i.e. inside the canvas and not within the margin).
     *
     * @param x x-coordinate
     * @param y y-coordinate
     * @return true if the point is within bounds, false if it exceeds them
     */
    boolean contains(int x, int y);

    /**
     * Returns the nearest point that lies in the drawable area (same predicate as
     * {@link #contains(int, int)}). If the input is already inside, it is returned
     * unchanged. Used by drivers that must not send the device outside the canvas.
     *
     * @param x requested x-coordinate
     * @param y requested y-coordinate
     * @return point with clamped coordinates
     */
    Point clampToBounds(int x, int y);

    /**
     * @return CompoundCommand which draws the canvas guides
     */
    ICompoundCommand toCommand();

    /**
     * @return human-readable name of this canvas (e.g. "A4", "B3", "Circle r=200")
     */
    String getName();
}
