package edu.kis.powp.jobs2d.command.gui;

import edu.kis.powp.appbase.gui.WindowComponent;
import edu.kis.powp.jobs2d.command.*;
import edu.kis.powp.jobs2d.command.manager.CommandManager;
import edu.kis.powp.jobs2d.command.visitor.CommandEditVisitor;
import edu.kis.powp.jobs2d.command.visitor.CommandTransformVisitor;
import edu.kis.powp.jobs2d.command.visitor.CommandTreeBuilderVisitor;
import edu.kis.powp.jobs2d.drivers.transformations.*;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;

public class ComplexCommandEditor extends JFrame implements WindowComponent {
    private final CommandManager commandManager;

    private ICompoundCommand workingCopy;
    private DriverCommand selectedCommand;
    private JTree commandTree;
    private DefaultTreeModel treeModel;
    private final JTextField xField = new JTextField();
    private final JTextField yField = new JTextField();

    public ComplexCommandEditor(CommandManager commandManager) {
        this.commandManager = commandManager;

        setTitle("Complex Command Editor");
        setSize(600, 500);
        setLayout(new BorderLayout());

        commandTree = new JTree();
        add(new JScrollPane(commandTree), BorderLayout.CENTER);

        rebuildTree();

        JPanel topPanel = new JPanel(new GridLayout(2, 2));

        topPanel.add(new JLabel("X"));
        topPanel.add(xField);
        topPanel.add(new JLabel("Y"));
        topPanel.add(yField);

        add(topPanel, BorderLayout.NORTH);

        commandTree.addTreeSelectionListener(e -> {

            DefaultMutableTreeNode node =
                    (DefaultMutableTreeNode) commandTree.getLastSelectedPathComponent();

            if (node == null) return;

            Object obj = node.getUserObject();

            if (obj instanceof DriverCommand) {
                selectedCommand = (DriverCommand) obj;
                updateFieldsFromSelection();
            } else {
                selectedCommand = null;
            }
        });

        JPanel bottomPanel = new JPanel(new GridLayout(2, 1));

        JPanel transformPanel = new JPanel(new GridLayout(2, 3));
        transformPanel.setBorder(BorderFactory.createTitledBorder("Whole Command Transformations"));

        JButton scaleUp = new JButton("Scale 2x");
        JButton scaleDown = new JButton("Scale 0.5x");
        JButton rotate = new JButton("Rotate 45");
        JButton flipX = new JButton("Flip X");
        JButton flipY = new JButton("Flip Y");
        JButton shift = new JButton("Shift");

        transformPanel.add(scaleUp);
        transformPanel.add(scaleDown);
        transformPanel.add(rotate);
        transformPanel.add(flipX);
        transformPanel.add(flipY);
        transformPanel.add(shift);

        JPanel editPanel = new JPanel(new GridLayout(1, 3));
        editPanel.setBorder(BorderFactory.createTitledBorder("Selected Command"));

        JButton apply = new JButton("Apply");
        JButton moveUp = new JButton("Move Up");
        JButton moveDown = new JButton("Move Down");

        editPanel.add(apply);
        editPanel.add(moveUp);
        editPanel.add(moveDown);

        bottomPanel.add(transformPanel);
        bottomPanel.add(editPanel);

        add(bottomPanel, BorderLayout.SOUTH);

        scaleUp.addActionListener(e ->
                applyTransformation(new ScaleTransformer(2.0, 2.0))
        );

        scaleDown.addActionListener(e ->
                applyTransformation(new ScaleTransformer(0.5, 0.5))
        );

        rotate.addActionListener(e ->
                applyTransformation(new RotateTransformer(45.0))
        );

        flipX.addActionListener(e ->
                applyTransformation(new FlipTransformer(true, false))
        );

        flipY.addActionListener(e ->
                applyTransformation(new FlipTransformer(false, true))
        );

        shift.addActionListener(e ->
                applyTransformation(new ShiftTransformer(Integer.parseInt(xField.getText()),
                        Integer.parseInt(yField.getText())))
        );

        moveUp.addActionListener(e -> {
            moveUpDeep((CompoundCommand) workingCopy, selectedCommand);
            commandManager.setCurrentCommand(workingCopy);
            rebuildTree();
        });

        moveDown.addActionListener(e -> {
            moveDownDeep((CompoundCommand) workingCopy, selectedCommand);
            commandManager.setCurrentCommand(workingCopy);
            rebuildTree();
        });

        apply.addActionListener(e -> applyChanges());
    }

    private void applyTransformation(CoordinateTransformer transformer) {
        CommandTransformVisitor visitor = new CommandTransformVisitor(transformer);
        workingCopy.accept(visitor);
        DriverCommand transformed = visitor.getTransformedCommand();

        if (transformed instanceof CompoundCommand) {
            workingCopy = (CompoundCommand) transformed;
        } else {
            CompoundCommand wrapper = new CompoundCommand();
            wrapper.addCommand(transformed);
            workingCopy = wrapper;
        }

        commandManager.setCurrentCommand(workingCopy);
        rebuildTree();
    }

    private boolean moveUpDeep(CompoundCommand parent, DriverCommand target) {
        for (int i = 0; i < parent.getCommandCount(); i++) {
            DriverCommand current = parent.getCommand(i);

            if (current == target) {
                if (i == 0) {
                    return true;
                }

                DriverCommand previous = parent.getCommand(i - 1);

                parent.setCommand(i - 1, current);
                parent.setCommand(i, previous);

                return true;
            }

            if (current instanceof CompoundCommand) {
                boolean moved =
                        moveUpDeep((CompoundCommand) current, target);
                if (moved) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean moveDownDeep(CompoundCommand parent, DriverCommand target) {
        for (int i = 0; i < parent.getCommandCount(); i++) {

            DriverCommand current = parent.getCommand(i);

            if (current == target) {

                if (i >= parent.getCommandCount() - 1) {
                    return true;
                }

                DriverCommand next = parent.getCommand(i + 1);

                parent.setCommand(i + 1, current);
                parent.setCommand(i, next);

                return true;
            }

            if (current instanceof CompoundCommand) {
                boolean moved =
                        moveDownDeep((CompoundCommand) current, target);

                if (moved) {
                    return true;
                }
            }
        }

        return false;
    }

    private void rebuildTree() {
        loadWorkingCopy();
        CommandTreeBuilderVisitor visitor = new CommandTreeBuilderVisitor();
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("ROOT");
        visitor.init(root);

        workingCopy.accept(visitor);
        treeModel = new DefaultTreeModel(root);
        commandTree.setModel(treeModel);
        commandTree.revalidate();
        commandTree.repaint();
    }

    private void loadWorkingCopy() {
        DriverCommand current = commandManager.getCurrentCommand();

        if (current == null) {
            workingCopy = new CompoundCommand();
            return;
        }

        DriverCommand copy = current.deepCopy();

        if (copy instanceof CompoundCommand) {
            workingCopy = (CompoundCommand) copy;
        } else {
            CompoundCommand wrapper = new CompoundCommand();
            wrapper.addCommand(copy);
            workingCopy = wrapper;
        }
    }

    private void applyChanges() {
        if (selectedCommand == null) {
            return;
        }

        int x, y;

        try {
            x = Integer.parseInt(xField.getText());
            y = Integer.parseInt(yField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid coordinates");
            return;
        }

        CommandEditVisitor visitor = new CommandEditVisitor(x, y);
        selectedCommand.accept(visitor);

        DriverCommand updated = visitor.getResult();

        replaceDeep((CompoundCommand) workingCopy, selectedCommand, updated);

        commandManager.setCurrentCommand(workingCopy);

        rebuildTree();
    }

    private void updateFieldsFromSelection() {
        if (selectedCommand == null) {
            return;
        }

        if (selectedCommand instanceof SetPositionCommand) {
            SetPositionCommand c = (SetPositionCommand) selectedCommand;
            xField.setText(String.valueOf(c.getPosX()));
            yField.setText(String.valueOf(c.getPosY()));
            return;
        }

        if (selectedCommand instanceof OperateToCommand) {
            OperateToCommand c = (OperateToCommand) selectedCommand;
            xField.setText(String.valueOf(c.getPosX()));
            yField.setText(String.valueOf(c.getPosY()));
            return;
        }

        xField.setText("");
        yField.setText("");
    }

    private boolean replaceDeep(CompoundCommand parent, DriverCommand target, DriverCommand replacement) {
        for (int i = 0; i < parent.getCommandCount(); i++) {

            DriverCommand current = parent.getCommand(i);

            if (current == target) {
                parent.setCommand(i, replacement);
                return true;
            }

            if (current instanceof CompoundCommand) {
                boolean replaced = replaceDeep((CompoundCommand) current, target, replacement);
                if (replaced) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void HideIfVisibleAndShowIfHidden() {
        if (this.isVisible()) {
            this.setVisible(false);
        } else {
            rebuildTree();
            this.setVisible(true);
        }
    }
}