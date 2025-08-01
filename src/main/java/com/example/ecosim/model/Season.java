package com.example.ecosim.model;

public enum Season {
    SPRING(19.5, 4.5),
    SUMMER(28, 4),
    AUTUMN(14, 4),
    WINTER(8, 3); // SPRING と WINTER の差を自然に、5℃未満に調整

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

    /** 各季節に応じたランダムな気温を返す */
    public double generateTemperature() {
        return averageTemperature + (Math.random() - 0.5) * 2 * temperatureVariance;
    }
}
