package com.example.ecosim;

import javafx.geometry.Point2D;
import java.util.Random;  // reproduce でランダムを使うなら

public class Carnivore extends Animal {
    private double huntingSkill;
    private static final Random random = new Random();

    public Carnivore(String id, Point2D pos, double e, int speed, double huntingSkill) {
        super(id, pos, e, speed);
        this.huntingSkill = huntingSkill;
    }

    @Override
    public void move() {
        // 既存の速度ベース移動
        double dx = (random.nextDouble() * 2 - 1) * speed;
        double dy = (random.nextDouble() * 2 - 1) * speed;
        Point2D p = position.add(dx, dy);
        position = clampToBounds(p);
    }

    @Override
    public void hunt() { /* … */ }

    @Override
    public AbstractOrganism reproduce() {
        // 例：エネルギーが一定以上で確率繁殖
        if (energy > 20 && random.nextDouble() < huntingSkill * 0.05) {
            Carnivore child = new Carnivore(
                id + "-c", position, energy / 2, speed, huntingSkill
            );
            energy /= 2;
            return child;
        }
        return null;
    }

    // 画面外補正メソッド（共通化してもOK）
    private Point2D clampToBounds(Point2D p) {
        double x = Math.max(0, Math.min(Ecosystem.WIDTH,  p.getX()));
        double y = Math.max(0, Math.min(Ecosystem.HEIGHT, p.getY()));
        return new Point2D(x, y);
    }
}