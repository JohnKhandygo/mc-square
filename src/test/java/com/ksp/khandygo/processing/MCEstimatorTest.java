package com.ksp.khandygo.processing;

import com.google.common.base.Preconditions;
import com.ksp.khandygo.geometry.Circle;
import com.ksp.khandygo.geometry.Rectangle;
import com.ksp.khandygo.geometry.SingleRectangleCoverBuilder;
import com.ksp.khandygo.geometry.core.Figure;
import com.ksp.khandygo.geometry.core.HasSquare;
import static java.lang.Math.*;
import org.junit.BeforeClass;
import java.util.List;
import java.util.stream.IntStream;

public class MCEstimatorTest {

  private static SingleRectangleCoverBuilder SingleRectangleCoverBuilder;

  protected double successRateOfMCEstimation(
      final MCEstimator estimator,
      final List<Circle> circlesToEstimate, final int mcSampleSize, final int totalRuns) {
    for (final Circle c : circlesToEstimate)
      for (Circle oc : circlesToEstimate)
        if (!oc.equals(c)) Preconditions.checkState(!c.intersectsWith(oc));

    return successRateOfMCEstimation(estimator, new FigureOfCircles(circlesToEstimate),
        mcSampleSize, totalRuns);
  }

  protected double successRateOfMCEstimation(
      final MCEstimator estimator,
      final Figure figureToEstimate, final int mcSampleSize, final int totalRuns) {
    final Rectangle r = SingleRectangleCoverBuilder.cover(figureToEstimate);
    final double eps = findBound(r, figureToEstimate, mcSampleSize);
    final double expected = figureToEstimate.square() / r.square();
    final long successfulRuns = IntStream.range(0, totalRuns)
        .mapToObj(i -> estimator.estimate(figureToEstimate, mcSampleSize))
        .filter(estimation -> abs(estimation - expected) <= eps).count();
    return ((double) successfulRuns) / totalRuns;
  }

  private double findBound(
      final HasSquare enclosing, final HasSquare enclosed,
      final int sampleSize) {
    final double squaresRatio = enclosed.square() / enclosing.square();
    final double s2 = squaresRatio * (1 - squaresRatio);
    return 1.96 * sqrt(s2) / sqrt(sampleSize);
  }

  @BeforeClass
  public static void setUpForAllTests() {
    SingleRectangleCoverBuilder = new SingleRectangleCoverBuilder();
  }
}

