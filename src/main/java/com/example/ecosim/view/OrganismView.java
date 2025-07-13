package com.example.ecosim;

import javafx.animation.TranslateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.effect.ColorAdjust;
import javafx.util.Duration;

public class OrganismView {
    private static final Image PLANT_IMG = new Image(
            OrganismView.class.getResourceAsStream("/ecosim/images/Grass.png"));
    private static final Image HERB_IMG = new Image(OrganismView.class.getResourceAsStream("/ecosim/images/Horse.png"));
    private static final Image CARN_IMG = new Image(OrganismView.class.getResourceAsStream("/ecosim/images/Rion.png"));

    private final AbstractOrganism model;
    private final ImageView view;
    private double lastEnergy;

    public OrganismView(AbstractOrganism model) {
        this.model = model;

        // 種類に応じてアイコンを割り当て
        Image icon;
        if (model instanceof Plant) {
            icon = PLANT_IMG;
        } else if (model instanceof Herbivore) {
            icon = HERB_IMG;
        } else {
            icon = CARN_IMG;
        }

        view = new ImageView(icon);
        view.setFitWidth(24); // 表示サイズを適宜調整
        view.setFitHeight(24);
        view.setMouseTransparent(true); // パン／クリック操作が下に通るように
        lastEnergy = model.getEnergy();
        updatePositionInstant();
    }

    public Node getNode() {
        return view;
    }

    public void updatePositionInstant() {
        view.setTranslateX(model.getPosition().getX() - view.getFitWidth() / 2);
        view.setTranslateY(model.getPosition().getY() - view.getFitHeight() / 2);
    }

    public void updatePositionAnimated() {
        // ターゲット位置
        double targetX = model.getPosition().getX() - view.getFitWidth() / 2;
        double targetY = model.getPosition().getY() - view.getFitHeight() / 2;

        // ランダム混ぜたDurationで、個体ごとに動きのズレを演出
        double durationMs = 500 + Math.random() * 300; // 500～800ms

        TranslateTransition tt = new TranslateTransition(Duration.millis(durationMs), view);
        tt.setToX(targetX);
        tt.setToY(targetY);

        // イーズイン・アウトで自然な動きに
        tt.setInterpolator(Interpolator.EASE_BOTH);

        tt.play();
    }

    public void playSpawnAnimation() {
        // 初期状態を小さく
        view.setScaleX(0);
        view.setScaleY(0);
        // 0→1 にスケールアップ
        ScaleTransition st1 = new ScaleTransition(Duration.millis(300), view);
        st1.setFromX(0);
        st1.setFromY(0);
        st1.setToX(1.5);
        st1.setToY(1.5);
        st1.setInterpolator(Interpolator.EASE_OUT);

        // 次に 1.5→1 へスケールダウン
        ScaleTransition st2 = new ScaleTransition(Duration.millis(200), view);
        st2.setFromX(1.5);
        st2.setFromY(1.5);
        st2.setToX(1);
        st2.setToY(1);
        st2.setInterpolator(Interpolator.EASE_IN);

        // st1 終了後に st2 を再生
        st1.setOnFinished(e -> st2.play());

        st1.play();
    }

    public void playEnergyPulse() {
        // ColorAdjust で輝度を操作
        ColorAdjust adjust = new ColorAdjust();
        view.setEffect(adjust);

        // 0.5→0 の明るさタイムライン
        Timeline tl = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(adjust.brightnessProperty(), 0.5)),
                new KeyFrame(Duration.millis(300),
                        new KeyValue(adjust.brightnessProperty(), 0)));
        tl.setCycleCount(1);
        tl.play();
    }

    public void updateEnergyIfChanged() {
        double current = model.getEnergy();
        // 浮動小数点の厳密比較に不安がある場合は、
        // Math.abs(current - lastEnergy) > 1e-6 のようにする
        if (current != lastEnergy) {
            playEnergyPulse();
            lastEnergy = current;
        }
    }

}