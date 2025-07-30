package com.example.ecosim.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PopulationManager {
    /** 生存個体の移動・成長・繁殖・死亡判定 */
    public void update(Ecosystem eco, double dt) {
        List<AbstractOrganism> newborns = new ArrayList<>();
        Iterator<AbstractOrganism> it = eco.getOrganisms().iterator();

        while (it.hasNext()) {
            AbstractOrganism o = it.next();
            o.move(dt);      // 位置を更新
            o.grow(dt);      // エネルギー消費など

            // 草食動物なら食べる処理
            if (o instanceof Herbivore) {
                ((Herbivore) o).eat(eco.getOrganisms());
            }

            if (o.getEnergy() <= 0) {
                it.remove(); // エネルギー切れで死
                continue;
            }

            // 繁殖して子供が生まれたら一時リストに追加
            AbstractOrganism child = o.reproduce(dt);
            if (child != null) {
                newborns.add(child);
            }
        }

        // 最後にまとめて追加（イテレータ操作中に追加しないため）
        eco.getOrganisms().addAll(newborns);
    }
}
