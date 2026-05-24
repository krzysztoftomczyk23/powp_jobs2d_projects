package edu.kis.powp.jobs2d.command;

import java.util.Iterator;

import edu.kis.powp.jobs2d.command.visitor.ICommandVisitor;

/**
 * Interface extending VisitableDriverCommand to execute more than one command.
 */
public interface ICompoundCommand extends DriverCommand, Iterable<DriverCommand> {

    public Iterator<DriverCommand> iterator();

    /**
     * Default visitor accept implementation for compound commands.
     * All concrete compound implementations dispatch to the same
     * {@code visit(ICompoundCommand)} method on the visitor, so the
     * default works for every implementation without modification.
     */
    @Override
    default void accept(ICommandVisitor visitor) {
        visitor.visit(this);
    }

}