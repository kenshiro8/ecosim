package com.example.ecosim;

import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class OrganismView {
    private static final Image PLANT_IMG =
        new Image(OrganismView.class.getResourceAsStream("/ecosim/images/Grass.png"));
    private static final Image HERB_IMG =
        new Image(OrganismView.class.getResourceAsStream("/ecosim/images/Horse.png"));
    private static final Image CARN_IMG =
        new Image(OrganismView.class.getResourceAsStream("/ecosim/images/Rion.png"));

    private final AbstractOrganism model;
    private final ImageView view;

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
        view.setFitWidth(24);   // 表示サイズを適宜調整
        view.setFitHeight(24);
        view.setMouseTransparent(true);  // パン／クリック操作が下に通るように

        updatePositionInstant();
    }

    public Node getNode() {
        return view;
    }

    public void updatePositionInstant() {
        view.setTranslateX(model.getPosition().getX() - view.getFitWidth()/2);
        view.setTranslateY(model.getPosition().getY() - view.getFitHeight()/2);
    }

    public void updatePositionAnimated() {
        TranslateTransition tt = new TranslateTransition(Duration.millis(800), view);
        tt.setToX(model.getPosition().getX() - view.getFitWidth()/2);
        tt.setToY(model.getPosition().getY() - view.getFitHeight()/2);
        tt.play();
    }
}