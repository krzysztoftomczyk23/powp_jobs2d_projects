package edu.kis.powp.jobs2d.command.visitor;

import edu.kis.powp.jobs2d.command.*;

public class CommandEditVisitor implements ICommandVisitor {

    private final int newX;
    private final int newY;
    private DriverCommand result;

    public CommandEditVisitor(int x, int y) {
        this.newX = x;
        this.newY = y;
    }

    public DriverCommand getResult() {
        return result;
    }

    @Override
    public void visit(SetPositionCommand command) {
        result = new SetPositionCommand(newX, newY);
    }

    @Override
    public void visit(OperateToCommand command) {
        result = new OperateToCommand(newX, newY);
    }

    @Override
    public void visit(ICompoundCommand command) {
        CompoundCommand updated = new CompoundCommand();

        for (DriverCommand child : command) {
            child.accept(this);
            updated.addCommand(result);
        }

        result = updated;
    }
}