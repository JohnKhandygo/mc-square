package com.ksp.khandygo.processing;

import com.ksp.khandygo.geometry.Rectangle;
import com.ksp.khandygo.geometry.SingleRectangleCoverBuilder;
import com.ksp.khandygo.geometry.core.Region;

public interface MCEstimator {

  double estimate(final Region regionTiEstimate, final int sampleSize);

  default int doNProbes(
      final Region regionTiEstimate,
      final int sampleSize) {
    int successfulProbes = 0;
    final Rectangle r = new SingleRectangleCoverBuilder()
        .cover(regionTiEstimate);
    final Uniform2DGenerator g = Uniform2DGenerator.in(r);
    for (int i = 0; i < sampleSize; ++i)
      if (regionTiEstimate.contains(g.next())) ++successfulProbes;
    return successfulProbes;
  }
}
