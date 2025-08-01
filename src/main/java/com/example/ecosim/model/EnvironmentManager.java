package com.example.ecosim.model;

public class EnvironmentManager {
    private final Environment env;
    private int stepCounter = 0; // ステップ数カウンタ

    public EnvironmentManager(Environment env) {
        this.env = env;
    }

    /** 毎ステップ呼び出し（例：1フレーム = 1ステップ） */
    public void update(double dt) {
        env.updateWeather();
        stepCounter++;

        if (stepCounter >= 450) {
            env.changeSeason();
            stepCounter = 0;
        }
    }

    public Environment getEnvironment() {
        return env;
    }
}
