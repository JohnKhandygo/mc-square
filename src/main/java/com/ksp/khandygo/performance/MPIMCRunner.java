package com.ksp.khandygo.performance;

import com.ksp.khandygo.geometry.core.Region;
import com.ksp.khandygo.performance.utils.InputProvider;
import com.ksp.khandygo.performance.utils.SetupParametersFormatter;
import com.ksp.khandygo.processing.mpi.MPIMCCoordinator;
import com.ksp.khandygo.processing.mpi.MPIMCEstimator;
import mpi.MPI;
import java.util.List;

public class MPIMCRunner {

  public static void main(final String[] args)
  throws Exception {
    MPI.Init(args);
    final int coordinatorRank = 0;
    final int tag = 0;
    final int thisRank = MPI.COMM_WORLD.Rank();
    final int[] samplesBuff = new int[1];

    final List<Region> inputs = InputProvider.feededFrom(args[4]).provide();
    final Region[] regionBuff = new Region[inputs.size()];
    setUpAndShare(args, coordinatorRank, thisRank, samplesBuff, inputs, regionBuff);

    MPI.COMM_WORLD.Barrier();

    for (int i = 0; i < regionBuff.length; ++i) {
      doEstimation(coordinatorRank, tag, thisRank, regionBuff[i], samplesBuff);
    }

    if (thisRank == coordinatorRank) {
      MPIMCCoordinator.printStatistics();
    }
    MPI.Finalize();
  }

  private static void setUpAndShare(
      final String[] args,
      final int coordinatorRank,
      final int thisRank,
      final int[] samplesBuff,
      final List<Region> regions,
      final Region[] regionBuff) {
    if (thisRank == coordinatorRank) {
      regions.toArray(regionBuff);
      MPI.COMM_WORLD.Bcast(regionBuff, 0, regionBuff.length, MPI.OBJECT, coordinatorRank);
      final int worldSize = MPI.COMM_WORLD.Size();
      final int mcSampleSize = Integer.parseInt(args[3]);
      samplesBuff[0] = mcSampleSize / worldSize;

      MPI.COMM_WORLD.Bcast(samplesBuff, 0, 1, MPI.INT, coordinatorRank);

      SetupParametersFormatter.print(args[4], regions.size(), mcSampleSize, worldSize);
    } else {
      MPI.COMM_WORLD.Bcast(regionBuff, 0, regionBuff.length, MPI.OBJECT, coordinatorRank);
      MPI.COMM_WORLD.Bcast(samplesBuff, 0, 1, MPI.INT, coordinatorRank);
    }
  }

  private static double doEstimation(
      final int coordinatorRank,
      final int tag,
      final int thisRank,
      final Region regionToEstimate,
      final int[] samplesBuff) {
    final double estimation;
    if (thisRank == coordinatorRank) {
      final int worldSize = MPI.COMM_WORLD.Size();
      final MPIMCCoordinator mpiCoordinator = new MPIMCCoordinator(thisRank, worldSize, tag);
      estimation = mpiCoordinator.estimate(regionToEstimate, samplesBuff[0]);
    } else {
      final MPIMCEstimator mpiEstimator = new MPIMCEstimator(coordinatorRank, tag);
      estimation = mpiEstimator.estimate(regionToEstimate, samplesBuff[0]);
    }
    return estimation;
  }
}