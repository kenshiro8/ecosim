package com.example.ecosim.model;

/**
 * 環境イベント用インタフェース
 */
public interface EnvironmentalEvent {
    void applyEvent(Ecosystem ecosystem);
}