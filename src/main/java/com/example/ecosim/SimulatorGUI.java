package com.example.ecosim;

import com.example.ecosim.SimulationEngine;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.Map;

public class SimulatorGUI extends Application {
    private Label stepLabel = new Label("Step: 0");
    private Label statsLabel = new Label("Plants:0 Herb:0 Carn:0 AvgE:0.0");
    private SimulationEngine engine = new SimulationEngine();

@Override
public void start(Stage stage) {
    // ラベルやレイアウトのセットアップ
    VBox root = new VBox(10, stepLabel, statsLabel);
    root.setPadding(new Insets(10));
    stage.setScene(new Scene(root, 400, 200));
    stage.show();

    // Engine インスタンス生成／GUI を登録してからシミュレーション開始
    engine = new SimulationEngine(50, 20, 8);
    engine.setGui(this);
    engine.runSimulation();
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