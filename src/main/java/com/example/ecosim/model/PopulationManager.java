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
            o.move(dt);
            o.grow(dt);
            if (o.getEnergy() <= 0) {
                it.remove();
                continue;
            }
            AbstractOrganism child = o.reproduce(dt);
            if (child != null) newborns.add(child);
        }
        eco.getOrganisms().addAll(newborns);
    }
}