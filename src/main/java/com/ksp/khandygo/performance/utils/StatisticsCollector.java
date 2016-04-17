package com.ksp.khandygo.performance.utils;

import static com.google.common.collect.Lists.newArrayList;
import static java.lang.Math.sqrt;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class StatisticsCollector {

  private final List<Long> measures;

  private StatisticsCollector(final List<Long> measures) {
    this.measures = measures;
  }

  public void add(final long measure) {
    measures.add(measure);
  }

  public void print() {
    long min = TimeUnit.MINUTES.toMillis(10);
    double sum = 0;
    double sum2 = 0;
    long max = 0;
    final int n = measures.size();
    for (int i = 0; i < n; ++i) {
      final long m = measures.get(i);
      if (m < min) min = m;
      if (m > max) max = m;
      sum += m;
      sum2 += m * m;
    }
    final double mean = sum / n;
    final double sd = sqrt((sum2 / n - mean * mean) * n / (n - 1));

    System.out.format("MEASURES:%n");
    System.out.format("\tMIN = %d%n", min);
    System.out.format("\tMEAN = %.2f%n", mean);
    System.out.format("\tSD = %.2f%n", sd);
    System.out.format("\tMAX = %d%n", max);
    System.out.println();
  }

  public static StatisticsCollector newOne() {
    return new StatisticsCollector(newArrayList());
  }
}
