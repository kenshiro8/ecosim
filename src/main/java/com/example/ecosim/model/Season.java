package com.example.ecosim.model;

public enum Season {
    SPRING(16, 2),   // 14〜18℃
    SUMMER(28, 4),   // 24〜32℃
    AUTUMN(13, 3),   // 10〜16℃
    WINTER(5, 2);    // 3〜7℃

    private final double averageTemperature;
    private final double temperatureVariance;

    Season(double averageTemperature, double temperatureVariance) {
        this.averageTemperature = averageTemperature;
        this.temperatureVariance = temperatureVariance;
    }

    public Season next() {
        return values()[(ordinal() + 1) % values().length];
    }

    public double getAverageTemperature() {
        return averageTemperature;
    }

    public double getTemperatureVariance() {
        return temperatureVariance;
    }

    /** 各季節に応じたランダムな気温を返す  */
    public double generateTemperature() {
        return averageTemperature + (Math.random() - 0.5) * 2 * temperatureVariance;
    }
}
