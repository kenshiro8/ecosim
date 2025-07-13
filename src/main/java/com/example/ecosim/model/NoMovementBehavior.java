package com.example.ecosim.model;

import javafx.geometry.Point2D;

/**
 * 動かない個体用の MovementBehavior
 */
public class NoMovementBehavior implements MovementBehavior {
    @Override
    public Point2D computeNextPosition(AbstractOrganism o, double dt) {
        return o.getPosition();
    }
}