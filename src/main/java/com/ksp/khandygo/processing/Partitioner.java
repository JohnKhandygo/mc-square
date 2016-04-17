package com.ksp.khandygo.processing;

import com.google.common.base.Preconditions;
import com.google.common.base.Verify;
import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.ksp.khandygo.geometry.Circle;
import static java.util.Collections.unmodifiableList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

public class Partitioner {

  private final List<Circle> cs;

  private final Queue<List<Circle>> partitions;

  private Partitioner(final List<Circle> cs, final Queue<List<Circle>> partitions) {
    this.cs = cs;
    this.partitions = partitions;
  }

  Queue<List<Circle>> partitions() {
    return partitions.isEmpty() ? doPartition() : Queues.newLinkedBlockingQueue(partitions);
  }

  private Queue<List<Circle>> doPartition() {
    while (!cs.isEmpty()) {
      final List<Circle> partition = Lists.newArrayList(cs.remove(0));
      while (true) {
        final List<Circle> intersects = Lists.newArrayList();
        final Iterator<Circle> it = cs.iterator();
        while (it.hasNext()) {
          final Circle c = it.next();
          if (atLeastOneOfIntersectsWith(partition, c)) {
            intersects.add(c);
            it.remove();
          }
        }
        if (intersects.isEmpty()) break;
        partition.addAll(intersects);
      }
      Verify.verify(!partition.isEmpty());
      partitions.add(unmodifiableList(partition));
    }
    return partitions;
  }

  private boolean atLeastOneOfIntersectsWith(final List<Circle> partition, final Circle c) {
    return partition.stream().anyMatch(c::intersectsWith);
  }

  public static Partitioner of(final List<Circle> cs) {
    Preconditions.checkState(!cs.isEmpty());
    return new Partitioner(Lists.newArrayList(cs), Queues.newLinkedBlockingQueue());
  }
}
