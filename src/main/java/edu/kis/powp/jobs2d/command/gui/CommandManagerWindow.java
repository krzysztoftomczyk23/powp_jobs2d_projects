package edu.kis.powp.jobs2d.command.gui;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

import edu.kis.powp.appbase.gui.WindowComponent;
import edu.kis.powp.jobs2d.canvas.ICanvas;
import edu.kis.powp.jobs2d.canvas.gui.CanvasPanel;
import edu.kis.powp.jobs2d.command.ICompoundCommand;
import edu.kis.powp.jobs2d.command.io.CommandImporter;
import edu.kis.powp.jobs2d.command.io.CommandImporterFactory;
import edu.kis.powp.jobs2d.command.manager.CommandManager;
import edu.kis.powp.jobs2d.features.CanvasFeature;
import edu.kis.powp.observer.Subscriber;

public class CommandManagerWindow extends JFrame implements WindowComponent {

    private static final long serialVersionUID = 9204679248304669948L;

    private CommandManager commandManager;

    private JTextArea currentCommandField;
    private String observerListString;
    private JTextArea observerListField;

    private CanvasPanel canvasPanel;
    private JComboBox<ICanvas> canvasSelector;

    public CommandManagerWindow(CommandManager commandManager) {
        this.setTitle("Command Manager");
        this.setSize(700, 700);
        Container content = this.getContentPane();
        content.setLayout(new GridBagLayout());

        this.commandManager = commandManager;

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.gridx = 0;

        observerListField = new JTextArea("");
        observerListField.setEditable(false);
        c.weighty = 0.2;
        c.gridy = 0;
        content.add(observerListField, c);
        updateObserverListField();

        currentCommandField = new JTextArea("");
        currentCommandField.setEditable(false);
        c.weighty = 0.2;
        c.gridy = 1;
        content.add(currentCommandField, c);
        updateCurrentCommandField();

        c.weighty = 0.0;
        c.gridy = 2;
        content.add(new JLabel("Preview canvas:"), c);

        canvasSelector = new JComboBox<>(buildCanvasModel());
        canvasSelector.setRenderer(new CanvasListRenderer());
        canvasSelector.setSelectedItem(CanvasFeature.getCanvas());
        canvasSelector.addActionListener(e -> onCanvasSelected());
        c.gridy = 3;
        content.add(canvasSelector, c);

        canvasPanel = new CanvasPanel();
        c.weighty = 1.0;
        c.gridy = 4;
        content.add(canvasPanel, c);
        syncCanvasFromFeature();
        updateCanvasPanelCommand();

        // Keep the preview consistent with CanvasFeature even when the canvas is
        // changed elsewhere (e.g. from the application's Canvas menu).
        CanvasFeature.subscribeToCanvasChange(this::syncCanvasFromFeature);

        JButton btnImportCommands = new JButton("Import command");
        btnImportCommands.addActionListener((ActionEvent e) -> this.importCommands());
        c.weighty = 0.0;
        c.gridy = 5;
        content.add(btnImportCommands, c);

        JButton btnClearCommand = new JButton("Clear command");
        btnClearCommand.addActionListener((ActionEvent e) -> this.clearCommand());
        c.gridy = 6;
        content.add(btnClearCommand, c);

        JButton btnClearObservers = new JButton("Delete observers");
        btnClearObservers.addActionListener((ActionEvent e) -> this.deleteObservers());
        c.gridy = 7;
        content.add(btnClearObservers, c);
    }

    /**
     * Builds the canvas-selector model from the canvases registered in
     * {@link CanvasFeature}. A leading {@code null} entry represents "no canvas".
     * Adding a new canvas type requires no change here - it is picked up
     * automatically from the registry (OCP).
     */
    private DefaultComboBoxModel<ICanvas> buildCanvasModel() {
        DefaultComboBoxModel<ICanvas> model = new DefaultComboBoxModel<>();
        model.addElement(null);
        for (ICanvas canvas : CanvasFeature.getAvailableCanvases()) {
            model.addElement(canvas);
        }
        return model;
    }

    private void clearCommand() {
        commandManager.clearCurrentCommand();
        updateCurrentCommandField();
    }

    public void updateCurrentCommandField() {
        currentCommandField.setText(commandManager.getCurrentCommandString());
        updateCanvasPanelCommand();
    }

    private void updateCanvasPanelCommand() {
        if (canvasPanel != null) {
            canvasPanel.setCommand(commandManager.getCurrentCommand());
        }
    }

    /**
     * Called when the user picks a canvas in the selector. Delegates to
     * {@link CanvasFeature#setCanvas(ICanvas)} so that the application has a
     * single source of truth for the current canvas; the preview is then
     * refreshed by the canvas-change notification via {@link #syncCanvasFromFeature()}.
     */
    private void onCanvasSelected() {
        ICanvas selected = (ICanvas) canvasSelector.getSelectedItem();
        if (selected != null && selected != CanvasFeature.getCanvas()) {
            CanvasFeature.setCanvas(selected);
        } else {
            // "None" selected, or no change reported by the feature - refresh directly.
            syncCanvasFromFeature();
        }
    }

    /**
     * Synchronises the selector and the preview with the current canvas held by
     * {@link CanvasFeature}, the single source of truth.
     */
    private void syncCanvasFromFeature() {
        ICanvas current = CanvasFeature.getCanvas();
        if (canvasSelector.getSelectedItem() != current) {
            canvasSelector.setSelectedItem(current);
        }
        if (canvasPanel != null) {
            canvasPanel.setCanvas(current);
        }
    }

    public void deleteObservers() {
        commandManager.getChangePublisher().clearObservers();
        this.updateObserverListField();
    }

    private void importCommands() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select command file to import");
        fileChooser.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON & TXT files", "json", "txt");
        fileChooser.addChoosableFileFilter(filter);

        int userSelection = fileChooser.showOpenDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToImport = fileChooser.getSelectedFile();
            try {
                String text = Files.readString(fileToImport.toPath());

                CommandImporter importer = CommandImporterFactory.getImporter(text);

                ICompoundCommand importedCommand = importer.importCommands(text);

                commandManager.setCurrentCommand(importedCommand);
            } catch (IOException ex) {
                System.err.println("Error reading the file: " + ex.getMessage());
            } catch (Exception ex) {
                System.err.println("Error parsing the file: " + ex.getMessage());
            }
        }
    }

    private void updateObserverListField() {
        observerListString = "";
        List<Subscriber> commandChangeSubscribers = commandManager.getChangePublisher().getSubscribers();
        for (Subscriber observer : commandChangeSubscribers) {
            observerListString += observer.toString() + System.lineSeparator();
        }
        if (commandChangeSubscribers.isEmpty())
            observerListString = "No observers loaded";

        observerListField.setText(observerListString);
    }

    @Override
    public void HideIfVisibleAndShowIfHidden() {
        updateObserverListField();
        if (this.isVisible()) {
            this.setVisible(false);
        } else {
            this.setVisible(true);
        }
    }

}
