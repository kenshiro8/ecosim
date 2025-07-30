package com.example.ecosim.model;

import java.util.Queue;
import java.util.LinkedList;

public class EventManager {
    private final Queue<EnvironmentalEvent> queue = new LinkedList<>();

    public void addEvent(EnvironmentalEvent e) {
        queue.offer(e);
    }

    /** 毎ステップ呼び出し */
    public void applyEvents(Ecosystem ecosystem) {
        while (!queue.isEmpty()) {
            queue.poll().applyEvent(ecosystem);
        }
    }
}