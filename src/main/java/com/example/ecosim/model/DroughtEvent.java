package com.example.ecosim.model;
import java.util.Iterator;
import java.util.List;
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
        List<Plant> plants = ecosystem.getPlants(); // List<Plant> を取得
        Iterator<Plant> iterator = plants.iterator();
        while (iterator.hasNext()) {
            Plant plant = iterator.next();
            boolean isDead = plant.applyDrought(interval);
            if (isDead) {
                iterator.remove();  // 枯死した植物をリストから削除
            }
        }
    }
}