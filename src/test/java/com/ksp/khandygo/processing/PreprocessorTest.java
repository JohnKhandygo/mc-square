package com.ksp.khandygo.processing;

import com.ksp.khandygo.geometry.Circle;
import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import generators.CirclesGenerator;
import generators.SingleCircleGenerator;
import generators.annotations.Embedded;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import org.assertj.core.api.Condition;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.Collections;
import java.util.List;

@RunWith(JUnitQuickcheck.class)
public class PreprocessorTest {

  @Test(expected = IllegalStateException.class)
  public void whenEmptyListSuppliedAsCreationParameter_theISEWillBeThrown() {
    Preprocessor.of(Collections.emptyList());
  }

  @Test(expected = NullPointerException.class)
  public void whenNullSuppliedAsCreationParameter_theNPEWillBeThrown() {
    Preprocessor.of((List<Circle>) null);
  }

  @Property(trials = 101, shrink = false)
  public void whenPreprocessSingleCircle_resultContaindOnlyThisCircle(
      final @From(SingleCircleGenerator.class) Circle c) {
    final Preprocessor pp = Preprocessor.of(c);
    final List<Circle> pcs = pp.process();
    assertThat(pcs).hasSameSizeAs(pcs);
    assertThat(pcs.get(0)).isEqualTo(c);
  }

  @Property(trials = 101, shrink = false)
  public void whenPreprocessListWhereOneSingleCircleEnclosesAllOthers_resultContaindOnlyThisCircle(
      final @Embedded @From(CirclesGenerator.class) List<Circle> cs) {
    final Preprocessor pp = Preprocessor.of(cs);
    final List<Circle> pcs = pp.process();
    assertThat(pcs).hasSize(1);
    assertThat(cs).are(enclosedBy(pcs.get(0)));
  }

  private Condition<Circle> enclosedBy(final Circle encloser) {
    return new Condition<>(encloser::contains, format("enclosed by %s", encloser));
  }

  @Property(trials = 101, shrink = false)
  public void generalPreprocessorTest(final @From(CirclesGenerator.class) List<Circle> cs) {
    final Preprocessor pp = Preprocessor.of(cs);
    final List<Circle> pcs = pp.process();
    assertThat(cs).are(enclosedByOneOf(pcs));
  }

  private Condition<Circle> enclosedByOneOf(final List<Circle> cs) {
    return new Condition<>(c -> anyOfContains(cs, c), format("enclosed by %s", cs));
  }

  private boolean anyOfContains(final List<Circle> cs, final Circle c) {
    return cs.stream().anyMatch(e -> e.contains(c));
  }
}