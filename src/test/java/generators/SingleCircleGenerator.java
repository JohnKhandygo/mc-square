package generators;

import com.ksp.khandygo.geometry.Circle;
import com.ksp.khandygo.geometry.core.Point;
import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

public class SingleCircleGenerator extends Generator<Circle> {

  public SingleCircleGenerator() {
    super(Circle.class);
  }

  @Override
  public Circle generate(final SourceOfRandomness random, final GenerationStatus status) {
    final double x = random.nextDouble(0.1, 100.0);
    final double y = random.nextDouble(0.1, 100.0);
    final double r = random.nextDouble(0.1, 100.0);
    return new Circle(new Point(x, y), r);
  }
}
