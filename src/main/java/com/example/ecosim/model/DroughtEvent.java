// src/main/java/com/example/ecosim/model/DroughtEvent.java
package com.example.ecosim.model;

public class DroughtEvent implements EnvironmentalEvent {
  private final double severity; // 0.0〜1.0

  public DroughtEvent(double severity) {
    this.severity = severity;
  }

  @Override
  public void applyEvent(Ecosystem eco) {
    eco.getOrganisms().stream()
        .filter(o -> o instanceof Plant)
        .map(o -> (Plant) o)
        .forEach(p -> p.applyDrought(severity));
  }
}