package generators;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.ksp.khandygo.geometry.Circle;
import static com.ksp.khandygo.geometry.Distance.distanceBetween;
import com.ksp.khandygo.geometry.Rectangle;
import com.ksp.khandygo.geometry.core.Point;
import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import generators.annotations.Arbitrary;
import generators.annotations.Embedded;
import generators.annotations.Intersected;
import generators.annotations.Unintersected;
import java.util.Collections;
import java.util.List;

public class CirclesGenerator extends Generator<List> {

  private final static int MIN_SAMPLE_SIZE = 2;

  private final static int MAX_SAMPLE_SIZE = 100;

  private final static Rectangle REGION = Rectangle.of(new Point(-50, -50), 100, 100);

  private Arbitrary arbitrary;

  private Intersected intersected;

  private Unintersected unintersected;

  private Embedded embedded;

  private int declaredSampleSize = 0;

  public CirclesGenerator() {
    super(List.class);
  }

  @Override
  public List<Circle> generate(final SourceOfRandomness random, final GenerationStatus status) {
    final List<Circle> cs = Lists.newArrayList();
    final int sampleSize = declaredSampleSize < 1 ?
        random.nextInt(MIN_SAMPLE_SIZE, MAX_SAMPLE_SIZE) :
        declaredSampleSize;
    final List<Point> ps = new PointsGenerator(sampleSize).inside(REGION);
    for (final Point p : ps) {
      final double r;
      if (intersected != null) {
        if (cs.isEmpty()) {
          r = random.nextDouble(0.1, 10);
        } else if (cs.stream().anyMatch(c -> c.contains(p))) {
          r = random.nextDouble(0.1, 10);
        } else {
          final double minR = cs.stream()
              .mapToDouble(c -> distanceBetween(c.centre(), p) - c.radius())
              .min()
              .getAsDouble();
          r = random.nextDouble(minR, minR + 10.0);
        }
      } else if (unintersected != null) {
        final double minD = ps.stream()
            .mapToDouble(op -> distanceBetween(op, p) / 2)
            .filter(d -> d > 0)
            .min()
            .getAsDouble();
        final double maxR = minD > 0.1 ? minD - 0.1 : minD / 2;
        final double minR = maxR > 0.1 ? 0.1 : maxR / 2;
        r = random.nextDouble(minR, maxR);
      } else if (embedded != null) {
        if (cs.isEmpty()) {
          final double maxD = ps.stream()
              .mapToDouble(op -> distanceBetween(op, p) + 0.1)
              .max()
              .getAsDouble();
          r = random.nextDouble(maxD, maxD + 10);
        } else {
          final Circle encloser = cs.get(0);
          final double maxD = encloser.radius() - distanceBetween(encloser.centre(), p);
          final double minR = maxD > 0.1 ? 0.1 : maxD / 2;
          final double maxR = maxD;
          r = random.nextDouble(minR, maxR);
        }
      } else {
        r = random.nextDouble(0.1, 10);
      }
      cs.add(new Circle(p, r));
    }
    Collections.shuffle(cs);
    return cs;
  }

  public void configure(Arbitrary arbitrary) {
    Preconditions.checkState(intersected == null);
    Preconditions.checkState(unintersected == null);
    Preconditions.checkState(embedded == null);
    this.arbitrary = arbitrary;
    this.declaredSampleSize = arbitrary.size();
  }

  public void configure(Intersected intersected) {
    Preconditions.checkState(arbitrary == null);
    Preconditions.checkState(unintersected == null);
    Preconditions.checkState(embedded == null);
    this.intersected = intersected;
    this.declaredSampleSize = intersected.size();
  }

  public void configure(Unintersected unintersected) {
    Preconditions.checkState(arbitrary == null);
    Preconditions.checkState(intersected == null);
    Preconditions.checkState(embedded == null);
    this.unintersected = unintersected;
    this.declaredSampleSize = unintersected.size();
  }

  public void configure(Embedded embedded) {
    Preconditions.checkState(arbitrary == null);
    Preconditions.checkState(intersected == null);
    Preconditions.checkState(unintersected == null);
    this.embedded = embedded;
    this.declaredSampleSize = embedded.size();
  }
}
