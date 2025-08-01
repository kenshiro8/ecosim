package com.example.ecosim.model;

public class EnvironmentManager {
    private final Environment env;
    private int stepCounter = 0;

    public EnvironmentManager(Environment env) {
        this.env = env;
    }

    public void update(double dt) {
        env.updateWeather();
        stepCounter++;

        // 450ステップ（フレーム）ごとに季節変更
        if (stepCounter >= 450) {
            env.changeSeason();
            stepCounter = 0;
        }
    }

    public Environment getEnvironment() {
        return env;
    }
}
