package com.example.ecosim.model;

import javafx.geometry.Point2D;

public class NoMovementBehavior implements MovementBehavior {
    @Override
    public Point2D computeNextPosition(AbstractOrganism org, double dt) {
        // 動かさない（常に同じ位置を返す）
        return org.getPosition();
    }
}