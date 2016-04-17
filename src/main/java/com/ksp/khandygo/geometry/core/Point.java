package com.ksp.khandygo.geometry.core;

import java.io.Serializable;
import java.util.Objects;

public class Point implements Serializable {

  private final double x;

  private final double y;

  public Point(final double x, final double y) {
    this.x = x;
    this.y = y;
  }

  public double x() {
    return x;
  }

  public double y() {
    return y;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Point)) {
      return false;
    }
    final Point point = (Point) o;
    return Double.compare(point.x, x) == 0 &&
        Double.compare(point.y, y) == 0;
  }

  @Override
  public String toString() {
    return "Point{" +
        "x=" + x +
        ", y=" + y +
        '}';
  }
}
