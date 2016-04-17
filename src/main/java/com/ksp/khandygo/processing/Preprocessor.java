package com.ksp.khandygo.processing;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.ksp.khandygo.geometry.Circle;
import static java.util.Collections.singletonList;
import java.util.Iterator;
import java.util.List;

public class Preprocessor {

  private final List<Circle> cs;

  private final List<Circle> processed;

  private Preprocessor(final List<Circle> cs, final List<Circle> processed) {
    this.cs = cs;
    this.processed = processed;
  }

  public List<Circle> process() {
    return processed.isEmpty() ? doProcess() : processed;
  }

  private List<Circle> doProcess() {
    for (final Circle c : cs) {
      if (atLeastOneProcessedEncloses(c)) continue;
      final Iterator<Circle> it = processed.iterator();
      while (it.hasNext())
        if (c.contains(it.next())) it.remove();
      processed.add(c);
    }
    return processed;
  }

  private boolean atLeastOneProcessedEncloses(final Circle c) {
    return processed.stream().anyMatch(pc -> pc.contains(c));
  }

  static Preprocessor of(final Circle c) {
    return of(singletonList(c));
  }

  public static Preprocessor of(final List<Circle> cs) {
    Preconditions.checkState(!cs.isEmpty());
    return new Preprocessor(cs, Lists.newArrayList());
  }
}
