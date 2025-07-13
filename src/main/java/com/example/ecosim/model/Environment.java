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
        switch (season) {
            case SPRING: temperature = 15; humidity = 0.7; break;
            case SUMMER: temperature = 25; humidity = 0.6; break;
            case AUTUMN: temperature = 10; humidity = 0.8; break;
            case WINTER: temperature = 0;  humidity = 0.5; break;
        }
    }

    public void updateWeather() {
        double dt = (Math.random() - 0.5) * 2;   // ±1℃
        double dh = (Math.random() - 0.5) * 0.1; // ±0.05
        temperature = clamp(temperature + dt, -10, 40);
        humidity    = clamp(humidity + dh,        0, 1);
    }

    private double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }
}