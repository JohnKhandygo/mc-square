package com.ksp.khandygo.geometry;

import com.ksp.khandygo.geometry.core.Point;
import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import generators.PointsGenerator;
import generators.SinglePointGenerator;
import static java.lang.Math.abs;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import org.assertj.core.api.Condition;
import org.junit.runner.RunWith;
import java.util.List;

@RunWith(JUnitQuickcheck.class)
public class DistanceTest {

  private final PointsGenerator generator = new PointsGenerator(1000);

  @Property(trials = 100, shrink = false)
  public void testDistanceMeasuring(
      final @From(SinglePointGenerator.class) Point p0,
      final @InRange(minDouble = 0.1, maxDouble = 100) double expected) {
    final List<Point> ps = generator.pointsOnSuppliedDistanceFrom(() -> expected, p0);
    final List<Double> ds = ps.stream().map(p -> Distance.distanceBetween(p, p0)).collect(toList());
    assertThat(ds).are(withinDistance(expected));
  }

  private Condition<Double> withinDistance(final double expected) {
    return new Condition<>(d -> abs(d - expected) <= 1e-6, "inside expected distance");
  }

  @Property(trials = 100, shrink = false)
  public void testSquaredDistanceMeasuring(
      final @From(SinglePointGenerator.class) Point p0,
      final @InRange(minDouble = 0.1, maxDouble = 100) double d0) {
    final List<Point> ps = generator.pointsOnSuppliedDistanceFrom(() -> d0, p0);
    final List<Double> ds = ps.stream().map(p -> Distance.distance2Between(p, p0))
        .collect(toList());
    assertThat(ds).are(withinDistance(d0 * d0));
  }

  @Property(trials = 100)
  public void whenArgumentsAreSwapped_measureStayTheSame(
      final @From(SinglePointGenerator.class) Point p1,
      final @From(SinglePointGenerator.class) Point p2) {
    final double dFrom1To2 = Distance.distanceBetween(p1, p2);
    final double dFrom2To1 = Distance.distanceBetween(p2, p1);
    assertThat(dFrom1To2).isEqualTo(dFrom2To1);
  }

  @Property(trials = 100)
  public void whenArgumentsAreSwapped_squaredMeasureStayTheSame(
      final @From(SinglePointGenerator.class) Point p1,
      final @From(SinglePointGenerator.class) Point p2) {
    final double d2From1To2 = Distance.distance2Between(p1, p2);
    final double d2From2To1 = Distance.distance2Between(p2, p1);
    assertThat(d2From1To2).isEqualTo(d2From2To1);
  }

  @Property(trials = 100)
  public void distanceToThePointItself_isEqualsToZero(
      final @From(SinglePointGenerator.class) Point p) {
    final double d = Distance.distanceBetween(p, p);
    final double d2 = Distance.distance2Between(p, p);
    assertThat(d).isEqualTo(d2).isEqualTo(0);
  }
}
