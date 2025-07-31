package com.example.ecosim.model;

import javafx.geometry.Point2D;

public class Plant extends AbstractOrganism {
    private final Ecosystem ecosystem;
    private double growthRate;
    private double carryingCapacity;
    private double reproductionThreshold;

    // Ecosystem を受け取るコンストラクタを追加
    public Plant(
        String id,
        Point2D pos,
        double initEnergy,
        double growthRate,
        double carryingCapacity,
        double reproductionThreshold,
        Ecosystem ecosystem) {
        super(id, pos, initEnergy);
        this.growthRate = growthRate;
        this.carryingCapacity = carryingCapacity;
        this.reproductionThreshold = reproductionThreshold;
        this.ecosystem = ecosystem;
    }

    @Override
    public AbstractOrganism reproduce(double dt) {
        // 1) ロジスティック成長
        energy += growthRate * energy * (1 - energy / carryingCapacity) * dt;

        // 2) 繁殖閾値を超えたら密度依存判定
        if (energy >= reproductionThreshold) {
            int currentN = ecosystem.getPlantCount();
            int Kpop    = ecosystem.getMaxPlants();
            double densityFactor = Math.max(0.0, 1.0 - (double) currentN / Kpop);

            // 確率的に抑制
            if (Math.random() < densityFactor) {
                double childEnergy = energy / 2.0;
                energy /= 2.0;
                Point2D childPos = position.add(Math.random() - 0.5, Math.random() - 0.5);

                return new Plant(
                    id + "-p",
                    childPos,
                    childEnergy,
                    growthRate,
                    carryingCapacity,
                    reproductionThreshold,
                    ecosystem);
            }
        }
        return null;
    }

    /**
     * 干ばつによるエネルギー減少と死亡判定
     * @param severity 干ばつの深刻度
     * @return true = エネルギーが0以下（死）
     */
    public boolean applyDrought(double severity) {
        energy -= severity * 30.0;
        return energy <= 0;
    }
    /**
     * ID取得（デバッグや表示用）
     */
    public String getId() {
        return this.id;
    }
}