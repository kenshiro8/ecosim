package com.example.ecosim;

import java.util.Map;
import javafx.animation.*;
import javafx.util.Duration;
import javafx.application.Platform;



public class SimulationEngine {
    private final Ecosystem ecosystem;
    private SimulatorGUI gui; 
    private int timestep = 0;

    /** 初期個体数を受け取るコンストラクタ */
    public SimulationEngine(int numPlants,
                            int numHerbivores,
                            int numCarnivores) {
        this.ecosystem = new Ecosystem();
        // 初期化ロジックを呼び出し
        ecosystem.initialize(numPlants, numHerbivores, numCarnivores);
    }

    /** デフォルト呼び出しも用意 */
    public SimulationEngine() {
        this(30, 10, 5);  // デフォルト：植物30、草食10、肉食5
    }

    public void setGui(SimulatorGUI gui) {
        this.gui = gui;
    }

public void runSimulation() {
        Timeline loop = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            ecosystem.updateEcosystem();
            Map<String, Long> counts = ecosystem.countByType();
            double avgE = ecosystem.averageEnergy();

            // コンソール出力
            System.out.printf("Step %d | Plants:%d | Herb:%d | Carn:%d | AvgE:%.2f%n",
               timestep,
               counts.getOrDefault("Plant", 0L),
               counts.getOrDefault("Herbivore", 0L),
               counts.getOrDefault("Carnivore", 0L),
               avgE);

            // GUI 更新（UI スレッド上で安全に呼び出し）
            if (gui != null) {
                Platform.runLater(() -> 
                    gui.updateDisplay(timestep, counts, avgE)
                );
            }
            timestep++;
        }));
        loop.setCycleCount(Animation.INDEFINITE);
        loop.play();
    }


    private void printStats() {
        Map<String, Long> counts = ecosystem.countByType();
        double avgE = ecosystem.averageEnergy();
        System.out.printf("Step %d | Plants:%d | Herb:%d | Carn:%d | AvgE:%.2f%n",
            timestep,
            counts.getOrDefault("Plant", 0L),
            counts.getOrDefault("Herbivore", 0L),
            counts.getOrDefault("Carnivore", 0L),
            avgE);
        timestep++;
    }
}