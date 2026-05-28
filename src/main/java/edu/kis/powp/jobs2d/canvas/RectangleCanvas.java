package edu.kis.powp.jobs2d.canvas;

import java.awt.Point;

import edu.kis.powp.jobs2d.command.ICompoundCommand;
import edu.kis.powp.jobs2d.command.ShapeCommandFactory;

/**
 * Rectangular canvas with optional margin. The drawable area is a rectangle
 * centred at the origin with the specified width and height, shrunk on all
 * sides by the margin.
 *
 * Coordinates are treated as in screen-style (origin at top-left of the
 * drawable area: 0..width on X, 0..height on Y).
 */
public class RectangleCanvas implements ICanvas {

    private final int width;
    private final int height;
    private final int margin;
    private final String name;

    /**
     * @param name   human-readable canvas name (e.g. "A4")
     * @param width  width in pixels (or generic units)
     * @param height height in the same units as width
     * @param margin margin applied on every side, in the same units
     */
    public RectangleCanvas(String name, int width, int height, int margin) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.margin = margin;
    }

    @Override
    public boolean contains(int x, int y) {
        int halfWidth = width / 2;
        int halfHeight = height / 2;

        return x >= -halfWidth + margin
                && x <= halfWidth - margin
                && y >= -halfHeight + margin
                && y <= halfHeight - margin;
    }

    @Override
    public Point clampToBounds(int x, int y) {
        int halfWidth = width / 2;
        int halfHeight = height / 2;
        int minX = -halfWidth + margin;
        int maxX = halfWidth - margin;
        int minY = -halfHeight + margin;
        int maxY = halfHeight - margin;
        int cx = Math.max(minX, Math.min(maxX, x));
        int cy = Math.max(minY, Math.min(maxY, y));
        return new Point(cx, cy);
    }

    @Override
    public ICompoundCommand toCommand() {
        return ShapeCommandFactory.fromRectangle(width, height, margin);
    }

    @Override
    public String getName() {
        return name;
    }
}
