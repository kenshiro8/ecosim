package com.example.ecosim.model;

import java.util.List;

import javafx.geometry.Point2D;

/**
 * 台風イベント：指定地点を中心に一定半径内の個体に
 * Typhoon 効果を適用します。
 */
public class TyphoonEvent implements EnvironmentalEvent {
    private final Point2D center;
    private final double radius;
    private final double strength;

    /**
     * @param center   台風の中心座標
     * @param radius   台風が影響を及ぼす半径
     * @param strength 台風の強さ（ダメージ係数）
     */
    public TyphoonEvent(Point2D center, double radius, double strength) {
        this.center = center;
        this.radius = radius;
        this.strength = strength;
    }

    @Override
    public void applyEvent(Ecosystem ecosystem) {
        for (Animal a : ecosystem.getAllAnimals()) {
            a.setPosition(a.getPosition()
                .add(Math.random() - 0.5, Math.random() - 0.5));
            a.energy -= strength * Math.random();
        }
    }

}