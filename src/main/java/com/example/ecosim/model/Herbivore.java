package com.example.ecosim.model;

import javafx.geometry.Point2D;
import java.util.Random;

public class Herbivore extends Animal {
    private double stomachCapacity;
    private static final Random random = new Random();

    public Herbivore(String id, Point2D pos, double e, int speed, double stomachCapacity) {
        super(id, pos, e, speed, 1.0);
        this.stomachCapacity = stomachCapacity;
    }

    @Override
    public void move(double dt) {
        Point2D dir = new Point2D(random.nextDouble() * 2 - 1, random.nextDouble() * 2 - 1).normalize();
        // speed(px/sec)×dt(sec) 分だけ移動
        position = clampToBounds(position.add(dir.multiply(speed * dt)));
    }

    @Override
    public void hunt() {
        // 草食なので空実装
    }

    @Override
    public AbstractOrganism reproduce(double dt) {
        double reproRatePerSec = 0.05;
        if (energy > stomachCapacity*2 && random.nextDouble() < reproRatePerSec * dt) {
             Herbivore child = new Herbivore(id + "-c", position, energy / 2, speed, stomachCapacity);
             energy /= 2;
             return child;
         }
         return null;
     }

    private Point2D clampToBounds(Point2D p) {
        double x = Math.max(0, Math.min(Ecosystem.WIDTH, p.getX()));
        double y = Math.max(0, Math.min(Ecosystem.HEIGHT, p.getY()));
        return new Point2D(x, y);
    }
}