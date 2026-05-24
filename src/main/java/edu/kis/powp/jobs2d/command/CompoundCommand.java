package edu.kis.powp.jobs2d.command;

import edu.kis.powp.jobs2d.drivers.visitor.VisitableDriver;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;



/**
 * Concrete implementation of ICompoundCommand. Executes multiple commands in
 * sequence and provides iteration over them.
 */
public class CompoundCommand implements ICompoundCommand {

    private List<DriverCommand> commands;
    private String name;

    /**
     * Creates an empty compound command.
     */
    public CompoundCommand() {
        this.commands = new ArrayList<>();
        this.name = null;
    }

    /**
     * Creates a compound command with initial commands.
     *
     * @param commands initial list of commands
     */
    public CompoundCommand(List<DriverCommand> commands) {
        this.commands = new ArrayList<>(commands);
        this.name = null;
    }

    /**
     * Creates a compound command with initial commands and a name.
     *
     * @param commands initial list of commands
     * @param name     name of the compound command
     */
    public CompoundCommand(List<DriverCommand> commands, String name) {
        this.commands = new ArrayList<>(commands);
        this.name = name;
    }

    /**
     * Add a command to the compound command.
     *
     * @param command the command to add
     */
    public void addCommand(DriverCommand command) {
        commands.add(command);
    }

    public void setCommand(int index, DriverCommand command) {
        commands.set(index, command);
    }

    public DriverCommand getCommand(int index) {
        return commands.get(index);
    }

    /**
     * Remove a command from the compound command.
     *
     * @param command the command to remove
     * @return true if command was removed, false otherwise
     */
    public boolean removeCommand(DriverCommand command) {
        return commands.remove(command);
    }

    /**
     * Clear all commands from the compound command.
     */
    public void clearCommands() {
        commands.clear();
    }

    /**
     * Get the number of commands in this compound command.
     *
     * @return number of commands
     */
    public int getCommandCount() {
        return commands.size();
    }

    /**
     * Get the name of this compound command.
     *
     * @return name of the command, or null if not set
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of this compound command.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Execute all commands in sequence on the given driver.
     *
     * @param driver the driver to execute commands on
     */
    @Override
    public void execute(VisitableDriver driver) {
        for (DriverCommand command : commands) {
            command.execute(driver);
        }
    }

    /**
     * Get iterator over commands.
     *
     * @return iterator over commands
     */
    @Override
    public Iterator<DriverCommand> iterator() {
        return commands.iterator();
    }

    @Override
    public String toString() {
        if (name != null) {
            return name;
        }
        return "CompoundCommand [" + commands.size() + " commands]";
    }

    @Override
    public CompoundCommand deepCopy() {
        CompoundCommand copy = new CompoundCommand();
        copy.name = this.name;
        for (DriverCommand cmd : commands) {
            copy.addCommand(cmd.deepCopy());
        }
        return copy;
    }
}
