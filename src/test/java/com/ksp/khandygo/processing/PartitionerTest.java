package com.ksp.khandygo.processing;

import com.google.common.collect.Lists;
import com.ksp.khandygo.geometry.Circle;
import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import generators.CirclesGenerator;
import generators.SingleCircleGenerator;
import generators.annotations.Intersected;
import generators.annotations.Unintersected;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import org.assertj.core.api.Condition;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

@RunWith(JUnitQuickcheck.class)
public class PartitionerTest {

  @Test(expected = IllegalStateException.class)
  public void whenEmptyListSuppliedAsCreationParameter_theISEWillBeThrown() {
    Partitioner.of(Collections.emptyList());
  }

  @Test(expected = NullPointerException.class)
  public void whenNullSuppliedAsCreationParameter_theNPEWillBeThrown() {
    Partitioner.of(null);
  }

  @Property(trials = 101, shrink = false)
  public void whenPartitionOneCircleList_onePartitionContainingThatCircleReturned(
      final @From(SingleCircleGenerator.class) Circle c) {
    final Partitioner pr = Partitioner.of(Lists.newArrayList(c));
    final Queue<List<Circle>> prs = pr.partitions();
    assertThat(prs).hasSize(1);
    assertThat(prs.peek()).hasSize(1);
    assertThat(prs.peek().get(0)).isEqualTo(c);
  }

  @Property(trials = 101, shrink = false)
  public void whenPartitionUninersectedCircles_partitionsConsistsOfEveryCircleOnlyReturned(
      final @Unintersected @From(CirclesGenerator.class) List<Circle> cs) {
    final Partitioner pr = Partitioner.of(cs);
    final Queue<List<Circle>> prs = pr.partitions();
    assertThat(prs).hasSameSizeAs(cs);
    assertThat(prs).are(hasSize(1));
    final List<Circle> collected = prs.stream().map(l -> l.get(0)).collect(toList());
    assertThat(collected).containsOnlyElementsOf(cs);
  }

  private Condition<List<Circle>> hasSize(final int expectedSize) {
    return new Condition<>(p -> p.size() == expectedSize, format("has size %d", expectedSize));
  }

  @Property(trials = 101, shrink = false)
  public void whenPartitionInersectedCircles_onePartitionConsistsOfAllCirclesReturned(
      final @Intersected @From(CirclesGenerator.class) List<Circle> cs) {
    final Partitioner pr = Partitioner.of(cs);
    final Queue<List<Circle>> prs = pr.partitions();
    assertThat(prs).hasSize(1);
    assertThat(prs.peek()).hasSameSizeAs(cs);
    assertThat(prs.peek()).containsOnlyElementsOf(cs);
  }

  @Property(trials = 101, shrink = false)
  public void generalPartitionTest(
      final @From(CirclesGenerator.class) List<Circle> cs) {
    final Partitioner pr = Partitioner.of(cs);
    final Queue<List<Circle>> prs = pr.partitions();
    final List<List<Circle>> partitionsList = Lists.newArrayList(prs);
    for (int i = 0; i < partitionsList.size(); ++i) {
      final List<Circle> partition = partitionsList.get(i);
      if (partition.size() > 1)
        checkThatAllElementsActuallyConnected(partition);

      for (int j = 0; j < partitionsList.size(); ++j) {
        if (j == i) continue;
        final List<Circle> otherPartition = partitionsList.get(j);
        checkThatPartitionsNotConnected(partition, otherPartition);
      }
    }
  }

  private void checkThatPartitionsNotConnected(
      final List<Circle> partition,
      final List<Circle> otherPartition) {
    for (int t = 0; t < partition.size(); ++t) {
      final Circle c = partition.get(t);
      boolean intersects = false;
      for (int k = 0; k < otherPartition.size(); ++k) {
        if (k != t && otherPartition.get(k).intersectsWith(c)) {
          intersects = true;
          break;
        }
      }
      assertThat(intersects).isFalse();
    }
  }

  private void checkThatAllElementsActuallyConnected(final List<Circle> partition) {
    for (int j = 0; j < partition.size(); ++j) {
      final Circle c = partition.get(j);
      boolean intersects = false;
      for (int k = 0; k < partition.size(); ++k) {
        if (k != j && partition.get(k).intersectsWith(c)) {
          intersects = true;
          break;
        }
      }
      assertThat(intersects).isTrue();
    }
  }
}