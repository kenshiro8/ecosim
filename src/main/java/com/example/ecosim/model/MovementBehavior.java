package com.example.ecosim.model;

import javafx.geometry.Point2D;

/**
 * 個体の次ステップの位置を計算して返すインタフェース
 */
public interface MovementBehavior {
    /**
     * @param org 移動対象の個体
     * @param dt  経過時間（秒）
     * @return    次ステップの座標(Point2D)
     */
    Point2D computeNextPosition(AbstractOrganism org, double dt);
}