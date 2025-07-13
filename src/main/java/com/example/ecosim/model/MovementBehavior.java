package com.example.ecosim.model;

import javafx.geometry.Point2D;

public interface MovementBehavior {
    /**
     * 個体 o が dt 秒後にいるべき位置を返す
     */
    Point2D computeNextPosition(AbstractOrganism o, double dt);
}