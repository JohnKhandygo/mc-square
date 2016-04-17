package com.ksp.khandygo.geometry;

import com.google.common.collect.Range;
import static com.ksp.khandygo.geometry.Distance.*;
import com.ksp.khandygo.geometry.core.Figure;
import com.ksp.khandygo.geometry.core.Point;
import com.ksp.khandygo.geometry.core.Region;
import static java.lang.Math.*;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class Circle implements Figure {

  private final Point centre;

  private final double radius2;

  public Circle(final Point centre, final double radius) {
    this.centre = centre;
    this.radius2 = radius * radius;
  }

  @Override
  public double square() {
    return PI * radius2;
  }

  @Override
  public boolean contains(final Point p) {
    return distance2Between(centre, p) <= radius2;
  }

  @Override
  public boolean contains(final Region region) {
    if (region instanceof Circle) {
      final Circle c = (Circle) region;
      final double d = distanceBetween(centre, c.centre());
      return d + c.radius() <= radius();
    } else {
      throw new UnsupportedOperationException();
    }
  }

  public Point centre() {
    return centre;
  }

  public double radius() {
    return sqrt(radius2);
  }

  @Override
  public Range<Double> xprojection() {
    return Range.closed(centre.x() - radius(), centre.x() + radius());
  }

  @Override
  public Range<Double> yprojection() {
    return Range.closed(centre.y() - radius(), centre.y() + radius());
  }

  public boolean intersectsWith(final Circle c) {
    return distanceBetween(centre, c.centre()) <= radius() + c.radius();
  }
}
