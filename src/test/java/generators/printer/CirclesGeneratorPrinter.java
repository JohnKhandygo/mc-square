package generators.printer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.base.Verify;
import com.ksp.khandygo.geometry.Circle;
import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import generators.CirclesGenerator;
import generators.annotations.Arbitrary;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import lombok.AllArgsConstructor;
import org.junit.runner.RunWith;
import java.io.File;
import java.io.IOException;
import java.util.List;

@RunWith(JUnitQuickcheck.class)
public class CirclesGeneratorPrinter {

  private static final int TRIALS = 1000;

  private static final int TESTED_SAMPLE_SIZE = 100;

  private static final String BASE_PATH = "D:\\study\\10\\pv\\mc-square\\mpj-performanse\\inputs";

  private static final String FILE_NAME_PATTER = BASE_PATH + "\\" + "%03d";

  private static final ObjectMapper MAPPER = new ObjectMapper();

  private static int NUMBER = 0;

  static {
    MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
  }

  @Property(trials = TRIALS, shrink = false)
  public void print(
      final @From(CirclesGenerator.class) @Arbitrary(size = TESTED_SAMPLE_SIZE) List<Circle> cs)
  throws IOException {
    File f = new File(format(FILE_NAME_PATTER, NUMBER++));
    if (f.exists()) {
      Verify.verify(f.isFile());
      f.delete();
    }
    f.createNewFile();
    final List<CircleRepresentation> crs = cs.stream()
        .map(c -> new CircleRepresentation(c.centre().x(), c.centre().y(), c.radius()))
        .collect(toList());
    final CirclesRepresentation csr = new CirclesRepresentation(crs);
    MAPPER.writeValue(f, csr);
  }

  @AllArgsConstructor
  @JsonAutoDetect(
      fieldVisibility = JsonAutoDetect.Visibility.ANY,
      isGetterVisibility = JsonAutoDetect.Visibility.NONE,
      getterVisibility = JsonAutoDetect.Visibility.NONE,
      setterVisibility = JsonAutoDetect.Visibility.NONE)
  private static class CircleRepresentation {
    private final double x;

    private final double y;

    private final double r;
  }

  @AllArgsConstructor
  @JsonAutoDetect(
      fieldVisibility = JsonAutoDetect.Visibility.ANY,
      isGetterVisibility = JsonAutoDetect.Visibility.NONE,
      getterVisibility = JsonAutoDetect.Visibility.NONE,
      setterVisibility = JsonAutoDetect.Visibility.NONE)
  private static class CirclesRepresentation {
    private final List<CircleRepresentation> circles;
  }
}
