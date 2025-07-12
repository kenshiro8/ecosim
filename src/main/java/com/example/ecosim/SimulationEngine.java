package com.example.ecosim;

import java.util.Map;

public class SimulationEngine {
    private final Ecosystem ecosystem;
    private SimulatorGUI gui;
    private int stepCount = 0;
    private Map<String, Long> lastCounts;
    private double lastAverageEnergy = 0.0;

    /** 初期個体数を受け取るコンストラクタ */
    public SimulationEngine(int numPlants,
            int numHerbivores,
            int numCarnivores) {
        this.ecosystem = new Ecosystem();
        // 初期化ロジックを呼び出し
        ecosystem.initialize(numPlants, numHerbivores, numCarnivores);
        // 最初の統計値を作る
        updateStatistics();
    }

    /** デフォルト呼び出しも用意 */
    public SimulationEngine() {
        this(30, 10, 5); // デフォルト：植物30、草食10、肉食5
    }

    public void setGui(SimulatorGUI gui) {
        this.gui = gui;
    }

    /**
     * 1ステップだけ進めて統計を更新する
     */
    public void step() {
        ecosystem.updateEcosystem();
        stepCount++;
        updateStatistics();
    }

    /** 内部で最新の個体数・平均エネルギーを算出して lastCounts/lastAverageEnergy に保持 */
    private void updateStatistics() {
        lastCounts = ecosystem.countByType();
        lastAverageEnergy = ecosystem.averageEnergy();
    }

    /** GUI から呼び出す現在ステップ数 */
    public int getStepCount() {
        return stepCount;
    }

    /** GUI から呼び出す最新の個体数マップ */
    public Map<String, Long> getCounts() {
        return lastCounts;
    }

    /** GUI から呼び出す最新の平均エネルギー */
    public double getAverageEnergy() {
        return lastAverageEnergy;
    }

    /**
     * GUI層が個体一覧を取得するためのメソッド
     */
    public Ecosystem getEcosystem() {
        return ecosystem;
    }
}