package com.example.ecosim;

public class Plant extends AbstractOrganism {
    private double growthRate;

    public Plant(String id, Point2D pos, double initEnergy, double growthRate) {
        super(id, pos, initEnergy);
        this.growthRate = growthRate;
    }

    @Override
    public void move() { /* 植物は移動しない */ }

    @Override
    public AbstractOrganism reproduce() {
        // 一定確率で新しい Plant を返す or null
    }

    public void photosynthesize() {
        energy += growthRate;
    }
}