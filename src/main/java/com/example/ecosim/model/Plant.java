package com.example.ecosim.model;

import javafx.geometry.Point2D;

/**
 * 植物モデル（ロジスティック成長＋繁殖）
 */
public class Plant extends AbstractOrganism {
    private double growthRate; // 自然増殖レート
    private double carryingCapacity; // キャパシティ（エネルギー上限）
    private double reproductionThreshold; // 繁殖開始エネルギー閾値

    /**
     * ４引数版コンストラクタ（既存の生成ループをそのまま使いたい場合）
     * carryingCapacity と reproductionThreshold にはデフォルト値を設定
     */
    public Plant(
            String id,
            Point2D pos,
            double initEnergy,
            double growthRate) {
        super(id, pos, initEnergy);
        this.growthRate = growthRate;
        this.carryingCapacity = 200.0; // デフォルト値
        this.reproductionThreshold = 50.0; // デフォルト閾値
    }

    /**
     * ６引数版コンストラクタ（全パラメータを外部から注入したい場合）
     */
    public Plant(
            String id,
            Point2D pos,
            double initEnergy,
            double growthRate,
            double carryingCapacity,
            double reproductionThreshold) {
        super(id, pos, initEnergy);
        this.growthRate = growthRate;
        this.carryingCapacity = carryingCapacity;
        this.reproductionThreshold = reproductionThreshold;
    }

    /**
     * reproduce メソッドの完全実装。
     * ▶ ロジスティック成長モデルで energy を更新し、
     * ▶ reproductionThreshold を超えたら分割して子を生成。
     */
    @Override
    public AbstractOrganism reproduce(double dt) {
        // 1) ロジスティック成長：dE = r * E * (1 - E/K) * dt
        energy += growthRate * energy * (1 - energy / carryingCapacity) * dt;

        // 2) 繁殖判定：エネルギーが閾値以上なら子を1体生成
        if (energy >= reproductionThreshold) {
            double childEnergy = energy / 2.0;
            energy /= 2.0;

            // 子の位置は親の周辺ランダム
            Point2D childPos = this.position
                    .add(Math.random() - 0.5, Math.random() - 0.5);

            // 子インスタンスを生成して返却
            return new Plant(
                    this.id + "-p", // 新規ID
                    childPos,
                    childEnergy,
                    this.growthRate,
                    this.carryingCapacity,
                    this.reproductionThreshold);
        }
        // 繁殖しない場合は null
        return null;
    }

    public void applyDrought(double severity) {
        // 成長率を一時的に下げたり、エネルギーを減少させる
        energy -= severity * 2.0;
    }
}