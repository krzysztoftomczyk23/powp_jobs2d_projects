package edu.kis.powp.jobs2d.canvas;

import java.awt.Point;

import edu.kis.powp.jobs2d.command.ICompoundCommand;

public enum PaperFormat implements ICanvas {
    A4(new RectangleCanvas("A4", 210, 297, 0)),
    B3(new RectangleCanvas("B3", 353, 500, 0));

    private final RectangleCanvas canvas;

    PaperFormat(RectangleCanvas canvas) {
        this.canvas = canvas;
    }

    @Override
    public boolean contains(int x, int y) {
        return canvas.contains(x, y);
    }

    @Override
    public Point clampToBounds(int x, int y) {
        return canvas.clampToBounds(x, y);
    }

    @Override
    public ICompoundCommand toCommand() {
        return canvas.toCommand();
    }

    @Override
    public String getName() {
        return "Paper Format Canvas (" + name() + ")";
    }
}
