package com.example.ecosim.model;

import javafx.geometry.Point2D;
import com.example.ecosim.util.RandomProvider;

public class Carnivore extends Animal {
    // 繁殖パラメータのデフォルト値
    private static final double DEFAULT_REPRO_RATE_PER_SEC     = 0.15;
    private static final double DEFAULT_REPRO_ENERGY_THRESHOLD = 20.0;

    private double huntingSkill;
    private final Ecosystem ecosystem;
    private final double reproRatePerSec;
    private final double reproEnergyThreshold;

    /** フルパラメータ版コンストラクタ（Ecosystem 側で任意の値を渡したい場合） */
    public Carnivore(
            String id,
            Point2D pos,
            double initialEnergy,
            int speed,
            double huntingSkill,
            double reproRatePerSec,
            double reproEnergyThreshold,
            Ecosystem ecosystem) {
        super(id, pos, initialEnergy, speed, 1.5);
        this.huntingSkill         = huntingSkill;
        this.reproRatePerSec      = reproRatePerSec;
        this.reproEnergyThreshold = reproEnergyThreshold;
        this.ecosystem            = ecosystem;

        setMovementBehavior(
            new GoalDirectedBehavior(
                () -> this.ecosystem.getHerbivores(),
                speed));
    }

    /** デフォルト設定を使う簡易コンストラクタ */
    public Carnivore(
            String id,
            Point2D pos,
            double initialEnergy,
            int speed,
            double huntingSkill,
            Ecosystem ecosystem) {
        this(
            id,
            pos,
            initialEnergy,
            speed,
            huntingSkill,
            DEFAULT_REPRO_RATE_PER_SEC,
            DEFAULT_REPRO_ENERGY_THRESHOLD,
            ecosystem
        );
    }

    @Override
    public void hunt() {
        // ここに狩りロジックを実装
    }

    @Override
    public AbstractOrganism reproduce(double dt) {
        if (energy > reproEnergyThreshold
            && RandomProvider.get().nextDouble() < reproRatePerSec * dt) {
            double childEnergy = energy / 2;
            Carnivore child = new Carnivore(
                id + "-c",
                position,
                childEnergy,
                speed,
                huntingSkill,
                reproRatePerSec,
                reproEnergyThreshold,
                ecosystem);
            energy -= childEnergy;
            return child;
        }
        return null;
    }
}