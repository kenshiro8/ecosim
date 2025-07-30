package com.example.ecosim.model;

import javafx.geometry.Point2D;
import com.example.ecosim.util.RandomProvider;

public class RandomWalkBehavior implements MovementBehavior {
    private final double speed;           // pixels/sec
    private final double changeInterval;  // 向き転換間隔（秒）
    private double timeSinceChange = 0;   
    private Point2D currentDir = new Point2D(1, 0);
    
    public RandomWalkBehavior(double speed, double changeInterval) {
        this.speed = speed;
        this.changeInterval = changeInterval;
        // 最初の向きを即決定
        changeDirection();
    }

    @Override
    public Point2D computeNextPosition(AbstractOrganism org, double dt) {
        timeSinceChange += dt;
        if (timeSinceChange >= changeInterval) {
            changeDirection();
            timeSinceChange = 0;
        }

        Point2D pos = org.getPosition();
        Point2D next = pos.add(currentDir.multiply(speed * dt));
        
        // 画面外クランプ
        double x = clamp(next.getX(), 0, Ecosystem.WIDTH);
        double y = clamp(next.getY(), 0, Ecosystem.HEIGHT);
        return new Point2D(x, y);
    }
    
    private void changeDirection() {
        double theta = RandomProvider.get().nextDouble() * 2 * Math.PI;
        currentDir = new Point2D(Math.cos(theta), Math.sin(theta));
    }
    
    private double clamp(double v, double min, double max) {
        return Math.min(Math.max(v, min), max);
    }
}