package com.example.ecosim.model;

import java.util.Iterator;
import java.util.List;

import com.example.ecosim.util.RandomProvider;

import javafx.geometry.Point2D;

public class Herbivore extends Animal {
    private double stomachCapacity;

    public Herbivore(String id, Point2D pos, double e, int speed, double stomachCapacity) {
        super(id, pos, e, speed, 1.0);
        this.stomachCapacity = stomachCapacity;
        // 移動Behavior を注入（1秒ごとに方向変更）
        setMovementBehavior(new RandomWalkBehavior(1.0));
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
            Herbivore child = new Herbivore(id + "-c", position, energy / 2, speed, stomachCapacity);
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