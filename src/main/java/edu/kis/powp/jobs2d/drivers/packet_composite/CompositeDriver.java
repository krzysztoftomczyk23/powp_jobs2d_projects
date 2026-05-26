package edu.kis.powp.jobs2d.drivers.packet_composite;


import edu.kis.powp.jobs2d.drivers.visitor.DriverVisitor;
import edu.kis.powp.jobs2d.drivers.visitor.VisitableDriver;

import java.util.*;


public class CompositeDriver implements VisitableDriver {
    private List<VisitableDriver> drivers;
    private final String name;

    public CompositeDriver(String name) {
        this.name = name;
        this.drivers = new ArrayList<>();
    }

    public CompositeDriver(){
        this("Composite Driver");
    }

    public void addDriver(VisitableDriver driver) {
        if(drivers == null) {
            throw new IllegalArgumentException("Drivers cannot be null");
        }
        else if(drivers == this){
            throw new IllegalArgumentException("Cannot add itself as a driver");
        }
        drivers.add(driver);
    }

    public boolean removeDriver(VisitableDriver driver) {
        if(drivers == null) {
            throw new IllegalArgumentException("Drivers cannot be null");
        }
        return drivers.remove(driver);
    }

    public List<VisitableDriver> getDrivers() {
        return drivers;
    }

    public int getDriverCount() {
        return drivers.size();
    }



    @Override
    public void setPosition(int x, int y) {
        for (VisitableDriver driver : drivers) {
            driver.setPosition(x, y);
        }
    }

    @Override
    public void operateTo(int x, int y) {
        for (VisitableDriver driver : drivers) {
            driver.operateTo(x, y);
        }
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public void accept(DriverVisitor visitor) {
        visitor.visit(this);
    }
}
