package com.example.ecosim;

import javafx.geometry.Point2D;
import java.util.Random;

public class Herbivore extends Animal {
    private double stomachCapacity;
    private static final Random random = new Random();

    public Herbivore(String id, Point2D pos, double e, int speed, double stomachCapacity) {
        super(id, pos, e, speed);
        this.stomachCapacity = stomachCapacity;
    }

    @Override
    public void move() {
        double dx = (random.nextDouble() * 2 - 1) * speed;
        double dy = (random.nextDouble() * 2 - 1) * speed;
        Point2D p = position.add(dx, dy);
        position = clampToBounds(p);
    }

    @Override
    public void hunt() {
        // 草食なので空実装
    }

    @Override
    public AbstractOrganism reproduce() {
        // 例：エネルギー > 胃容量の 2 倍で繁殖
        if (energy > stomachCapacity * 2 && random.nextDouble() < 0.05) {
            Herbivore child = new Herbivore(
                id + "-c", position, energy / 2, speed, stomachCapacity
            );
            energy /= 2;
            return child;
        }
        return null;
    }

    private Point2D clampToBounds(Point2D p) {
        double x = Math.max(0, Math.min(Ecosystem.WIDTH,  p.getX()));
        double y = Math.max(0, Math.min(Ecosystem.HEIGHT, p.getY()));
        return new Point2D(x, y);
    }
}