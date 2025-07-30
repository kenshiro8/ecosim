package com.example.ecosim.model;

import java.util.Iterator;
import java.util.List;

import com.example.ecosim.util.RandomProvider;
import javafx.geometry.Point2D;

public class Herbivore extends Animal {
    private double stomachCapacity;
    private final Ecosystem ecosystem;

    public Herbivore(
        String id,
        Point2D pos,
        double e,
        int speed,
        double stomachCapacity,
        Ecosystem ecosystem
    ) {
        super(id, pos, e, speed, 1.0);
        this.stomachCapacity = stomachCapacity;
        this.ecosystem = ecosystem;

        setMovementBehavior(
            new GoalDirectedBehavior(
                () -> this.ecosystem.getPlants(),
                speed
            )
        );
    }

    @Override
    public void hunt() {
        // 草食なので空実装
    }

    @Override
    public AbstractOrganism reproduce(double dt) {
        double reproRatePerSec = 0.5;
        if (energy > stomachCapacity * 1.2
                && RandomProvider.get().nextDouble() < reproRatePerSec * dt) {
            Herbivore child = new Herbivore(
                id + "-c",
                position,
                energy / 2,
                speed,
                stomachCapacity,
                ecosystem
            );
            energy /= 1.5;
            return child;
        }
        return null;
    }

    public void eat(List<AbstractOrganism> organisms) {
        if (organisms == null) return;

        double eatRange = 50.0;
        double energyGainPerPlant = 20.0;

        Iterator<AbstractOrganism> it = organisms.iterator();
        while (it.hasNext()) {
            AbstractOrganism o = it.next();
            if (o instanceof Plant) {
                double dist = o.getPosition().distance(this.getPosition());
                if (dist <= eatRange) {
                    this.energy = Math.min(this.energy + energyGainPerPlant, stomachCapacity);
                    it.remove();
                    break;
                }
            }
        }
    }
}