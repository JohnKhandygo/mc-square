package generators;

import com.google.common.collect.Lists;
import com.ksp.khandygo.geometry.Circle;
import com.ksp.khandygo.geometry.Rectangle;
import com.ksp.khandygo.geometry.core.Point;
import static java.lang.Math.random;
import java.util.List;
import java.util.function.Supplier;

public class PointsGenerator {

  private final int sampleSize;

  public PointsGenerator(final int sampleSize) {
    this.sampleSize = sampleSize;
  }

  public List<Point> inside(final Circle c) {
    Supplier<Double> distanceSupplier = () -> random() * c.radius();
    return pointsOnSuppliedDistanceFrom(distanceSupplier, c.centre());
  }

  public List<Point> pointsOnSuppliedDistanceFrom(
      final Supplier<Double> distanceSupplier,
      final Point p) {
    final List<Point> ps = Lists.newArrayList();
    for (int i = 0; i < sampleSize; ++i) {
      final double d = distanceSupplier.get();
      final double w = Math.toRadians(random() * 360);
      ps.add(new Point(p.x() + d * Math.cos(w), p.y() + d * Math.sin(w)));
    }
    return ps;
  }

  public List<Point> outside(final Circle c) {
    Supplier<Double> distanceSupplier = () -> (c.radius() + 0.01) + random() * c.radius();
    return pointsOnSuppliedDistanceFrom(distanceSupplier, c.centre());
  }

  public List<Point> inside(final Rectangle r) {
    final Point ll = r.lowerLeft();
    final List<Point> ps = Lists.newArrayList();
    for (int i = 0; i < sampleSize; ++i) {
      final double xOffset = random() * r.width();
      final double yOffset = random() * r.height();
      ps.add(new Point(ll.x() + xOffset, ll.y() + yOffset));
    }
    return ps;
  }

  public List<Point> outside(final Rectangle r) {
    final Point ll = r.lowerLeft();
    final List<Point> ps = Lists.newArrayList();
    for (int i = 0; i < sampleSize; ++i) {
      final double xOffset;
      final double yOffset;
      if (random() > 0.5) {
        xOffset = r.width() + random() * r.width();
        yOffset = r.height() + random() * r.height();
      } else {
        xOffset = -random() * r.width();
        yOffset = -random() * r.height();
      }
      ps.add(new Point(ll.x() + xOffset, ll.y() + yOffset));
    }
    return ps;
  }

  public List<Point> onTheEdgeOf(final Circle c) {
    return pointsOnSuppliedDistanceFrom(c::radius, c.centre());
  }
}