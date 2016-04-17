package generators;

import com.ksp.khandygo.geometry.Rectangle;
import com.ksp.khandygo.geometry.core.Point;
import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

public class SingleRectangleGenerator extends Generator<Rectangle> {

  public SingleRectangleGenerator() {
    super(Rectangle.class);
  }

  @Override
  public Rectangle generate(final SourceOfRandomness random, final GenerationStatus status) {
    final SinglePointGenerator spg = new SinglePointGenerator();
    final Point ll = spg.generate(random, status);
    final double w = random.nextDouble(0.1, 100);
    final double h = random.nextDouble(0.1, 100);
    return Rectangle.of(ll, w, h);
  }
}
