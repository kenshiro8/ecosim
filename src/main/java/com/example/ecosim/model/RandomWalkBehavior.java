package com.example.ecosim.model;

import javafx.geometry.Point2D;
import com.example.ecosim.util.*;

/**
 * ランダムウォークを行う MovementBehavior 実装
 */
public class RandomWalkBehavior implements MovementBehavior {
    private final double changeInterval;   // 何秒ごとに方向変更
    private double timeAcc = 0.0;
    private double dirX, dirY;

    public RandomWalkBehavior(double changeInterval) {
        this.changeInterval = changeInterval;
        resetDirection();
    }

    @Override
    public Point2D computeNextPosition(AbstractOrganism o, double dt) {
        // 1) 時間を加算し、一定間隔で方向リセット
        timeAcc += dt;
        if (timeAcc >= changeInterval) {
            timeAcc -= changeInterval;
            resetDirection();
        }

        // 2) Animal でなければ位置をそのまま返す
        if (!(o instanceof Animal)) {
            return o.getPosition();
        }

        // 3) Animal の speed を使って移動距離を算出
        Animal a = (Animal) o;
        double dist = a.getSpeed() * dt;
        Point2D next = o.getPosition().add(dirX * dist, dirY * dist);

        // 4) WorldUtil.wrap() でドーナツ型ワールドにラップ
        return WorldUtil.wrap(next, Ecosystem.WIDTH, Ecosystem.HEIGHT);
    }

    private void resetDirection() {
        double angle = RandomProvider.get().nextDouble() * 2 * Math.PI;
        dirX = Math.cos(angle);
        dirY = Math.sin(angle);
    }
}