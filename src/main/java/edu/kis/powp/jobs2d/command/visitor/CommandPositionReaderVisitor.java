package edu.kis.powp.jobs2d.command.visitor;

import edu.kis.powp.jobs2d.command.DriverCommand;
import edu.kis.powp.jobs2d.command.ICompoundCommand;
import edu.kis.powp.jobs2d.command.OperateToCommand;
import edu.kis.powp.jobs2d.command.SetPositionCommand;

public class CommandPositionReaderVisitor implements ICommandVisitor {

    private Integer x;
    private Integer y;

    @Override
    public void visit(SetPositionCommand cmd) {
        x = cmd.getPosX();
        y = cmd.getPosY();
    }

    @Override
    public void visit(OperateToCommand cmd) {
        x = cmd.getPosX();
        y = cmd.getPosY();
    }

    @Override
    public void visit(ICompoundCommand command) {
        for (DriverCommand cmd : command) {
            cmd.accept(this);
        }
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }
}