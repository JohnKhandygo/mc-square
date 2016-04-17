package com.ksp.khandygo.geometry.core;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.io.Serializable;

@EqualsAndHashCode
@ToString
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
}
