package com.ksp.khandygo.processing.parallel;

import com.ksp.khandygo.geometry.Circle;
import com.ksp.khandygo.geometry.Rectangle;
import com.ksp.khandygo.processing.MCEstimator;
import com.ksp.khandygo.processing.MCEstimatorTest;
import com.ksp.khandygo.processing.sequential.SequentialMCEstimator;
import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import generators.CirclesGenerator;
import generators.SingleCircleGenerator;
import generators.SingleRectangleGenerator;
import generators.annotations.Unintersected;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Offset.offset;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import java.util.List;
import java.util.concurrent.Executors;

@RunWith(JUnitQuickcheck.class)
public class ParallelMCEstimatorTest extends MCEstimatorTest {

  private static int TOTAL_RUNS;

  private static int MC_SAMPLE_SIZE;

  @Ignore
  @Property(trials = 13, shrink = false)
  public void compareSingleCircleEstimation(
      final @From(SingleCircleGenerator.class) Circle c)
  throws Exception {
    final double actual = successRateOfMCEstimation(
        verifiableEstimator(), c, MC_SAMPLE_SIZE, TOTAL_RUNS);
    final double expected = successRateOfMCEstimation(
        referenceEstimator(), c, MC_SAMPLE_SIZE, TOTAL_RUNS);
    assertThat(actual).isEqualTo(expected, offset(4e-2));
  }

  private MCEstimator referenceEstimator() {
    return new SequentialMCEstimator();
  }

  private ParallelMCEstimator verifiableEstimator() {
    return new ParallelMCEstimator(Executors.newCachedThreadPool(), 5);
  }

  @Property(trials = 13, shrink = false)
  public void testSingleCircleEstimation(
      final @From(SingleCircleGenerator.class) Circle c)
  throws Exception {
    assertThat(successRateOfMCEstimation(verifiableEstimator(), c, MC_SAMPLE_SIZE, TOTAL_RUNS))
        .isEqualTo(0.95, offset(2e-2));
  }

  @Ignore
  @Property(trials = 13, shrink = false)
  public void compareSingleRectangleEstimation(
      final @From(SingleRectangleGenerator.class) Rectangle r)
  throws Exception {
    final double actual = successRateOfMCEstimation(
        verifiableEstimator(), r, MC_SAMPLE_SIZE, TOTAL_RUNS);
    final double expected = successRateOfMCEstimation(
        referenceEstimator(), r, MC_SAMPLE_SIZE, TOTAL_RUNS);
    assertThat(actual).isEqualTo(expected, offset(4e-2));
  }

  @Property(trials = 13, shrink = false)
  public void testSingleRectangleEstimation(
      final @From(SingleRectangleGenerator.class) Rectangle r)
  throws Exception {
    assertThat(successRateOfMCEstimation(verifiableEstimator(), r, MC_SAMPLE_SIZE, TOTAL_RUNS))
        .isEqualTo(1.0);
  }

  @Ignore
  @Property(trials = 13, shrink = false)
  public void commonMCEstimatorsComparison(
      final @From(CirclesGenerator.class) @Unintersected List<Circle> cs)
  throws Exception {
    final double actual = successRateOfMCEstimation(
        verifiableEstimator(), cs, MC_SAMPLE_SIZE, TOTAL_RUNS);
    final double expected = successRateOfMCEstimation(
        referenceEstimator(), cs, MC_SAMPLE_SIZE, TOTAL_RUNS);
    assertThat(actual).isEqualTo(expected, offset(4e-2));
  }

  @Property(trials = 13, shrink = false)
  public void commonMCEstimatorTest(
      final @From(CirclesGenerator.class) @Unintersected List<Circle> cs)
  throws Exception {
    assertThat(successRateOfMCEstimation(verifiableEstimator(), cs, MC_SAMPLE_SIZE, TOTAL_RUNS))
        .isEqualTo(0.95, offset(2e-2));
  }

  @BeforeClass
  public static void setUp() {
    TOTAL_RUNS = 1000;
    MC_SAMPLE_SIZE = 1000;
  }
}
