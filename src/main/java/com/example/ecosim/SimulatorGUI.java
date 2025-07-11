// SimulatorGUI.java
package com.example.ecosim;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.Map.Entry;
import javafx.animation.FadeTransition;
import javafx.util.Duration;
import java.util.*;

public class SimulatorGUI extends Application {
    private Label stepLabel  = new Label("Step: 0");
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
            getClass().getResource("/ecosim/style.css").toExternalForm()
            );

        stage.setScene(scene);
        stage.setTitle("EcoSim デフォルメ版");
        stage.show();

        engine = new SimulationEngine(50, 20, 8);
        engine.setGui(this);
        engine.runSimulation();
    }

    public void updateDisplay(int step, Map<String, Long> counts, double avgE) {
        // ラベル更新
        stepLabel.setText("Step: " + step);
        statsLabel.setText(String.format(
          "Plants:%d Herb:%d Carn:%d AvgE:%.2f",
          counts.getOrDefault("Plant",0L),
          counts.getOrDefault("Herbivore",0L),
          counts.getOrDefault("Carnivore",0L),
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
        Iterator<Entry<AbstractOrganism, OrganismView>> it
            = viewMap.entrySet().iterator();
        while (it.hasNext()) {
            Entry<AbstractOrganism, OrganismView> entry = it.next();
            if (!current.contains(entry.getKey())) {               FadeTransition ft = new FadeTransition(
                    Duration.millis(500),
                    entry.getValue().getNode()
                );
                ft.setToValue(0);
                ft.setOnFinished(evt -> drawPane.getChildren().remove(entry.getValue().getNode()));
                ft.play();

                it.remove();
            }
        }
    }
}