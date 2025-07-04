package com.example.ecosim;

import com.example.ecosim;
import java.util.*;
import java.util.stream.*;

public class Ecosystem {
    private List<AbstractOrganism> organisms = new ArrayList<>();
    private Environment env;

    public void updateEcosystem() {
        // move/grow/reproduce を各個体で実行
    }

    // タイプ別個体数取得
    public Map<String, Long> countByType() {
        return organisms.stream()
            .collect(Collectors.groupingBy(o -> o.getClass().getSimpleName(),
                     Collectors.counting()));
    }

    // 平均エネルギー
    public double averageEnergy() {
        return organisms.stream()
            .mapToDouble(AbstractOrganism::getEnergy)
            .average().orElse(0.0);
    }
}