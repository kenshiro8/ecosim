package com.example.ecosim;

import javafx.geometry.Point2D;

public class Carnivore extends Animal {
    private double huntingSkill;

    public Carnivore(String id, Point2D pos, double e, int speed, double huntingSkill) {
        super(id, pos, e, speed);
        this.huntingSkill = huntingSkill;
    }

    @Override
    public void move() {
        // TODO: 肉食動物の移動ロジック（例：ランダム移動）
        position = position.add(Math.random() - 0.5, Math.random() - 0.5);
    }

    @Override
    public void hunt() { /* 捕食ロジック */ }

    public void chase(AbstractOrganism prey) { /* 追跡 */ }

    @Override
    public AbstractOrganism reproduce() { /* 繁殖ロジック */ return null; }
}