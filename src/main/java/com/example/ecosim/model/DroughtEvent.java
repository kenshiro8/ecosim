package com.example.ecosim.model;

/**
 * 干ばつイベント：一定時間ごとに植物を枯らす
 */
public class DroughtEvent implements EnvironmentalEvent {
    private final double interval;
    
    public DroughtEvent(double intervalSeconds) {
        this.interval = intervalSeconds;
    }

    @Override
    public void applyEvent(Ecosystem ecosystem) {
        // 例: 全 Plant のエネルギーを減らす
        for (Plant p : ecosystem.getPlants()) {
            p.applyDrought(interval);
        }
    }
}