// SimulatorGUI.java
package com.example.ecosim;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.Node;
import javafx.stage.Stage;
import java.util.Map.Entry;
import javafx.util.Duration;
import java.util.*;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;

public class SimulatorGUI extends Application {
    private Label stepLabel = new Label("Step: 0");
    private Label statsLabel = new Label("Plants:0 Herb:0 Carn:0 AvgE:0.0");
    private Pane drawPane;
    private SimulationEngine engine;
    private Map<AbstractOrganism, OrganismView> viewMap = new HashMap<>();

    @Override
    public void start(Stage stage) {
        drawPane = new Pane();
        drawPane.setPrefSize(Ecosystem.WIDTH, Ecosystem.HEIGHT);

        VBox infoBox = new VBox(5, stepLabel, statsLabel);
        infoBox.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setTop(infoBox);
        root.setCenter(drawPane);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(
                getClass().getResource("/ecosim/style.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("生態系シュミレーター");
        stage.show();

        engine = new SimulationEngine(50, 20, 8);
        engine.setGui(this);
        AnimationTimer timer = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (lastUpdate == 0) {
                    // 最初のフレームだけスキップして時間を初期化
                    lastUpdate = now;
                    return;
                }
                // 50ms(20fps)ごとに1ステップ進める
                if (now - lastUpdate >= 50_000_000) {
                    engine.step(); // 1ステップ分のロジック
                    updateDisplay(
                            engine.getStepCount(),
                            engine.getCounts(),
                            engine.getAverageEnergy());
                    lastUpdate = now;
                }
            }
        };
        updateDisplay(
                engine.getStepCount(),
                engine.getCounts(),
                engine.getAverageEnergy());
        timer.start();
    }

    public void updateDisplay(int step, Map<String, Long> counts, double avgE) {
        // ラベル更新
        stepLabel.setText("Step: " + step);
        statsLabel.setText(String.format(
                "Plants:%d Herb:%d Carn:%d AvgE:%.2f",
                counts.getOrDefault("Plant", 0L),
                counts.getOrDefault("Herbivore", 0L),
                counts.getOrDefault("Carnivore", 0L),
                avgE));

        List<AbstractOrganism> current = engine.getEcosystem().getOrganisms();

        // 追加 & アニメーション移動
        for (AbstractOrganism o : current) {
            OrganismView ov = viewMap.get(o);
            if (ov == null) {
                ov = new OrganismView(o);
                viewMap.put(o, ov);
                drawPane.getChildren().add(ov.getNode());
                ov.updatePositionInstant();
            } else {
                ov.updatePositionAnimated();
            }
        }

        // 死亡個体のフェードアウト＆削除
        Iterator<Entry<AbstractOrganism, OrganismView>> it = viewMap.entrySet().iterator();
        while (it.hasNext()) {
            Entry<AbstractOrganism, OrganismView> entry = it.next();
            if (!current.contains(entry.getKey())) {
                // 削除するビューとモデル
                Node node = entry.getValue().getNode();
                double origX = node.getTranslateX();
                double origY = node.getTranslateY();

                // フェードアウト
                FadeTransition ft = new FadeTransition(Duration.millis(600), node);
                ft.setToValue(0);

                ft.setOnFinished(evt -> {
                    // 破片エフェクト
                    for (int i = 0; i < 6; i++) {
                        Circle shard = new Circle(2, Color.GRAY);
                        shard.setTranslateX(origX + node.getBoundsInParent().getWidth() / 2);
                        shard.setTranslateY(origY + node.getBoundsInParent().getHeight() / 2);
                        drawPane.getChildren().add(shard);

                        TranslateTransition pt = new TranslateTransition(
                                Duration.millis(800 + (int) (Math.random() * 200)), shard);
                        pt.setByX((Math.random() - 0.5) * 40);
                        pt.setByY((Math.random() - 0.5) * 40);
                        pt.setInterpolator(Interpolator.EASE_OUT);
                        pt.setOnFinished(e -> drawPane.getChildren().remove(shard));
                        pt.play();
                    }
                    drawPane.getChildren().remove(node);
                });
                ft.play();
                it.remove();
            }
        }
        // 追加 & アニメーション移動
        for (AbstractOrganism o : current) {
            OrganismView ov = viewMap.get(o);
            if (ov == null) {
                ov = new OrganismView(o);
                viewMap.put(o, ov);
                drawPane.getChildren().add(ov.getNode());

                // ← ここでポップイン再生
                ov.playSpawnAnimation();
                ov.updatePositionInstant();
            } else {
                ov.updatePositionAnimated();
            }
        }
    }
}