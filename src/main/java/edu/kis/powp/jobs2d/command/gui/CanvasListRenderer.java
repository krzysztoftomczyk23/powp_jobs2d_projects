package edu.kis.powp.jobs2d.command.gui;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import edu.kis.powp.jobs2d.canvas.ICanvas;

/**
 * List cell renderer that displays an {@link ICanvas} by its human-readable
 * name and renders a {@code null} entry as "None". Used by the canvas selector
 * combo box so the selector can hold {@code ICanvas} values directly instead of
 * mapping strings back to canvases.
 */
class CanvasListRenderer extends DefaultListCellRenderer {

    private static final long serialVersionUID = 1L;

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index,
            boolean isSelected, boolean cellHasFocus) {
        String label = (value instanceof ICanvas) ? ((ICanvas) value).getName() : "None";
        return super.getListCellRendererComponent(list, label, index, isSelected, cellHasFocus);
    }
}
