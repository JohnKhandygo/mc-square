package performance;

import com.google.common.base.Stopwatch;
import com.ksp.khandygo.geometry.Circle;
import com.ksp.khandygo.processing.FigureOfCircles;
import com.ksp.khandygo.processing.MCEstimator;
import com.ksp.khandygo.processing.sequential.SequentialMCEstimator;
import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import generators.CirclesGenerator;
import generators.annotations.Unintersected;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@RunWith(JUnitQuickcheck.class)
public class MultiSequentialEstimatorPerformanceTest {
  private final static int TRIALS = 100;

  private final static int TESTED_SAMPLE_SIZE = 100;

  private static final int MC_SAMPLE_SIZE = 100000;

  private static final int PARALLELISM_LEVEL = 4;

  private static AtomicLong MIN;

  private static AtomicLong SUM;

  private static AtomicLong MAX;

  @Property(trials = TRIALS)
  public void measureCommonMCEstimationPerformance(
      final @From(CirclesGenerator.class) @Unintersected(size = TESTED_SAMPLE_SIZE) List<Circle> cs)
  throws InterruptedException {
    final ExecutorService executor = Executors.newCachedThreadPool();
    final CountDownLatch latch = new CountDownLatch(PARALLELISM_LEVEL);
    final Stopwatch watch = Stopwatch.createStarted();
    for (int i = 0; i < PARALLELISM_LEVEL; ++i) {
      executor.submit(() -> {
        final MCEstimator estimator = new SequentialMCEstimator();
        estimator.estimate(new FigureOfCircles(cs), MC_SAMPLE_SIZE / PARALLELISM_LEVEL);
        latch.countDown();
      });
    }
    latch.await();
    final long measurement = watch.elapsed(TimeUnit.MILLISECONDS);
    System.out.format("\tMEASURE %dms%n", measurement);
    MIN.getAndUpdate(min -> measurement < min ? measurement : min);
    SUM.addAndGet(measurement);
    MAX.getAndUpdate(max -> measurement > max ? measurement : max);
  }

  @BeforeClass
  public static void setUp() {
    MIN = new AtomicLong(TimeUnit.MINUTES.toMillis(1));
    SUM = new AtomicLong(0L);
    MAX = new AtomicLong(0L);
    printSetup();
    System.out.println("RUNNING:");
  }

  private static void printSetup() {
    System.out.println("SETUP:");
    System.out.format("\tTRIALS = %d%n", TRIALS);
    System.out.format("\tTEST SAMPLE SIZE = %d%n", TESTED_SAMPLE_SIZE);
    System.out.format("\tESTIMATION SAMPLE SIZE = %d%n", MC_SAMPLE_SIZE);
  }

  @AfterClass
  public static void tearDown() {
    System.out.println("RESULT:");
    System.out.format("\tmin time was %dms%n", MIN.get());
    System.out.format("\tavg time was %dms%n", SUM.get() / TRIALS);
    System.out.format("\tmax time was %dms%n", MAX.get());
    System.out.format("\tsum time was %dms%n", SUM.get());
  }
}
