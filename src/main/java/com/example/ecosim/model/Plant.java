package com.example.ecosim.model;

import javafx.geometry.Point2D;

public class Plant extends AbstractOrganism {
    private double growthRate;

    public Plant(String id, Point2D pos, double initEnergy, double growthRate) {
        super(id, pos, initEnergy);
        this.growthRate = growthRate;
    }

    @Override
    public void move(double dt) {
        // 植物は動かんよ
    }

    @Override
    public void grow(double dt) {
        // 基本の枯死ロジックも残すなら super.grow(dt);
        energy += growthRate * dt;
    }

    @Override
    public AbstractOrganism reproduce(double dt) {
        // 繁殖確率を秒率にスケーリング
        double reproRatePerSec = 0.05;
        if (energy > 10 && Math.random() < reproRatePerSec * dt) {
            Plant child = new Plant(id + "-c", position, energy / 2, growthRate);
            energy /= 2;
            return child;
        }
        return null;
    }

}