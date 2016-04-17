package com.ksp.khandygo.processing.sequential;

import com.ksp.khandygo.geometry.core.Region;
import com.ksp.khandygo.processing.MCEstimator;

public class SequentialMCEstimator implements MCEstimator {

  @Override
  public double estimate(final Region regionTiEstimate, final int sampleSize) {
    final int successfulProbes = doNProbes(regionTiEstimate, sampleSize);
    return ((double) successfulProbes) / sampleSize;
  }
}
