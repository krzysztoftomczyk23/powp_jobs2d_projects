package edu.kis.powp.jobs2d.command;


import edu.kis.powp.jobs2d.command.visitor.ICommandVisitor;
import edu.kis.powp.jobs2d.drivers.visitor.VisitableDriver;

/**
 * Implementation of VisitableDriverCommand for operateTo command functionality.
 */
public class OperateToCommand implements DriverCommand {

    private int posX, posY;

    public OperateToCommand(int posX, int posY) {
        super();
        this.posX = posX;
        this.posY = posY;
    }

    @Override
    public void execute(VisitableDriver driver) {
        driver.operateTo(posX, posY);
    }

    @Override
    public void accept(ICommandVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public OperateToCommand deepCopy() {
        return new OperateToCommand(posX, posY);
    }

    @Override
    public String toString() {
        return "OperateTo(" + posX + ", " + posY + ")";
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

}