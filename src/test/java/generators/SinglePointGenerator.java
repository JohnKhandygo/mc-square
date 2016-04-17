package generators;

import com.ksp.khandygo.geometry.core.Point;
import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

public class SinglePointGenerator extends Generator<Point> {

  public SinglePointGenerator() {
    super(Point.class);
  }

  @Override
  public Point generate(final SourceOfRandomness random, final GenerationStatus status) {
    final double x = random.nextDouble(0.1, 100.0);
    final double y = random.nextDouble(0.1, 100.0);
    return new Point(x, y);
  }
}
