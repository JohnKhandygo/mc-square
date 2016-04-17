package com.ksp.khandygo.geometry;

import com.google.common.collect.Range;
import com.ksp.khandygo.geometry.core.Point;
import com.ksp.khandygo.geometry.core.Region;
import java.util.List;

public class CompositeRegion<T extends Region> implements Region {
  protected final List<T> regions;

  public CompositeRegion(final List<T> regions) {
    this.regions = regions;
  }

  @Override
  public boolean contains(final Point point) {
    for (final T region : regions)
      if (region.contains(point)) return true;
    return false;
    //return regions.stream().anyMatch(r -> r.contains(point));
  }

  @Override
  public boolean contains(final Region region) {
    return regions.stream().anyMatch(r -> r.contains(region));
  }

  @Override
  public Range<Double> xprojection() {
    final double minx = regions.stream()
        .map(Region::xprojection)
        .mapToDouble(Range::lowerEndpoint)
        .min()
        .getAsDouble();
    final double maxx = regions.stream()
        .map(Region::xprojection)
        .mapToDouble(Range::upperEndpoint)
        .max()
        .getAsDouble();
    return Range.closed(minx, maxx);
  }

  @Override
  public Range<Double> yprojection() {
    final double miny = regions.stream()
        .map(Region::yprojection)
        .mapToDouble(Range::lowerEndpoint)
        .min()
        .getAsDouble();
    final double maxy = regions.stream()
        .map(Region::yprojection)
        .mapToDouble(Range::upperEndpoint)
        .max()
        .getAsDouble();
    return Range.closed(miny, maxy);
  }
}
