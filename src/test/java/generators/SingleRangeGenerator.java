package generators;

import com.google.common.collect.Range;
import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

public class SingleRangeGenerator extends Generator<Range> {

  public SingleRangeGenerator() {
    super(Range.class);
  }

  @Override
  public Range<Double> generate(final SourceOfRandomness random, final GenerationStatus status) {
    final double s = random.nextDouble(0.1, 100.0);
    final double w = random.nextDouble(0.1, 100.0);
    return Range.closed(s, s + w);
  }
}
