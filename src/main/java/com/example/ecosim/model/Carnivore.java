package com.example.ecosim.model;

import javafx.geometry.Point2D;
import java.util.Random; // reproduce でランダムを使うなら

public class Carnivore extends Animal {
    private double huntingSkill;
    private static final Random random = new Random();

    public Carnivore(String id, Point2D pos, double e, int speed, double huntingSkill) {
        super(id, pos, e, speed, 1.5);
        this.huntingSkill = huntingSkill;
    }

    @Override
    public void move(double dt) {
        Point2D dir = new Point2D(random.nextDouble() * 2 - 1, random.nextDouble() * 2 - 1).normalize();
        position = clampToBounds(position.add(dir.multiply(speed * dt)));
    }

    @Override
    public void hunt() {
        /* … */ }

    @Override
    public AbstractOrganism reproduce(double dt) {
        double reproRatePerSec = huntingSkill * 0.05;
        if (energy > 20 && random.nextDouble() < reproRatePerSec * dt) {
             Carnivore child = new Carnivore(id + "-c", position, energy / 2, speed, huntingSkill);
             energy /= 2;
             return child;
         }
         return null;
     }

    // 画面外補正メソッド（共通化してもOK）
    private Point2D clampToBounds(Point2D p) {
        double x = Math.max(0, Math.min(Ecosystem.WIDTH, p.getX()));
        double y = Math.max(0, Math.min(Ecosystem.HEIGHT, p.getY()));
        return new Point2D(x, y);
    }
}