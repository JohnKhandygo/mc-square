package com.ksp.khandygo.geometry;

import com.google.common.collect.Range;
import com.ksp.khandygo.geometry.core.Figure;
import com.ksp.khandygo.geometry.core.Point;
import com.ksp.khandygo.geometry.core.Region;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class Rectangle implements Figure {

  private final Point ll;

  private final Point ur;

  Rectangle(final Point ll, final Point ur) {
    this.ll = ll;
    this.ur = ur;
  }

  public Point lowerLeft() {
    return ll;
  }

  @Override
  public double square() {
    return width() * height();
  }

  public double width() {
    return ur.x() - ll.x();
  }

  public double height() {
    return ur.y() - ll.y();
  }

  @Override
  public boolean contains(final Point p) {
    return xprojection().contains(p.x()) && yprojection().contains(p.y());
  }

  @Override
  public Range<Double> xprojection() {
    return Range.closed(ll.x(), ur.x());
  }

  @Override
  public Range<Double> yprojection() {
    return Range.closed(ll.y(), ur.y());
  }

  @Override
  public boolean contains(final Region region) {
    throw new UnsupportedOperationException();
  }

  public static Rectangle of(final Point ll, final double w, final double h) {
    return new Rectangle(ll, new Point(ll.x() + w, ll.y() + h));
  }
}
