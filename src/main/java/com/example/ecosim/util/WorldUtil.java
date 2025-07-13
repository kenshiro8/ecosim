package com.example.ecosim.util;

import javafx.geometry.Point2D;

public final class WorldUtil {
    private WorldUtil() {}

    /**
     * 画面内に収めるクランプ
     */
    public static Point2D clamp(Point2D p, double width, double height) {
        double x = Math.max(0, Math.min(width, p.getX()));
        double y = Math.max(0, Math.min(height, p.getY()));
        return new Point2D(x, y);
    }

    /**
     * トーラス型ラップ
     */
    public static Point2D wrap(Point2D p, double width, double height) {
        double x = mod(p.getX(), width);
        double y = mod(p.getY(), height);
        return new Point2D(x, y);
    }

    private static double mod(double value, double bound) {
        double m = value % bound;
        return (m < 0) ? m + bound : m;
    }
}