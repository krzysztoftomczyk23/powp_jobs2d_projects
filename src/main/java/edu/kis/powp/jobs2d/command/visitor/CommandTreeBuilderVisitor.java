package edu.kis.powp.jobs2d.command.visitor;

import edu.kis.powp.jobs2d.command.DriverCommand;
import edu.kis.powp.jobs2d.command.ICompoundCommand;
import edu.kis.powp.jobs2d.command.OperateToCommand;
import edu.kis.powp.jobs2d.command.SetPositionCommand;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.Stack;

public class CommandTreeBuilderVisitor implements ICommandVisitor {

    private Stack<DefaultMutableTreeNode> stack = new Stack<>();
    private DefaultMutableTreeNode root;

    public void init(DefaultMutableTreeNode root) {
        this.root = root;
        stack.clear();
        stack.push(root);
    }

    public DefaultMutableTreeNode getRoot() {
        return root;
    }

    private void attach(DefaultMutableTreeNode node) {
        stack.peek().add(node);
    }

    @Override
    public void visit(SetPositionCommand command) {
        DefaultMutableTreeNode node =
                new DefaultMutableTreeNode(command);

        attach(node);
    }

    @Override
    public void visit(OperateToCommand command) {
        DefaultMutableTreeNode node =
                new DefaultMutableTreeNode(command);

        attach(node);
    }

    @Override
    public void visit(ICompoundCommand command) {

        DefaultMutableTreeNode node =
                new DefaultMutableTreeNode(command);

        attach(node);

        stack.push(node);

        for (DriverCommand child : command) {
            child.accept(this);
        }

        stack.pop();
    }
}