package com.example.ecosim;

import javafx.geometry.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Map;
import java.util.stream.Collectors;

public class Ecosystem {
    // 描画領域（SimulatorGUI のサイズに合わせる）
    public static final double WIDTH  = 400;
    public static final double HEIGHT = 200;

    private final List<AbstractOrganism> organisms = new ArrayList<>();
    private final Random random = new Random();

    /** デフォルトコンストラクタ */
    public Ecosystem() {
    }

    /**
     * 初期個体を生成してリストに追加する
     * @param numPlants     植物の初期個体数
     * @param numHerbivores 草食動物の初期個体数
     * @param numCarnivores 肉食動物の初期個体数
     */
    public void initialize(int numPlants, int numHerbivores, int numCarnivores) {
        // 植物を生成
        for (int i = 0; i < numPlants; i++) {
            Point2D pos = randomPosition();
            double initEnergy = 5.0 + random.nextDouble() * 5.0;
            double growthRate = 0.05 + random.nextDouble() * 0.15;
            organisms.add(new Plant("P" + i, pos, initEnergy, growthRate));
        }

        // 草食動物を生成
        for (int i = 0; i < numHerbivores; i++) {
            Point2D pos = randomPosition();
            double initEnergy = 10.0 + random.nextDouble() * 10.0;
            int speed = 1 + random.nextInt(3);
            double stomachCap = 5.0 + random.nextDouble() * 5.0;
            organisms.add(new Herbivore("H" + i, pos, initEnergy, speed, stomachCap));
        }

        // 肉食動物を生成
        for (int i = 0; i < numCarnivores; i++) {
            Point2D pos = randomPosition();
            double initEnergy = 15.0 + random.nextDouble() * 10.0;
            int speed = 2 + random.nextInt(3);
            double huntingSkill = 0.3 + random.nextDouble() * 0.7;
            organisms.add(new Carnivore("C" + i, pos, initEnergy, speed, huntingSkill));
        }
    }

    /** 更新メソッドはそのまま */
    public void updateEcosystem() {
        // move/grow/reproduce のループ（例）
        List<AbstractOrganism> newborns = new ArrayList<>();
        for (AbstractOrganism o : organisms) {
            o.move();
            o.grow();
            AbstractOrganism child = o.reproduce();
            if (child != null) newborns.add(child);
        }
        organisms.addAll(newborns);
    }

    /** 種類ごとの個体数を返す */
    public Map<String, Long> countByType() {
        return organisms.stream()
            .collect(Collectors.groupingBy(o -> o.getClass().getSimpleName(),
                                           Collectors.counting()));
    }

    /** 平均エネルギーを返す */
    public double averageEnergy() {
        return organisms.stream()
            .mapToDouble(AbstractOrganism::getEnergy)
            .average().orElse(0.0);
    }

    /** ランダムな画面内ポジションを生成 */
    private Point2D randomPosition() {
        return new Point2D(random.nextDouble() * WIDTH,
                           random.nextDouble() * HEIGHT);
    }
}