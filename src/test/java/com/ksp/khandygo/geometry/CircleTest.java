package com.ksp.khandygo.geometry;

import com.ksp.khandygo.geometry.core.Point;
import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import generators.PointsGenerator;
import generators.SingleCircleGenerator;
import static java.lang.Math.PI;
import static org.assertj.core.api.Assertions.assertThat;
import org.assertj.core.api.Condition;
import static org.assertj.core.data.Offset.offset;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.mock;

@RunWith(JUnitQuickcheck.class)
public class CircleTest {

  private final PointsGenerator points = new PointsGenerator(1000);

  @Property(trials = 100, shrink = false)
  public void testSquare(
      final @InRange(minDouble = 0.1, maxDouble = 100) double radius) {
    final Point pointMock = mock(Point.class);
    final Circle c = new Circle(pointMock, radius);
    final double actual = c.square();
    final double expected = PI * radius * radius;
    assertThat(actual).isEqualTo(expected, offset(1e-6));
  }

  @Property(trials = 100, shrink = false)
  public void testContains_onPointsThatReallyIn(final @From(SingleCircleGenerator.class) Circle c) {
    assertThat(points.inside(c)).are(insideCircle(c));
  }

  private Condition<Point> insideCircle(final Circle c) {
    return new Condition<>(c::contains, "inside circle");
  }

  @Property(trials = 100, shrink = false)
  public void testContains_onPointsThatReallyOut(
      final @From(SingleCircleGenerator.class) Circle c) {
    assertThat(points.outside(c)).areNot(insideCircle(c));
  }
}

