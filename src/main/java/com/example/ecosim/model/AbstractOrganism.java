package com.example.ecosim.model;

import javafx.geometry.Point2D;

/**
 * すべての生物に共通するフィールドと振る舞いを定義する抽象クラス
 */
public abstract class AbstractOrganism {

    protected String id;
    protected Point2D position;
    protected double energy;
    protected int age;

    /**
     * コンストラクタ
     * 
     * @param id         個体ID
     * @param pos        初期座標
     * @param initEnergy 初期エネルギー
     */

    // --- Behavior 連携用フィールド ---
    private MovementBehavior movementBehavior;

    /** move() 時に呼ぶ Behavior をセット */
    public void setMovementBehavior(MovementBehavior mb) {
        this.movementBehavior = mb;
    }

    public AbstractOrganism(String id, Point2D pos, double initEnergy) {
        this.id = id;
        this.position = pos;
        this.energy = initEnergy;
        this.age = 0;
    }

    /**
     * 移動は MovementBehavior に委譲
     */

    public final void move(double dt) {
        if (movementBehavior != null) {
            // 旧: movementBehavior.computeNextPosition(this, dt);
            this.position = movementBehavior.computeNextPosition(this, dt);
        }
    }

    /** dt(秒)分だけ年を取り、基礎代謝分エネルギーを減衰させる */
    public void grow(double dt) {
        // age は「秒」か「ステップ数」か設計次第ですが、ここでは秒数を足してもOK
        age += dt;
        // 基礎消費0.1 エネルギー/秒 を使うなら
        energy -= 0.1 * dt;
    }

    /**
     * 繁殖ロジックをサブクラスで実装し、
     * 新個体を返す（繁殖しなければ null）
     */
    public abstract AbstractOrganism reproduce(double dt);

    /**
     * 現在のエネルギー量を取得
     */
    public double getEnergy() {
        return energy;
    }

    /**
     * 現在の座標を取得
     */
    public Point2D getPosition() {
        return position;
    }

    /**
     * 個体のIDを取得
     */
    public String getId() {
        return id;
    }
    public void setPosition(Point2D newPosition) {
        this.position = newPosition;
    }
}