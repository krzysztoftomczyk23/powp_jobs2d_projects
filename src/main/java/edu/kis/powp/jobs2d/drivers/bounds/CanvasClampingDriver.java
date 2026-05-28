package edu.kis.powp.jobs2d.drivers.bounds;

import java.awt.Point;
import java.util.Objects;
import java.util.function.Supplier;

import edu.kis.powp.jobs2d.canvas.ICanvas;
import edu.kis.powp.jobs2d.drivers.visitor.DriverVisitor;
import edu.kis.powp.jobs2d.drivers.visitor.VisitableDriver;

public class CanvasClampingDriver implements VisitableDriver {

    private final VisitableDriver innerDriver;
    private final Supplier<ICanvas> canvasSupplier;
    private final MissingCanvasStrategy missingCanvasStrategy;
    private final String name;

    public CanvasClampingDriver(VisitableDriver innerDriver, Supplier<ICanvas> canvasSupplier, String name) {
        this(innerDriver, canvasSupplier, name, new LoggingPassThroughMissingCanvasStrategy());
    }

    public CanvasClampingDriver(VisitableDriver innerDriver, Supplier<ICanvas> canvasSupplier, String name,
            MissingCanvasStrategy missingCanvasStrategy) {
        this.innerDriver = Objects.requireNonNull(innerDriver, "innerDriver");
        this.canvasSupplier = Objects.requireNonNull(canvasSupplier, "canvasSupplier");
        this.name = Objects.requireNonNull(name, "name");
        this.missingCanvasStrategy = Objects.requireNonNull(missingCanvasStrategy, "missingCanvasStrategy");
    }

    @Override
    public void setPosition(int x, int y) {
        Point p = clamp(x, y);
        innerDriver.setPosition(p.x, p.y);
    }

    @Override
    public void operateTo(int x, int y) {
        Point p = clamp(x, y);
        innerDriver.operateTo(p.x, p.y);
    }

    private Point clamp(int x, int y) {
        ICanvas canvas = canvasSupplier.get();
        if (canvas == null) {
            return missingCanvasStrategy.resolveCoordinatesWithoutCanvas(x, y, name);
        }
        missingCanvasStrategy.onCanvasAvailable(name);
        return canvas.clampToBounds(x, y);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public void accept(DriverVisitor visitor) {
        visitor.visit(this);
    }

    public VisitableDriver getInnerDriver() {
        return innerDriver;
    }
}
