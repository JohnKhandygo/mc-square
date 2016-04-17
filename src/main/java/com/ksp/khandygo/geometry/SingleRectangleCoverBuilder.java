package com.ksp.khandygo.geometry;

import com.google.common.collect.Range;
import com.ksp.khandygo.geometry.core.Point;
import com.ksp.khandygo.geometry.core.Region;
import java.util.List;

public class SingleRectangleCoverBuilder {
  public <T extends Region> Rectangle cover(final List<T> regionsToEnclose) {
    return cover(new CompositeRegion<>(regionsToEnclose));
  }

  public Rectangle cover(final Region regionToEnclose) {
    final Range<Double> xprojection = regionToEnclose.xprojection();
    final Range<Double> yprojection = regionToEnclose.yprojection();
    return new Rectangle(
        new Point(xprojection.lowerEndpoint(), yprojection.lowerEndpoint()),
        new Point(xprojection.upperEndpoint(), yprojection.upperEndpoint()));
  }
}
