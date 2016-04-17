package com.ksp.khandygo.processing;

import com.ksp.khandygo.geometry.Circle;
import com.ksp.khandygo.geometry.CompositeRegion;
import com.ksp.khandygo.geometry.core.Figure;
import java.util.List;

public class FigureOfCircles extends CompositeRegion<Circle> implements Figure {

  public FigureOfCircles(final List<Circle> regions) {
    super(regions);
  }

  @Override
  public double square() {
    return regions.stream().mapToDouble(Circle::square).sum();
  }
}
