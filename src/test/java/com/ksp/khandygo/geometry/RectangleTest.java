package com.ksp.khandygo.geometry;

import com.google.common.collect.Range;
import com.ksp.khandygo.geometry.core.Point;
import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import generators.PointsGenerator;
import generators.SinglePointGenerator;
import generators.SingleRangeGenerator;
import generators.SingleRectangleGenerator;
import static org.assertj.core.api.Assertions.assertThat;
import org.assertj.core.api.Condition;
import static org.assertj.core.data.Offset.offset;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.*;

@RunWith(JUnitQuickcheck.class)
public class RectangleTest {

  private final PointsGenerator points = new PointsGenerator(1000);

  @Property(trials = 100, shrink = false)
  public void testStaticCreation(
      final @From(SinglePointGenerator.class) Point p,
      final @InRange(minDouble = 0.1, maxDouble = 100) double w,
      final @InRange(minDouble = 0.1, maxDouble = 100) double h) {
    final Rectangle r = Rectangle.of(p, w, h);
    assertThat(r.lowerLeft()).isEqualTo(p);
    assertThat(r.width()).isEqualTo(w, offset(1e-6));
    assertThat(r.height()).isEqualTo(h, offset(1e-6));
  }

  @Property(trials = 100, shrink = false)
  public void testWidth(
      final @From(SingleRangeGenerator.class) Range<Double> xprojection) {
    final Rectangle r = mockRectangleWithWidthOnly(xprojection);
    final double actual = r.width();
    final double expected = xprojection.upperEndpoint() - xprojection.lowerEndpoint();
    assertThat(actual).isEqualTo(expected);
  }

  private Rectangle mockRectangleWithWidthOnly(final Range<Double> xprojection) {
    final Point ll = mockPointWithAbscissaOnly(xprojection.lowerEndpoint());
    final Point ur = mockPointWithAbscissaOnly(xprojection.upperEndpoint());
    return new Rectangle(ll, ur);
  }

  private Point mockPointWithAbscissaOnly(final double x) {
    final Point p = mock(Point.class);
    doReturn(x).when(p).x();
    return p;
  }

  @Property(trials = 100, shrink = false)
  public void testHeight(
      final @From(SingleRangeGenerator.class) Range<Double> yprojection) {
    final Rectangle r = mockRectangleWithHeightOnly(yprojection);
    final double actual = r.height();
    final double expected = yprojection.upperEndpoint() - yprojection.lowerEndpoint();
    assertThat(actual).isEqualTo(expected);
  }

  private Rectangle mockRectangleWithHeightOnly(final Range<Double> yprojection) {
    final Point ll = mockPointWithOrdinateOnly(yprojection.lowerEndpoint());
    final Point ur = mockPointWithOrdinateOnly(yprojection.upperEndpoint());
    return new Rectangle(ll, ur);
  }

  private Point mockPointWithOrdinateOnly(final double y) {
    final Point p = mock(Point.class);
    doReturn(y).when(p).y();
    return p;
  }

  @Property(trials = 100, shrink = false)
  public void testSquare(
      final @From(SingleRectangleGenerator.class) Rectangle r) {
    final double actual = r.square();
    final double expected = r.width() * r.height();
    assertThat(actual).isEqualTo(expected);
  }

  @Property(trials = 100, shrink = false)
  public void testContains_onPointsThatReallyIn(
      final @From(SingleRectangleGenerator.class) Rectangle r) {
    assertThat(points.inside(r)).are(insideRectangle(r));
  }

  private Condition<Point> insideRectangle(final Rectangle r) {
    return new Condition<>(r::contains, "inside rectangle");
  }

  @Property(trials = 100, shrink = false)
  public void testContains_onPointsThatReallyOut(
      final @From(SingleRectangleGenerator.class) Rectangle r) {
    assertThat(points.outside(r)).areNot(insideRectangle(r));
  }
}