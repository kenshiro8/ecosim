package com.example.ecosim;

import javafx.geometry.Point2D;

public class Herbivore extends Animal {
    private double stomachCapacity;

    public Herbivore(String id, Point2D pos, double e, int speed, double stomachCapacity) {
        super(id, pos, e, speed);
        this.stomachCapacity = stomachCapacity;
    }

    @Override
    public void move() {
        // TODO: 草食動物の移動ロジック（例：ランダム移動）
        position = position.add(Math.random() - 0.5, Math.random() - 0.5);
    }

    @Override
    public void hunt() {
        // 草食動物は hunt を使わないので空実装
    }

    public void graze(Plant p) { /* 採食ロジック */ }

    @Override
    public AbstractOrganism reproduce() { /* 繁殖ロジック */ return null; }
}