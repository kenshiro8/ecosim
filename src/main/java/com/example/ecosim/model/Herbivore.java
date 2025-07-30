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
            Ecosystem ecosystem) {
        super(id, pos, e, speed, 0.1);
        this.stomachCapacity = stomachCapacity;
        this.ecosystem = ecosystem;

        setMovementBehavior(
                new GoalDirectedBehavior(
                        () -> this.ecosystem.getPlants(),
                        speed));
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
                    ecosystem);
            energy /= 1.5;
            return child;
        }
        return null;
    }

    /**
     * 引数は「消費対象の Plant リスト」のみを渡す
     * 成功した Plant は戻り値で返し、呼び出し側で ecosystem から remove する
     */
    public Plant eat(List<Plant> plants) {
        double eatRange = 50.0;
        double energyGainPerPlant = 20.0;

        for (Plant plant : plants) {
            if (plant.getPosition().distance(this.getPosition()) <= eatRange) {
                this.energy = Math.min(this.energy + energyGainPerPlant, stomachCapacity);
                return plant;
            }
        }
        return null;
    }
}