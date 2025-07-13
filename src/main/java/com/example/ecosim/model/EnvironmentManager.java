package com.example.ecosim.model;

import com.example.ecosim.model.*;

public class EnvironmentManager {
    private final Environment env;

    public EnvironmentManager(Environment env) {
        this.env = env;
    }

    /** 毎ステップ呼び出し */
    public void update(double dt) {
        env.updateWeather();
        if (Math.random() < 0.0005) {
            env.changeSeason();
        }
    }

    public Environment getEnvironment() {
        return env;
    }
}