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
    public AbstractOrganism hunt() {
        return null;
    }

    @Override
    public AbstractOrganism reproduce(double dt) {
        double reproRatePerSec = 0.5;
        if (energy > maxEnergy * 0.5
                && RandomProvider.get().nextDouble() < reproRatePerSec * dt) {
            double childEnergy = energy / 2;
            Herbivore child = new Herbivore(
                    id + "-c",
                    position,
                    childEnergy,
                    speed,
                    stomachCapacity,
                    ecosystem);
            energy -= childEnergy; // 子に与えた分だけ親は消費
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
        if (stomachCapacity <= 0)
            return null; // 満腹なら何もしない

        for (Plant plant : plants) {
            if (plant.getPosition().distance(getPosition()) <= eatRange) {
                double gained = Math.min(energyGainPerPlant, stomachCapacity);
                energy = Math.min(energy + gained, this.maxEnergy);
                stomachCapacity -= gained;
                return plant;
            }
        }
        return null;
    }
}