package com.example.ecosim.model;

public class Environment {
    private double temperature;
    private double humidity;
    private Season season;

    public Environment(double temperature, double humidity, Season season) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.season = season;
    }

    public double getTemperature() { return temperature; }
    public double getHumidity()    { return humidity; }
    public Season getSeason()      { return season; }

    public void changeSeason() {
        this.season = season.next();
        this.temperature = season.generateTemperature();  // 季節ごとの気温にリセット

        switch (season) {
            case SPRING: humidity = 0.7; break;
            case SUMMER: humidity = 0.6; break;
            case AUTUMN: humidity = 0.8; break;
            case WINTER: humidity = 0.5; break;
        }
    }

    public void updateWeather() {
        double dt = (Math.random() - 0.5) * 1.0;   // ±0.5℃
        double seasonalMean = season.getAverageTemperature();
        double correction = (seasonalMean - temperature) * 0.01;  // 季節平均への収束項

        temperature = clamp(temperature + dt + correction, -10, 40);

        double dh = (Math.random() - 0.5) * 0.1;   // ±0.05
        humidity = clamp(humidity + dh, 0, 1);
    }

    private double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }
}
