package com.ksp.khandygo.processing.mpi;

import com.google.common.base.Stopwatch;
import com.ksp.khandygo.geometry.core.Region;
import com.ksp.khandygo.performance.utils.StatisticsCollector;
import com.ksp.khandygo.processing.MCEstimator;
import mpi.MPI;
import java.util.concurrent.TimeUnit;

public class MPIMCCoordinator implements MCEstimator {
  private static final StatisticsCollector MEASURES = StatisticsCollector.newOne();

  private static boolean FIRST_RUN = true;

  private final int thisRank;

  private final int worldSize;

  private final int tag;

  public MPIMCCoordinator(
      final int thisRank,
      final int worldSize,
      final int tag) {
    this.thisRank = thisRank;
    this.worldSize = worldSize;
    this.tag = tag;
  }

  @Override
  public double estimate(final Region regionTiEstimate, final int sampleSize) {
    final Stopwatch measurer = Stopwatch.createStarted();
    final int successfulProbes = doNProbes(regionTiEstimate, sampleSize);
    final int[] buff = new int[worldSize];
    buff[thisRank] = successfulProbes;
    for (int i = 0; i < worldSize; ++i)
      if (i != thisRank) MPI.COMM_WORLD.Recv(buff, i, 1, MPI.INT, i, tag);
    final int successfulProbesOverAllRanks = sum(buff);
    if (FIRST_RUN) FIRST_RUN = false;
    else MEASURES.add(measurer.elapsed(TimeUnit.MILLISECONDS));

    return ((double) successfulProbesOverAllRanks) / (worldSize * sampleSize);
  }

  private int sum(final int[] buff) {
    int acc = 0;
    for (int i = 0; i < worldSize; ++i) {
      acc += buff[i];
    }
    return acc;
  }

  public static void printStatistics() {
    MEASURES.print();
  }
}
