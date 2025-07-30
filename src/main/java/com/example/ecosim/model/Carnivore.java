package com.example.ecosim.model;

import javafx.geometry.Point2D;
import com.example.ecosim.util.RandomProvider;

public class Carnivore extends Animal {
    private double huntingSkill;
    private final Ecosystem ecosystem;

    public Carnivore(
            String id,
            Point2D pos,
            double e,
            int speed,
            double huntingSkill,
            Ecosystem ecosystem) {
        super(id, pos, e, speed, 1.5);
        this.huntingSkill = huntingSkill;
        this.ecosystem = ecosystem;

        setMovementBehavior(
                new GoalDirectedBehavior(
                        () -> this.ecosystem.getHerbivores(),
                        speed));
    }

    @Override
    public void hunt() {
        // 既存の狩りロジックをここに残す
    }

    @Override
    public AbstractOrganism reproduce(double dt) {
        // 既存の繁殖ロジックをここに残す
        double reproRatePerSec = huntingSkill * 0.05;
        if (energy > 30
                && RandomProvider.get().nextDouble() < reproRatePerSec * dt) {
            Carnivore child = new Carnivore(
                    id + "-c",
                    position,
                    energy / 2,
                    speed,
                    huntingSkill,
                    ecosystem);
            energy /= 2;
            return child;
        }
        // 繁殖しなかった場合は null を返す
        return null;
    }
}