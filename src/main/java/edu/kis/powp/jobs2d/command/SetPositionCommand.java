package edu.kis.powp.jobs2d.command;


import edu.kis.powp.jobs2d.command.visitor.ICommandVisitor;
import edu.kis.powp.jobs2d.drivers.visitor.VisitableDriver;

/**
 * Implementation of VisitableDriverCommand for setPosition command functionality.
 */
public class SetPositionCommand implements DriverCommand {

    private int posX, posY;

    public SetPositionCommand(int posX, int posY) {
        super();
        this.posX = posX;
        this.posY = posY;
    }

    @Override
    public void execute(VisitableDriver driver) {
        driver.setPosition(posX, posY);
    }

    @Override
    public void accept(ICommandVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public SetPositionCommand deepCopy() {
        return new SetPositionCommand(posX, posY);
    }

    @Override
    public String toString() {
        return "SetPosition(" + posX + ", " + posY + ")";
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

}