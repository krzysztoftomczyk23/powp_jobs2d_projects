package edu.kis.powp.jobs2d.features;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.kis.legacy.drawer.shape.ILine;
import edu.kis.legacy.drawer.shape.LineFactory;
import edu.kis.powp.appbase.Application;
import edu.kis.powp.jobs2d.canvas.CircleCanvas;
import edu.kis.powp.jobs2d.canvas.ICanvas;
import edu.kis.powp.jobs2d.canvas.PaperFormat;
import edu.kis.powp.jobs2d.drivers.adapter.LineDriverAdapter;
import edu.kis.powp.observer.Publisher;
import edu.kis.powp.observer.Subscriber;

public class CanvasFeature implements IFeature {
    private static ICanvas currentFormat;
    private static ILine guidesLineType = LineFactory.getSpecialLine();

    private static final List<ICanvas> availableCanvases = new ArrayList<>();

    /**
     * Notifies subscribers whenever the current canvas changes, so that other
     * components (e.g. the command preview) can stay consistent with
     * {@link #getCanvas()} - the single source of truth for the current canvas.
     */
    private static final Publisher canvasChangePublisher = new Publisher();

    static {
        for (PaperFormat format : PaperFormat.values()) {
            registerCanvas(format);
        }
        registerCanvas(new CircleCanvas("Circle r=200", 0, 0, 200, 20));
    }

    @Override
    public void setup(Application application) {
        setupCanvasPlugin(application);
    }

    @Override
    public String getName() {
        return "Canvas";
    }

    public static void setupCanvasPlugin(Application application) {
        application.addComponentMenu(CanvasFeature.class, "Canvas", 0);

        for (ICanvas canvas : availableCanvases) {
            application.addComponentMenuElement(CanvasFeature.class, canvas.getName(), event -> setCanvas(canvas));
        }
    }

    /**
     * Register an additional canvas so it becomes available throughout the
     * application (menu, previews, ...). This is the OCP extension point:
     * supporting a new canvas type requires only a {@code registerCanvas} call,
     * no changes to the GUI components that consume {@link #getAvailableCanvases()}.
     *
     * @param canvas canvas to register
     */
    public static void registerCanvas(ICanvas canvas) {
        if (canvas != null && !availableCanvases.contains(canvas)) {
            availableCanvases.add(canvas);
        }
    }

    /**
     * @return immutable view of all canvases known to the application
     */
    public static List<ICanvas> getAvailableCanvases() {
        return Collections.unmodifiableList(availableCanvases);
    }

    public static void clearPanel() {
        DrawerFeature.getDrawerController().clearPanel();
        redrawCanvas(currentFormat, true);
    }

    public static void setCanvas(ICanvas format) {
        redrawCanvas(format, false);
    }

    public static ICanvas getCanvas() {
        return currentFormat;
    }

    /**
     * Subscribe to canvas changes. Subscribers are notified whenever
     * {@link #setCanvas(ICanvas)} changes the current canvas, allowing them to
     * read the new value from {@link #getCanvas()}.
     *
     * @param subscriber subscriber to notify on canvas change
     */
    public static void subscribeToCanvasChange(Subscriber subscriber) {
        canvasChangePublisher.addSubscriber(subscriber);
    }

    public static void setGuidesLineType(ILine lineType) {
        guidesLineType = lineType;
    }

    public static ILine getGuidesLineType() {
        return guidesLineType;
    }

    private static void redrawCanvas(ICanvas format, boolean forceRedraw) {
        if (!forceRedraw && format == currentFormat) {
            return;
        }

        format.toCommand().execute(new LineDriverAdapter(DrawerFeature.getDrawerController(), guidesLineType, "Canvas Guides"));

        currentFormat = format;
        canvasChangePublisher.notifyObservers();
    }
}
