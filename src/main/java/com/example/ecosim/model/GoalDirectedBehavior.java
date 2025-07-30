// src/main/java/com/example/ecosim/model/GoalDirectedBehavior.java
package com.example.ecosim.model;

import javafx.geometry.Point2D;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.Comparator;

public class GoalDirectedBehavior implements MovementBehavior {
  // Supplier<? extends List<? extends AbstractOrganism>> に変更
  private final Supplier<? extends List<? extends AbstractOrganism>> targetsSupplier;
  private final double speed;

  public GoalDirectedBehavior(
      Supplier<? extends List<? extends AbstractOrganism>> targetsSupplier,
      double speed) {
    this.targetsSupplier = targetsSupplier;
    this.speed = speed;
  }

  @Override
  public Point2D computeNextPosition(AbstractOrganism org, double dt) {
    List<? extends AbstractOrganism> targets = targetsSupplier.get();
    if (targets.isEmpty()) {
      return org.getPosition();
    }

    // Optional を受け取る
    Optional<? extends AbstractOrganism> opt = targets.stream()
        .min(Comparator.comparingDouble(
            a -> a.getPosition().distance(org.getPosition())));

    // orElse の代わりに isPresent()/get() で分岐
    AbstractOrganism nearest = opt.isPresent()
        ? opt.get()
        : org;

    Point2D dir = nearest.getPosition()
        .subtract(org.getPosition())
        .normalize();
    Point2D next = org.getPosition().add(dir.multiply(speed * dt));

    // 画面外に出ないようクランプ
    double x = Math.min(Math.max(next.getX(), 0), Ecosystem.WIDTH);
    double y = Math.min(Math.max(next.getY(), 0), Ecosystem.HEIGHT);
    return new Point2D(x, y);
  }
}