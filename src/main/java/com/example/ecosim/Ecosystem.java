package com.example.ecosim;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import javafx.geometry.Point2D;

public class Ecosystem {
    /** 画面サイズのデフォルト値／GUIからの更新結果を保持するstaticフィールド */
    public static double WIDTH = 400;
    public static double HEIGHT = 200;

    // 描画領域（SimulatorGUI のサイズに合わせる）
    private double width = WIDTH;
    private double height = HEIGHT;

    private final List<AbstractOrganism> organisms = new ArrayList<>();
    private final Random random = new Random();

    public Ecosystem() {
        // organisms, random はフィールド初期化子で生成済み
    }

    /** GUI から呼ばれる：描画領域サイズを更新 */
    public void setBounds(double width, double height) {
        this.width = width;
        this.height = height;
        WIDTH = width;
        HEIGHT = height;
    }

    // 現在の個体リストを返す
    public List<AbstractOrganism> getOrganisms() {
        return organisms;
    }

    /**
     * 初期個体を生成してリストに追加する
     * 
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

    public void applyTyphoon(Typhoon typhoon) {
        for (AbstractOrganism o : organisms) {
            if (typhoon.hits(o)) {
                typhoon.applyEffect(o);
            }
        }
    }

    public void updateEcosystem() {
        List<AbstractOrganism> newborns = new ArrayList<>();
        Iterator<AbstractOrganism> it = organisms.iterator();
        while (it.hasNext()) {
            AbstractOrganism o = it.next();
            o.move();
            o.grow();
            if (o.getEnergy() <= 0) {
                it.remove(); // 死亡
                continue;
            }
            AbstractOrganism child = o.reproduce();
            if (child != null)
                newborns.add(child);
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
        return new Point2D(
                random.nextDouble() * width,
                random.nextDouble() * height);
    }
}