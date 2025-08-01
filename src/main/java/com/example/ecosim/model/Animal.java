// Animal (abstract)
package com.example.ecosim.model;

import javafx.geometry.Point2D;

public abstract class Animal extends AbstractOrganism {
    protected int speed;
    protected double metabolism;
    protected double maxEnergy;

    public Animal(String id, Point2D pos, double initialEnergy, int speed, double metabolism) {
        super(id, pos, initialEnergy);
        this.speed = speed;
        this.maxEnergy = initialEnergy;
        this.metabolism = metabolism;
    }

    /** MovementBehavior から呼ばれる speed 取得用 */
    public int getSpeed() {
        return speed;
    }

    @Override
    public void grow(double dt) {
        // 基礎代謝分 dt 秒だけ消費
        energy -= metabolism * dt;
        super.age += dt; // age の扱いを秒数に合わせるなら
    }

    public abstract AbstractOrganism hunt();

    public void flee() {
        /* 逃走ロジック */ }
}
