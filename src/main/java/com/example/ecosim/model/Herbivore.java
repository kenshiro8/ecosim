package com.example.ecosim.model;

import com.example.ecosim.util.*;

import javafx.geometry.Point2D;

public class Herbivore extends Animal {
    private double stomachCapacity;

    public Herbivore(String id, Point2D pos, double e, int speed, double stomachCapacity) {
        super(id, pos, e, speed, 1.0);
        this.stomachCapacity = stomachCapacity;
    }

    @Override
    public void move(double dt) {
        // 新しいランダム方向を取得
        double dx = RandomProvider.get().nextDouble() * 2 - 1;
        double dy = RandomProvider.get().nextDouble() * 2 - 1;
        Point2D dir = new Point2D(dx, dy).normalize();
        // 移動後にクランプ
        Point2D next = position.add(dir.multiply(speed * dt));
        position = WorldUtil.clamp(next, Ecosystem.WIDTH, Ecosystem.HEIGHT);
    }

    @Override
    public void hunt() {
        // 草食なので空実装
    }

    @Override
    public AbstractOrganism reproduce(double dt) {
        double reproRatePerSec = 0.05;
        if (energy > stomachCapacity * 2
                && RandomProvider.get().nextDouble() < reproRatePerSec * dt) {
            Herbivore child = new Herbivore(id + "-c", position, energy / 2, speed, stomachCapacity);
            energy /= 2;
            return child;
        }
        return null;
    }
}