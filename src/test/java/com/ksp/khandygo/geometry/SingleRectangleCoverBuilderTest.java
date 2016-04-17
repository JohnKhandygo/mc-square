package com.ksp.khandygo.geometry;

import com.google.common.base.Verify;
import com.ksp.khandygo.geometry.core.Point;
import com.ksp.khandygo.processing.Preprocessor;
import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import generators.CirclesGenerator;
import generators.PointsGenerator;
import generators.SingleCircleGenerator;
import generators.annotations.Embedded;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import org.assertj.core.api.Condition;
import static org.assertj.core.data.Offset.offset;
import org.junit.runner.RunWith;
import java.util.List;

@RunWith(JUnitQuickcheck.class)
public class SingleRectangleCoverBuilderTest {

  @Property(trials = 101, shrink = false)
  public void whenEnclosingSingleCircle_getASquareWithDoubledRadiusSides(
      final @From(SingleCircleGenerator.class) Circle c) {
    //GIVEN
    final SingleRectangleCoverBuilder coverBuilder = new SingleRectangleCoverBuilder();
    //WHEN
    final Rectangle r = coverBuilder.cover(c);
    //THEN
    assertThat(r.height()).isEqualTo(r.width(), offset(1e-6));
    assertThat(r.width()).isEqualTo(2 * c.radius(), offset(1e-6));
    assertThat(r.lowerLeft().x()).isEqualTo(c.centre().x() - c.radius(), offset(1e-6));
    assertThat(r.lowerLeft().y()).isEqualTo(c.centre().y() - c.radius(), offset(1e-6));
  }

  @Property(trials = 101, shrink = false)
  public void whenEnclosingEmbedsCircles_theResultIsTheSameAsForEnclosingCircle(
      final @Embedded @From(CirclesGenerator.class) List<Circle> cs) {
    //GIVEN
    final SingleRectangleCoverBuilder coverBuilder = new SingleRectangleCoverBuilder();
    final Circle enclosingCircle = enclosingCircle(cs);
    //WHEN
    final Rectangle r1 = coverBuilder.cover(cs);
    final Rectangle r2 = coverBuilder.cover(enclosingCircle);
    //THEN
    assertThat(r1).isEqualTo(r2);
  }

  private Circle enclosingCircle(
      final @From(SingleCircleGenerator.class) @Embedded List<Circle> cs) {
    final Preprocessor pp = Preprocessor.of(cs);
    final List<Circle> pcs = pp.process();
    Verify.verify(pcs.size() == 1);
    return pcs.get(0);
  }

  @Property(trials = 101, shrink = false)
  public void generalEncloserTest(
      final @From(CirclesGenerator.class) List<Circle> cs) {
    //GIVEN
    final SingleRectangleCoverBuilder coverBuilder = new SingleRectangleCoverBuilder();
    final List<Point> ps = pointOnTheEdgesOf(cs);
    //WHEN
    final Rectangle r = coverBuilder.cover(cs);
    //THEN
    assertThat(ps).are(insideRectangle(r));
  }

  private List<Point> pointOnTheEdgesOf(final @From(CirclesGenerator.class) List<Circle> cs) {
    final PointsGenerator points = new PointsGenerator(1000);
    return cs.stream()
        .map(points::onTheEdgeOf)
        .flatMap(List::stream)
        .collect(toList());
  }

  private Condition<Point> insideRectangle(final Rectangle r) {
    return new Condition<>(r::contains, "inside rectangle");
  }
}
