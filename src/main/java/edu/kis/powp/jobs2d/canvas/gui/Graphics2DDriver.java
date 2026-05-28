package edu.kis.powp.jobs2d.canvas.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import edu.kis.powp.jobs2d.canvas.ICanvas;
import edu.kis.powp.jobs2d.drivers.visitor.DriverVisitor;
import edu.kis.powp.jobs2d.drivers.visitor.VisitableDriver;

/**
 * A {@link VisitableDriver} implementation that renders the head movements onto
 * a {@link Graphics2D} context. It is used solely for the in-window command
 * preview.
 *
 * <p>Because it implements the same driver interface as the real device
 * drivers, any {@code DriverCommand} (including the canvas outline produced by
 * {@link ICanvas#toCommand()}) can be previewed simply by calling
 * {@code command.execute(driver)} - the preview reuses the existing command
 * infrastructure instead of bespoke Graphics2D drawing logic.</p>
 *
 * <p>If a canvas is supplied, segments that leave the drawable area are drawn
 * in {@link #OUT_OF_BOUNDS_COLOR} so the user can see where a command exceeds
 * the canvas.</p>
 */
public class Graphics2DDriver implements VisitableDriver {

    private static final Color DEFAULT_COLOR = Color.BLUE;
    private static final Color OUT_OF_BOUNDS_COLOR = Color.RED;

    private final Graphics2D graphics;
    private final ICanvas canvas;
    private final Color color;

    private int currentX = 0;
    private int currentY = 0;

    public Graphics2DDriver(Graphics2D graphics, ICanvas canvas) {
        this(graphics, canvas, DEFAULT_COLOR);
    }

    public Graphics2DDriver(Graphics2D graphics, ICanvas canvas, Color color) {
        this.graphics = graphics;
        this.canvas = canvas;
        this.color = color;
    }

    @Override
    public void setPosition(int x, int y) {
        this.currentX = x;
        this.currentY = y;
    }

    @Override
    public void operateTo(int x, int y) {
        Color previous = graphics.getColor();
        graphics.setColor(colorForSegment(currentX, currentY, x, y));
        graphics.drawLine(currentX, currentY, x, y);
        graphics.setColor(previous);

        setPosition(x, y);
    }

    private Color colorForSegment(int x1, int y1, int x2, int y2) {
        if (canvas != null && (!canvas.contains(x1, y1) || !canvas.contains(x2, y2))) {
            return OUT_OF_BOUNDS_COLOR;
        }
        return color;
    }

    /**
     * This preview-only driver is never traversed by the driver visitors, so
     * {@code accept} is intentionally a no-op. It exists only to satisfy the
     * {@link VisitableDriver} contract required by
     * {@link edu.kis.powp.jobs2d.command.DriverCommand#execute}.
     */
    @Override
    public void accept(DriverVisitor visitor) {
        // no-op: the preview driver is not part of the visitable driver graph
    }
}
