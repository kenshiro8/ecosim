package com.example.ecosim.model;

import com.example.ecosim.util.*;

import javafx.geometry.Point2D;

public class Carnivore extends Animal {
    private double huntingSkill;

    public Carnivore(String id, Point2D pos, double e, int speed, double huntingSkill) {
        super(id, pos, e, speed, 1.5);
        this.huntingSkill = huntingSkill;
    }

    @Override
    public void move(double dt) {
        // ランダムな方向ベクトル
        double dx = RandomProvider.get().nextDouble() * 2 - 1;
        double dy = RandomProvider.get().nextDouble() * 2 - 1;
        Point2D dir = new Point2D(dx, dy).normalize();
        // 移動後は画面内にクランプ
        Point2D next = position.add(dir.multiply(speed * dt));
        position = WorldUtil.clamp(next, Ecosystem.WIDTH, Ecosystem.HEIGHT);
    }

    @Override
    public void hunt() {
        /* … */ }

    @Override
    public AbstractOrganism reproduce(double dt) {
        double reproRatePerSec = huntingSkill * 0.05;
        if (energy > 20
                && RandomProvider.get().nextDouble() < reproRatePerSec * dt) {
            Carnivore child = new Carnivore(id + "-c", position, energy / 2, speed, huntingSkill);
            energy /= 2;
            return child;
        }
        return null;
    }
}