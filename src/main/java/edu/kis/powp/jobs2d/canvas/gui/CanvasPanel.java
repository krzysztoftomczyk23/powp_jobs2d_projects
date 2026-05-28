package edu.kis.powp.jobs2d.canvas.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import edu.kis.powp.jobs2d.drivers.visitor.VisitableDriver;
import edu.kis.powp.jobs2d.canvas.ICanvas;
import edu.kis.powp.jobs2d.command.DriverCommand;

/**
 * Swing panel that previews the outline of a selected canvas together with the
 * currently loaded driver command.
 *
 * <p>Both the canvas outline and the command are rendered through a
 * {@link Job2dDriver} ({@link Graphics2DDriver}) by executing the corresponding
 * commands - the same abstraction the rest of the application uses to draw on a
 * real device - rather than issuing {@code Graphics2D} calls directly. The
 * canvas and the command can be set independently; each setter triggers a
 * repaint.</p>
 */
public class CanvasPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final Color CANVAS_OUTLINE_COLOR = Color.GRAY;
    private static final Color COMMAND_COLOR = Color.BLUE;

    private transient ICanvas canvas;
    private transient DriverCommand command;

    public CanvasPanel() {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(400, 500));
    }

    public void setCanvas(ICanvas canvas) {
        this.canvas = canvas;
        repaint();
    }

    public void setCommand(DriverCommand command) {
        this.command = command;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics gOriginal) {
        super.paintComponent(gOriginal);
        Graphics2D g = (Graphics2D) gOriginal.create();
        try {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Canvas coordinates are centred around (0,0); move the origin to the
            // centre of the panel so positive coordinates go right/down.
            g.translate(getWidth() / 2, getHeight() / 2);

            paintCanvasOutline(g);
            paintCommand(g);
        } finally {
            g.dispose();
        }
    }

    private void paintCanvasOutline(Graphics2D g) {
        if (canvas == null) {
            g.setColor(Color.LIGHT_GRAY);
            g.drawString("No canvas selected", -50, -10);
            return;
        }
        VisitableDriver driver = new Graphics2DDriver(g, null, CANVAS_OUTLINE_COLOR);
        canvas.toCommand().execute(driver);
    }

    private void paintCommand(Graphics2D g) {
        if (command == null) {
            g.setColor(Color.LIGHT_GRAY);
            g.drawString("No command loaded", -50, 10);
            return;
        }
        VisitableDriver driver = new Graphics2DDriver(g, canvas, COMMAND_COLOR);
        command.execute(driver);
    }
}
