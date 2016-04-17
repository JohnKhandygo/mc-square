package com.ksp.khandygo.geometry.core;

import com.google.common.collect.Range;
import java.io.Serializable;

public interface Region extends Serializable {

  boolean contains(final Point point);

  boolean contains(final Region region);

  Range<Double> xprojection();

  Range<Double> yprojection();
}
