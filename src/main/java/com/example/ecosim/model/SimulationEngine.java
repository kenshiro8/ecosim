package com.example.ecosim.model;

import com.example.ecosim.view.SimulatorGUI;

import java.util.Map;

public class SimulationEngine {
    private final Ecosystem ecosystem;
    private final EnvironmentManager envMgr;
    private final EventManager eventMgr;
    private final PopulationManager popMgr;
    private SimulatorGUI gui;
    private int stepCount = 0;
    private Map<String, Long> lastCounts;
    private double lastAverageEnergy = 0.0;
    private double simSpeed = 1.0;

    /** デフォルトコンストラクタ（初期化は GUI で行う） */
    public SimulationEngine() {
        this.ecosystem = new Ecosystem();
        this.envMgr = new EnvironmentManager(ecosystem.getEnvironment());
        this.eventMgr = new EventManager();
        this.popMgr = new PopulationManager();
    }

    public void setSimulationSpeed(double speed) {
        this.simSpeed = speed;
    }

    /** 初期個体数を指定して Ecosystem を初期化し、統計をリセット */
    public void initialize(int numPlants, int numHerbivores, int numCarnivores) {
        ecosystem.initialize(numPlants, numHerbivores, numCarnivores);
        stepCount = 0;
        updateStatistics();
    }

    public void setGui(SimulatorGUI gui) {
        this.gui = gui;
    }

    /**
     * 1ステップだけ進めて統計を更新する
     */
    public void step(double dt) {
        double sdt = dt * simSpeed;
        envMgr.update(sdt);
        eventMgr.applyEvents(ecosystem);
        popMgr.update(ecosystem, sdt);
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

    public void addEvent(EnvironmentalEvent e) {
        eventMgr.addEvent(e);
    }

}