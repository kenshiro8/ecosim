package com.example.ecosim.model;

import com.example.ecosim.util.RandomProvider;

import javafx.geometry.Point2D;

public class Carnivore extends Animal {
    private double huntingSkill;

    public Carnivore(String id, Point2D pos, double e, int speed, double huntingSkill) {
        super(id, pos, e, speed, 1.5);
        this.huntingSkill = huntingSkill;
        setMovementBehavior(new RandomWalkBehavior(1.0));
    }

    @Override
    public void hunt() {
    }


    @Override
    public AbstractOrganism reproduce(double dt) {
        double reproRatePerSec = huntingSkill * 0.05;
        if (energy > 30
                && RandomProvider.get().nextDouble() < reproRatePerSec * dt) {
            Carnivore child = new Carnivore(id + "-c", position, energy / 2, speed, huntingSkill);
            energy /= 2;
            return child;
        }
        return null;
    }
}