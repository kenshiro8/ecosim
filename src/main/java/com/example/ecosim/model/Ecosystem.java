package com.example.ecosim.model;

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
    private Environment environment;
    private final Random random = new Random();

    public Ecosystem() {
        // organisms, random はフィールド初期化子で生成済み
        this.environment = new Environment(20.0, 0.7, Season.SPRING);
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

        // 草食動物を生成して GoalDirectedBehavior をセット
        for (int i = 0; i < numHerbivores; i++) {
            Point2D pos = randomPosition();
            double initEnergy = 200.0 + random.nextDouble() * 10.0;
            int speed = 500 + random.nextInt(3);
            double stomachCap = 5.0 + random.nextDouble() * 5.0;

            Herbivore h = new Herbivore(
                    "H" + i,
                    pos,
                    initEnergy,
                    speed,
                    stomachCap,
                    this);
            // 目標：常に最新の植物リストを返す Supplier／速度は speed
            h.setMovementBehavior(
                    new GoalDirectedBehavior(
                            () -> this.getPlants(),
                            speed));
            organisms.add(h);
        }

        // 肉食動物を生成して草食動物を目標に設定
        for (int i = 0; i < numCarnivores; i++) {
            Point2D pos = randomPosition();
            double initEnergy = 150.0 + random.nextDouble() * 10.0;
            int speed = 500 + random.nextInt(3);
            double huntingSkill = 0.3 + random.nextDouble() * 0.7;

            Carnivore c = new Carnivore(
                    "C" + i,
                    pos,
                    initEnergy,
                    speed,
                    huntingSkill,
                    this);
            // 草食動物リストを常に最新で参照し、speedで移動
            c.setMovementBehavior(
                    new GoalDirectedBehavior(
                            () -> this.getHerbivores(),
                            speed));
            organisms.add(c);
        }
    }

    public void applyTyphoon(Typhoon typhoon) {
        for (AbstractOrganism o : organisms) {
            if (typhoon.hits(o)) {
                typhoon.applyEffect(o);
            }
        }
    }

    public void updateEcosystem(double dt) {
        List<AbstractOrganism> newborns = new ArrayList<>();
        List<Plant> eatenPlants = new ArrayList<>(); // ①追加：食べられた植物を収集
        Iterator<AbstractOrganism> it = organisms.iterator();

        while (it.hasNext()) {
            AbstractOrganism o = it.next();
            o.move(dt);
            o.grow(dt);

            // ①草食動物なら摂食処理 → 食べたPlantをeatenPlantsへ
            if (o instanceof Herbivore) {
                Plant prey = ((Herbivore) o).eat(getPlants());
                if (prey != null) {
                    eatenPlants.add(prey);
                }
            }

            // エネルギー切れで死亡
            if (o.getEnergy() <= 0) {
                it.remove();
                continue;
            }

            // 繁殖判定
            AbstractOrganism child = o.reproduce(dt);
            if (child != null) {
                newborns.add(child);
            }
        }

        // ②ループ後に食べられた植物を一気に削除
        organisms.removeAll(eatenPlants);

        // 新生個体を追加
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

    public Environment getEnvironment() {
        return environment;
    }

    public List<Plant> getPlants() {
        return organisms.stream()
                .filter(o -> o instanceof Plant)
                .map(o -> (Plant) o)
                .collect(Collectors.toList());
    }

    public List<Herbivore> getHerbivores() {
        return organisms.stream()
                .filter(o -> o instanceof Herbivore)
                .map(o -> (Herbivore) o)
                .collect(Collectors.toList());
    }

    public List<Carnivore> getCarnivores() {
        return organisms.stream()
                .filter(o -> o instanceof Carnivore)
                .map(o -> (Carnivore) o)
                .collect(Collectors.toList());
    }

    public List<Animal> getAllAnimals() {
        return organisms.stream()
                .filter(o -> o instanceof Animal)
                .map(o -> (Animal) o)
                .collect(Collectors.toList());
    }

}