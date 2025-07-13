package com.example.ecosim.model;

import javafx.geometry.Point2D;

public class Typhoon {
    private Point2D center;
    private double radius;
    private double strength;

    public Typhoon(Point2D center, double radius, double strength) {
        this.center = center;
        this.radius = radius;
        this.strength = strength;
    }

    public boolean hits(AbstractOrganism o) {
        return o.getPosition().distance(center) <= radius;
    }

    public void applyEffect(AbstractOrganism o) {
        o.energy -= strength * Math.random(); // ランダム減衰
        if (o instanceof Animal) {
            // 風に流されるように移動
            Animal a = (Animal) o;
            Point2D wind = new Point2D(Math.random() - 0.5, Math.random() - 0.5).normalize().multiply(5);
            a.position = a.position.add(wind);
        }
    }
}