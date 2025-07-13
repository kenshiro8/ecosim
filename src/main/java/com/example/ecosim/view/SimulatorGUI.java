package com.example.ecosim.view;

import com.example.ecosim.model.*;

import java.util.*;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.animation.*;
import javafx.util.Duration;
import javafx.scene.Node;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;

public class SimulatorGUI extends Application {
    private Label stepLabel = new Label("Step: 0");
    private Label statsLabel = new Label("Plants:0 Herb:0 Carn:0 AvgE:0.0");
    private Pane drawPane;
    private SimulationEngine engine;
    private Map<AbstractOrganism, OrganismView> viewMap = new HashMap<>();

    private AnimationTimer timer;
    private boolean running = false;

    private Button startBtn;
    private Button stopBtn;
    private Button resetBtn;
    private Slider speedSlider;
    private Button typhoonBtn;
    private Button droughtBtn;

    @Override
    public void start(Stage stage) {
        // 1) コントロールバーの作成
        startBtn = new Button("開始");
        stopBtn = new Button("停止");
        resetBtn = new Button("リセット");
        typhoonBtn = new Button("台風発生");
        droughtBtn = new Button("干ばつ発生");

        speedSlider = new Slider(0.1, 5.0, 1.0);
        speedSlider.setShowTickLabels(true);
        speedSlider.setShowTickMarks(true);
        speedSlider.setMajorTickUnit(1.0);
        speedSlider.valueProperty().addListener((obs, oldV, newV) -> {
            engine.setSimulationSpeed(newV.doubleValue());
        });

        HBox controlBar = new HBox(10,
                startBtn, stopBtn, resetBtn, typhoonBtn, droughtBtn,
                new Label("速度(x)："), speedSlider);
        controlBar.setPadding(new Insets(10));
        controlBar.setAlignment(Pos.CENTER);

        // 2) 描画・情報エリアの作成
        drawPane = new Pane();
        drawPane.setPrefSize(Ecosystem.WIDTH, Ecosystem.HEIGHT);

        VBox infoBox = new VBox(5, stepLabel, statsLabel);
        infoBox.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setTop(infoBox);
        root.setCenter(drawPane);
        root.setBottom(controlBar);

        // 3) リサイズに追従させるバインド
        drawPane.prefWidthProperty().bind(root.widthProperty());
        drawPane.prefHeightProperty().bind(
                root.heightProperty()
                        .subtract(infoBox.heightProperty())
                        .subtract(controlBar.heightProperty()));

        // 4) シーン・ステージの設定
        Scene scene = new Scene(root, 600, 400);
        scene.getStylesheets().add(
                getClass().getResource("/ecosim/style.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("生態系シミュレーター");
        stage.setResizable(true);
        stage.show();

        // 5) エンジン生成＆GUI登録
        engine = new SimulationEngine();
        engine.setGui(this);

        // 6) スライダーに simSpeed リスナーを登録
        speedSlider.valueProperty().addListener((obs, oldV, newV) -> engine.setSimulationSpeed(newV.doubleValue()));

        // 7) bounds 設定 → 初期化 → 初回描画
        engine.getEcosystem().setBounds(
                drawPane.getWidth(), drawPane.getHeight());
        engine.initialize(50, 20, 8);
        updateDisplay(
                engine.getStepCount(),
                engine.getCounts(),
                engine.getAverageEnergy());

        // 8) PaneサイズをEcosystemに反映
        // さらに、リサイズ時も自動で追従させたいならリスナー登録
        drawPane.widthProperty().addListener((o, oldW, newW) -> engine.getEcosystem().setBounds(
                newW.doubleValue(),
                drawPane.getHeight()));
        drawPane.heightProperty().addListener((o, oldH, newH) -> engine.getEcosystem().setBounds(
                drawPane.getWidth(),
                newH.doubleValue()));

        // 9) AnimationTimer の定義
        timer = new AnimationTimer() {
            private long last = 0;

            @Override
            public void handle(long now) {
                if (last == 0) {
                    last = now;
                    return;
                }
                double dt = (now - last) / 1_000_000_000.0;
                engine.step(dt);
                updateDisplay(
                        engine.getStepCount(),
                        engine.getCounts(),
                        engine.getAverageEnergy());
                last = now;
            }
        };

        // 10) ボタン／スライダーイベント登録
        startBtn.setOnAction(e -> {
            if (!running) {
                timer.start();
                running = true;
            }
        });
        stopBtn.setOnAction(e -> {
            if (running) {
                timer.stop();
                running = false;
            }
        });
        resetBtn.setOnAction(e -> {
            timer.stop();
            running = false;

            // 2) 既存のビューをクリア
            viewMap.clear();
            drawPane.getChildren().clear();

            // 3) 新しいエンジンを生成し GUI をセット
            engine = new SimulationEngine();
            engine.setGui(this);

            // 4) bounds を反映してから初期化
            engine.getEcosystem().setBounds(
                    drawPane.getWidth(), drawPane.getHeight());
            engine.initialize(50, 20, 8);

            updateDisplay(
                    engine.getStepCount(),
                    engine.getCounts(),
                    engine.getAverageEnergy());
        });
        // 台風発生ボタン
        typhoonBtn.setOnAction(e -> {
            double cx = drawPane.getWidth() / 2;
            double cy = drawPane.getHeight() / 2;
            engine.addEvent(new TyphoonEvent(
                    new Point2D(cx, cy), // 台風中心
                    80, // 半径
                    5.0 // 強さ
            ));
        });
        // 干ばつ発生ボタン
        droughtBtn.setOnAction(e -> {
            engine.addEvent(new DroughtEvent(0.3)); // severity=0.3
        });

        // 11) シミュレーション自動開始
        timer.start();
        running = true;
    }

    public void updateDisplay(int step, Map<String, Long> counts, double avgE) {
        // 1) ラベル更新
        stepLabel.setText("Step: " + step);
        statsLabel.setText(String.format(
                "Plants:%d Herb:%d Carn:%d AvgE:%.2f",
                counts.getOrDefault("Plant", 0L),
                counts.getOrDefault("Herbivore", 0L),
                counts.getOrDefault("Carnivore", 0L),
                avgE));

        // 2) 現在モデル上に存在する個体一覧
        List<AbstractOrganism> current = engine
                .getEcosystem()
                .getOrganisms();

        // 3) 死亡個体のフェード＆パーティクル処理
        Iterator<Map.Entry<AbstractOrganism, OrganismView>> it = viewMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<AbstractOrganism, OrganismView> entry = it.next();
            if (!current.contains(entry.getKey())) {
                fadeAndShardEffect(entry.getValue());
                it.remove();
            }
        }

        // 4) 生存個体の追加・移動・エネルギーパルス
        for (AbstractOrganism o : current) {
            OrganismView ov = viewMap.get(o);
            if (ov == null) {
                ov = new OrganismView(o);
                viewMap.put(o, ov);
                drawPane.getChildren().add(ov.getNode());
                ov.playSpawnAnimation();
                ov.updatePositionInstant();
            } else {
                ov.updatePositionAnimated();
                // Energy変化時発光
                // ov.updateEnergyIfChanged();
            }
        }
    }

    /**
     * フェードアウト＋破片パーティクルを飛ばす演出
     */
    private void fadeAndShardEffect(OrganismView ov) {
        Node node = ov.getNode();
        double x = node.getTranslateX();
        double y = node.getTranslateY();

        // フェードアウト
        FadeTransition ft = new FadeTransition(Duration.millis(600), node);
        ft.setToValue(0);
        ft.setOnFinished(evt -> {
            // 破片生成
            for (int i = 0; i < 6; i++) {
                Circle shard = new Circle(2, Color.GRAY);
                shard.setTranslateX(x + node.getBoundsInParent().getWidth() / 2);
                shard.setTranslateY(y + node.getBoundsInParent().getHeight() / 2);
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
    }

    /**
     * JavaFX アプリケーション起動用エントリポイント
     */
    public static void main(String[] args) {
        launch(args);
    }
}