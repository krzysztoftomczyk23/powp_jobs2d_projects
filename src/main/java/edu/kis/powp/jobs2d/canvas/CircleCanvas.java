package edu.kis.powp.jobs2d.canvas;

import java.awt.Point;

import edu.kis.powp.jobs2d.command.ICompoundCommand;
import edu.kis.powp.jobs2d.command.ShapeCommandFactory;

/**
 * Circular canvas - example of a non-rectangular ("custom shape") canvas.
 * The drawable area is the disc of radius (radius - margin) centred at
 * (centerX, centerY).
 */
public class CircleCanvas implements ICanvas {

    private final int centerX;
    private final int centerY;
    private final int radius;
    private final int margin;
    private final String name;

    /**
     * @param name    human-readable canvas name
     * @param centerX x-coordinate of the centre
     * @param centerY y-coordinate of the centre
     * @param radius  radius in the same units as the coordinates
     * @param margin  margin reducing the effective drawable radius
     */
    public CircleCanvas(String name, int centerX, int centerY, int radius, int margin) {
        this.name = name;
        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = radius;
        this.margin = margin;
    }

    @Override
    public boolean contains(int x, int y) {
        int effectiveRadius = radius - margin;
        long dx = x - centerX;
        long dy = y - centerY;
        return dx * dx + dy * dy <= (long) effectiveRadius * effectiveRadius;
    }

    @Override
    public Point clampToBounds(int x, int y) {
        if (contains(x, y)) {
            return new Point(x, y);
        }
        int effectiveRadius = radius - margin;
        if (effectiveRadius <= 0) {
            return new Point(centerX, centerY);
        }
        double dx = x - centerX;
        double dy = y - centerY;
        double dist = Math.hypot(dx, dy);
        if (dist < 1e-9) {
            return new Point(centerX + effectiveRadius, centerY);
        }
        double scale = effectiveRadius / dist;
        return new Point(
                (int) Math.round(centerX + dx * scale),
                (int) Math.round(centerY + dy * scale)
        );
    }

    @Override
    public ICompoundCommand toCommand() {
        return ShapeCommandFactory.fromCircle(centerX, centerY, radius, margin);
    }

    @Override
    public String getName() {
        return name;
    }
}
