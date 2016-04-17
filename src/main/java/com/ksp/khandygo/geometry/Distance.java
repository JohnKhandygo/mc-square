package com.ksp.khandygo.geometry;

import com.ksp.khandygo.geometry.core.Point;
import static java.lang.Math.*;

public class Distance {

  public static double distanceBetween(final Point p1, final Point p2) {
    return sqrt(distance2Between(p1, p2));
  }

  static double distance2Between(final Point p1, final Point p2) {
    return pow((p1.x() - p2.x()), 2) + pow((p1.y() - p2.y()), 2);
  }
}
