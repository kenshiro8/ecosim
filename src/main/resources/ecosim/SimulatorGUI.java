// SimulatorGUI.java
package com.example.ecosim;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.List;

public class SimulatorGUI extends Application {
    private Label stepLabel  = new Label("Step: 0");
    private Label statsLabel = new Label("Plants:0 Herb:0 Carn:0 AvgE:0.0");
    private Canvas canvas;
    private SimulationEngine engine;

    @Override
    public void start(Stage stage) {
        // Canvas の生成（Ecosystem.WIDTH/HEIGHT を参照）
        canvas = new Canvas(Ecosystem.WIDTH, Ecosystem.HEIGHT);

        // ラベルを縦並びに
        VBox infoBox = new VBox(5, stepLabel, statsLabel);
        infoBox.setPadding(new Insets(10));

        // 全体レイアウト
        BorderPane root = new BorderPane();
        root.setTop(infoBox);
        root.setCenter(canvas);

        stage.setScene(new Scene(root));
        stage.setTitle("EcoSim Visualization");
        stage.show();

        // エンジン生成→GUI 登録→開始
        engine = new SimulationEngine(50, 20, 8);
        engine.setGui(this);
        engine.runSimulation();
    }

    /**
     * シミュレーションの進行に合わせて呼ばれる
     */
    public void updateDisplay(int step, 
                              java.util.Map<String, Long> counts, 
                              double avgE) {
        // ラベル更新
        stepLabel.setText("Step: " + step);
        statsLabel.setText(String.format(
            "Plants:%d Herb:%d Carn:%d AvgE:%.2f",
            counts.getOrDefault("Plant",0L),
            counts.getOrDefault("Herbivore",0L),
            counts.getOrDefault("Carnivore",0L),
            avgE));

        // Canvas を再描画
        drawOrganisms(engine.getEcosystem().getOrganisms());
    }

    /**
     * Canvas 上に個体を色分けして描画
     */
    private void drawOrganisms(List<AbstractOrganism> list) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        // 画面クリア
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for (AbstractOrganism o : list) {
            double x = o.getPosition().getX();
            double y = o.getPosition().getY();
            double r;
            Color c;

            if (o instanceof Plant) {
                r = 4; c = Color.LIGHTGREEN;
            } else if (o instanceof Herbivore) {
                r = 6; c = Color.CORNFLOWERBLUE;
            } else {  // Carnivore
                r = 8; c = Color.SALMON;
            }

            gc.setFill(c);
            gc.fillOval(x - r, y - r, r * 2, r * 2);
        }
    }
}