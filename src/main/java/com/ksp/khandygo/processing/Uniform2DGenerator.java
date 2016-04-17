package com.ksp.khandygo.processing;

import com.ksp.khandygo.geometry.Rectangle;
import com.ksp.khandygo.geometry.core.Point;
import java.util.Random;

public interface Uniform2DGenerator {

  Point next();

  static Uniform2DGenerator in(final Rectangle r) {
    final Random g = new Random();
    return () -> {
      final double x = g.nextDouble() * r.width() + r.lowerLeft().x();
      final double y = g.nextDouble() * r.height() + r.lowerLeft().y();
      return new Point(x, y);
    };
  }
}