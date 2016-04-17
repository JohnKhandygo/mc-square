package com.ksp.khandygo.processing.sequential;

import com.ksp.khandygo.geometry.Circle;
import com.ksp.khandygo.geometry.Rectangle;
import com.ksp.khandygo.processing.MCEstimator;
import com.ksp.khandygo.processing.MCEstimatorTest;
import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import generators.CirclesGenerator;
import generators.SingleCircleGenerator;
import generators.SingleRectangleGenerator;
import generators.annotations.Unintersected;
import static org.assertj.core.api.Assertions.assertThat;
import org.assertj.core.data.Offset;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import java.util.List;

@RunWith(JUnitQuickcheck.class)
public class SequentialMCEstimatorTest extends MCEstimatorTest {

  private static int TOTAL_RUNS;

  private static int MC_SAMPLE_SIZE;

  @Property(trials = 13, shrink = false)
  public void testSingleCircleEstimation(
      final @From(SingleCircleGenerator.class) Circle c)
  throws Exception {
    final MCEstimator estimator = new SequentialMCEstimator();
    assertThat(successRateOfMCEstimation(estimator, c, MC_SAMPLE_SIZE, TOTAL_RUNS))
        .isEqualTo(0.95, Offset.offset(2e-2));
  }

  @Property(trials = 13, shrink = false)
  public void testSingleRectangleEstimation(
      final @From(SingleRectangleGenerator.class) Rectangle r)
  throws Exception {
    final MCEstimator estimator = new SequentialMCEstimator();
    assertThat(successRateOfMCEstimation(estimator, r, MC_SAMPLE_SIZE, TOTAL_RUNS))
        .isEqualTo(1.0);
  }

  @Property(trials = 13, shrink = false)
  public void commonMCEstimatorTest(
      final @From(CirclesGenerator.class) @Unintersected List<Circle> cs)
  throws Exception {
    final MCEstimator estimator = new SequentialMCEstimator();
    assertThat(successRateOfMCEstimation(estimator, cs, MC_SAMPLE_SIZE, TOTAL_RUNS))
        .isEqualTo(0.95, Offset.offset(2e-2));
  }

  @BeforeClass
  public static void setUp() {
    TOTAL_RUNS = 1000;
    MC_SAMPLE_SIZE = 1000;
  }
}

