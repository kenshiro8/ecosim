package com.example.ecosim.view;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.example.ecosim.model.AbstractOrganism;
import com.example.ecosim.model.DroughtEvent;
import com.example.ecosim.model.Ecosystem;
import com.example.ecosim.model.Environment;
import com.example.ecosim.model.SimulationEngine;
import com.example.ecosim.model.TyphoonEvent;

import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SimulatorGUI extends Application {
    private Label stepLabel = new Label("Step: 0");
    private Label statsLabel = new Label("Plants:0 Herb:0 Carn:0 AvgE:0.0");
    private Label envLabel = new Label("Season: SPRING  Temp:20.0℃  Hum:0.70");
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
    private Pane organismLayer;
    private Pane overlayPane;
    private String defaultPaneStyle;

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

        // 2) 描画エリア（drawPane）と２つの重ねレイヤーを準備
        drawPane = new Pane();
        drawPane.setPrefSize(Ecosystem.WIDTH, Ecosystem.HEIGHT);

        organismLayer = new Pane();
        organismLayer.prefWidthProperty().bind(drawPane.widthProperty());
        organismLayer.prefHeightProperty().bind(drawPane.heightProperty());

        overlayPane = new Pane();
        overlayPane.setMouseTransparent(true);
        overlayPane.prefWidthProperty().bind(drawPane.widthProperty());
        overlayPane.prefHeightProperty().bind(drawPane.heightProperty());

        // 一度だけ追加。addAll で順序を保証（overlayPane が上）
        drawPane.getChildren().addAll(organismLayer, overlayPane);

        // 元の drawPane スタイルを保存
        defaultPaneStyle = drawPane.getStyle();

        VBox infoBox = new VBox(5, stepLabel, statsLabel, envLabel);
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
        engine.initialize(200, 20, 10);
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
            organismLayer.getChildren().clear();


            // 3) 新しいエンジンを生成し GUI をセット
            engine = new SimulationEngine();
            engine.setGui(this);

            // 4) bounds を反映してから初期化
            engine.getEcosystem().setBounds(
                    drawPane.getWidth(), drawPane.getHeight());
            engine.initialize(200, 20, 10);

            updateDisplay(
                    engine.getStepCount(),
                    engine.getCounts(),
                    engine.getAverageEnergy());
        });
        // 台風発生ボタン
        typhoonBtn.setOnAction(e -> {
            // 発生前の動物数・エネルギー合計を取得
            int beforeCount = engine.getEcosystem().getAllAnimals().size();
            double beforeEnergy = engine.getEcosystem().getAllAnimals().stream()
                    .mapToDouble(o -> o.getEnergy()).sum();

            // イベント登録→即時適用
            engine.addEvent(new TyphoonEvent(
                    new Point2D(drawPane.getWidth() / 2, drawPane.getHeight() / 2),
                    80, 5.0));
            engine.step(0); // dt=0 でイベントのみ適用

            // 発生後の動物数・エネルギー合計を取得
            int afterCount = engine.getEcosystem().getAllAnimals().size();
            double afterEnergy = engine.getEcosystem().getAllAnimals().stream()
                    .mapToDouble(o -> o.getEnergy()).sum();

            // 被害量を計算
            int died = beforeCount - afterCount;
            double energyDropPercent = beforeEnergy == 0
                    ? 0
                    : (beforeEnergy - afterEnergy) / beforeEnergy * 100.0;

            // 背景閃光＆メッセージ
            flashBackground("rgba(100,100,255,0.3)"); // 水色っぽく
            showEventMessage(
                    String.format("台風被害：%d頭消滅  エネルギー：%.0f%%減", died, energyDropPercent));
        });

        // 干ばつ発生ボタン
        droughtBtn.setOnAction(e -> {
            // 発生前の植物エネルギー合計
            double beforeEnergy = engine.getEcosystem().getPlants().stream()
                    .mapToDouble(p -> p.getEnergy()).sum();

            engine.addEvent(new DroughtEvent(0.3));
            engine.step(0);

            // 発生後の植物エネルギー合計
            double afterEnergy = engine.getEcosystem().getPlants().stream()
                    .mapToDouble(p -> p.getEnergy()).sum();

            double dropPercent = beforeEnergy == 0
                    ? 0
                    : (beforeEnergy - afterEnergy) / beforeEnergy * 100.0;

            flashBackground("rgba(255,200,100,0.3)"); // 暖色に
            showEventMessage(
                    String.format("植物エネルギー：%.0f%%減", dropPercent));
        });

        // 11) シミュレーション自動開始
        timer.start();
        running = true;
    }

    public void updateDisplay(int step, Map<String, Long> counts, double avgE) {
        // 0) 環境情報を取得してラベル更新 ← 追加
        Environment env = engine.getEcosystem().getEnvironment();
        envLabel.setText(String.format(
                "季節: %s  気温: %.1f℃  Hum: %.2f",
                env.getSeason(), env.getTemperature(), env.getHumidity()));

        // 1) ラベル更新
        stepLabel.setText("Step: " + step);
        statsLabel.setText(String.format(
                "植物:%d 草食動物:%d 肉食動物:%d 平均エネルギー:%.2f",
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
                organismLayer.getChildren().add(ov.getNode());
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
     * 背景色を一時的に変えて、2秒後に元に戻す
     */
    private void flashBackground(String flashCssColor) {
        // ▲ ここでオーバーレイを最前面にする ▲
        overlayPane.toFront();

        drawPane.setStyle("-fx-background-color: " + flashCssColor + ";");

        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(ev -> {
            // 元のスタイルに戻す
            drawPane.setStyle(defaultPaneStyle);
            // 念のためオーバーレイを最前面に
            overlayPane.toFront();
        });
        pause.play();
    }

    /**
     * 画面中央にメッセージをフェードイン／フェードアウトで表示
     */
    private void showEventMessage(String message) {
        // overlayPane を最前面に
        overlayPane.toFront();

        Label msg = new Label(message);
        // ラベル自体も前面に
        msg.toFront();
        msg.setStyle(
                "-fx-font-size:24px; -fx-text-fill:white;"
                        + "-fx-background-color:rgba(0,0,0,0.6);"
                        + "-fx-padding:10px; -fx-background-radius:5px;");

        // 中央配置バインド
        msg.layoutXProperty().bind(
                overlayPane.widthProperty().subtract(msg.widthProperty()).divide(2));
        msg.layoutYProperty().bind(
                overlayPane.heightProperty().subtract(msg.heightProperty()).divide(2));

        overlayPane.getChildren().add(msg);

        // フェードイン→ホールド→フェードアウト
        FadeTransition ftIn = new FadeTransition(Duration.millis(300), msg);
        ftIn.setFromValue(0);
        ftIn.setToValue(1);

        PauseTransition hold = new PauseTransition(Duration.seconds(1.4));

        FadeTransition ftOut = new FadeTransition(Duration.millis(300), msg);
        ftOut.setFromValue(1);
        ftOut.setToValue(0);
        ftOut.setOnFinished(e -> overlayPane.getChildren().remove(msg));

        new SequentialTransition(ftIn, hold, ftOut).play();
    }

    /**
     * JavaFX アプリケーション起動用エントリポイント
     */
    public static void main(String[] args) {
        launch(args);
    }
}