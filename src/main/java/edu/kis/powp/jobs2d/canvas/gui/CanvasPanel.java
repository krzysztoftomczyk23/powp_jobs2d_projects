package edu.kis.powp.jobs2d.canvas.gui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

import edu.kis.legacy.drawer.panel.DrawPanelController;
import edu.kis.legacy.drawer.shape.ILine;
import edu.kis.legacy.drawer.shape.LineFactory;

import edu.kis.powp.jobs2d.canvas.ICanvas;
import edu.kis.powp.jobs2d.command.DriverCommand;
import edu.kis.powp.jobs2d.drivers.adapter.LineDriverAdapter;

/**
 * Swing panel that previews the outline of a canvas together with the currently
 * loaded driver command.
 *
 * <p>The panel does not draw with {@code Graphics2D} directly. Instead it owns a
 * {@link DrawPanelController} - the same drawing abstraction the rest of the
 * application uses - and renders by executing the relevant commands on a
 * {@link LineDriverAdapter}: {@code canvas.toCommand().execute(adapter)} for the
 * outline and {@code command.execute(adapter)} for the loaded command. The
 * canvas and the command can be set independently; each setter re-renders.</p>
 */
public class CanvasPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private final transient DrawPanelController drawPanelController = new DrawPanelController();
    private final transient ILine outlineLine = LineFactory.getSpecialLine();
    private final transient ILine commandLine = LineFactory.getBasicLine();

    private transient ICanvas canvas;
    private transient DriverCommand command;

    public CanvasPanel() {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(400, 500));
        drawPanelController.initialize(this);
    }

    public void setCanvas(ICanvas canvas) {
        this.canvas = canvas;
        render();
    }

    public void setCommand(DriverCommand command) {
        this.command = command;
        render();
    }

    /**
     * Clears the panel and redraws the canvas outline and the loaded command
     * using the application's drawing abstraction.
     */
    private void render() {
        drawPanelController.clearPanel();

        if (canvas != null) {
            canvas.toCommand().execute(
                    new LineDriverAdapter(drawPanelController, outlineLine, "Canvas outline"));
        }

        if (command != null) {
            command.execute(
                    new LineDriverAdapter(drawPanelController, commandLine, "Command preview"));
        }
    }
}
