package com.example.ecosim;

import com.example.ecosim.engine.SimulationEngine;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class SimulatorGUI extends Application {
    private Label stepLabel = new Label("Step: 0");
    private Label statsLabel = new Label("Plants:0 Herb:0 Carn:0 AvgE:0.0");
    private SimulationEngine engine = new SimulationEngine();

    @Override
    public void start(Stage stage) {
        VBox root = new VBox(10, stepLabel, statsLabel);
        root.setPadding(new Insets(10));
        stage.setScene(new Scene(root, 400, 200));
        stage.show();
        engine.runSimulation();  // 内部で GUI 更新を呼ぶように要拡張
    }

    // engine 側から呼ぶメソッド例
    public void updateDisplay(int step, Map<String, Long> counts, double avgE) {
        stepLabel.setText("Step: " + step);
        statsLabel.setText(String.format("Plants:%d Herb:%d Carn:%d AvgE:%.2f",
            counts.getOrDefault("Plant",0L),
            counts.getOrDefault("Herbivore",0L),
            counts.getOrDefault("Carnivore",0L),
            avgE));
    }
}