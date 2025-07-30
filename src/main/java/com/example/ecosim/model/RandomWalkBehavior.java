package com.example.ecosim.model;

import javafx.geometry.Point2D;
import com.example.ecosim.util.RandomProvider;

public class RandomWalkBehavior implements MovementBehavior {
    private final double stepSize;
    public RandomWalkBehavior(double stepSize) {
        this.stepSize = stepSize;
    }

    @Override
    public Point2D computeNextPosition(AbstractOrganism org, double dt) {
        // ランダム方向ベクトル
        double angle = RandomProvider.get().nextDouble() * 2 * Math.PI;
        double dx = Math.cos(angle) * stepSize * dt;
        double dy = Math.sin(angle) * stepSize * dt;
        Point2D next = org.getPosition().add(dx, dy);

        // 画面外に出ないようにクランプ
        double x = Math.min(Math.max(next.getX(), 0), Ecosystem.WIDTH);
        double y = Math.min(Math.max(next.getY(), 0), Ecosystem.HEIGHT);
        return new Point2D(x, y);
    }
}