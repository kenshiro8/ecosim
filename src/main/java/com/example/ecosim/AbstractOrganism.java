package com.example.ecosim;

import javafx.geometry.Point2D;

public abstract class AbstractOrganism {
    protected String id;
    protected Point2D position;
    protected double energy;
    protected int age;

    public AbstractOrganism(String id, Point2D pos, double initEnergy) {
        this.id = id;
        this.position = pos;
        this.energy = initEnergy;
        this.age = 0;
    }

    public abstract void move();
    public void grow() {
        age++;
        // エネルギー減衰など
        energy -= 0.1;
    }
    public abstract AbstractOrganism reproduce();
    // getter / setter 省略
}