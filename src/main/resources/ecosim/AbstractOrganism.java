package com.example.ecosim;

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
     * @param id        個体ID
     * @param pos       初期座標
     * @param initEnergy 初期エネルギー
     */
    public AbstractOrganism(String id, Point2D pos, double initEnergy) {
        this.id = id;
        this.position = pos;
        this.energy = initEnergy;
        this.age = 0;
    }

    /**
     * 個体の移動ロジックをサブクラスで実装
     */
    public abstract void move();

    /**
     * 年を取り、エネルギーを減衰させる
     */
    public void grow() {
        age++;
        energy -= 0.1;
    }

    /**
     * 繁殖ロジックをサブクラスで実装し、
     * 新個体を返す（繁殖しなければ null）
     */
    public abstract AbstractOrganism reproduce();

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
}