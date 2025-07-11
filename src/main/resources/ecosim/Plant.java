package com.example.ecosim;
import javafx.geometry.Point2D;

public class Plant extends AbstractOrganism {
    private double growthRate;

    public Plant(String id, Point2D pos, double initEnergy, double growthRate) {
        super(id, pos, initEnergy);
        this.growthRate = growthRate;
    }

    @Override
    public void move() {
        double dx = (Math.random() - 0.5) * 2;
        double dy = (Math.random() - 0.5) * 2;
        position = position.add(dx, dy);
    }

    @Override
    public AbstractOrganism reproduce() {
        if (energy > 10 && Math.random() < 0.05) {
            Plant child = new Plant(id + "-c", position, energy / 2, growthRate);
            energy /= 2;
            return child;
        }
        return null;
    }

    public void photosynthesize() {
        energy += growthRate;
    }
}