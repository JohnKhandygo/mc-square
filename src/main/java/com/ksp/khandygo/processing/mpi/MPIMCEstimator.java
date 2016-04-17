package com.ksp.khandygo.processing.mpi;

import com.ksp.khandygo.geometry.core.Region;
import com.ksp.khandygo.processing.MCEstimator;
import mpi.MPI;

public class MPIMCEstimator implements MCEstimator {

  private final int coordinatorRank;

  private final int tag;

  public MPIMCEstimator(final int coordinatorRank, final int tag) {
    this.coordinatorRank = coordinatorRank;
    this.tag = tag;
  }

  @Override
  public double estimate(final Region regionTiEstimate, final int sampleSize) {
    final int successfulProbes = doNProbes(regionTiEstimate, sampleSize);
    final int[] buff = new int[]{successfulProbes};
    MPI.COMM_WORLD.Isend(buff, 0, 1, MPI.INT, coordinatorRank, tag);
    return ((double) successfulProbes) / sampleSize;
  }
}