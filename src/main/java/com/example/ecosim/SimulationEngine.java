package com.example.ecosim;

import javafx.animation.*;
import javafx.util.Duration;

public class SimulationEngine {
    private Ecosystem ecosystem = new Ecosystem();
    private int timestep = 0;

    public void runSimulation() {
        Timeline loop = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            ecosystem.updateEcosystem();
            printStats();
            timestep++;
        }));
        loop.setCycleCount(Animation.INDEFINITE);
        loop.play();
    }

    private void printStats() {
        Map<String, Long> counts = ecosystem.countByType();
        double avgE = ecosystem.averageEnergy();
        System.out.printf("Step %d | Plants: %d | Herbivores: %d | Carnivores: %d | AvgEnergy: %.2f%n",
            timestep,
            counts.getOrDefault("Plant", 0L),
            counts.getOrDefault("Herbivore", 0L),
            counts.getOrDefault("Carnivore", 0L),
            avgE);
    }
}