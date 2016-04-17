package com.ksp.khandygo.performance.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Verify;
import com.google.common.collect.Lists;
import com.ksp.khandygo.geometry.Circle;
import com.ksp.khandygo.geometry.CompositeRegion;
import com.ksp.khandygo.geometry.core.Point;
import com.ksp.khandygo.geometry.core.Region;
import static java.util.stream.Collectors.toList;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class InputProvider {

  private final File baseDir;

  private InputProvider(final File baseDir) {
    this.baseDir = baseDir;
  }

  public List<Region> provide()
  throws IOException {
    return Stream.of(baseDir.listFiles()).map(this::parseRegion).collect(toList());
  }

  private CompositeRegion parseRegion(final File inputFile) {
    final JsonNode setup;
    try {
      setup = new ObjectMapper().readTree(inputFile);
    } catch (IOException e) {
      throw new RuntimeException("error while parsing " + inputFile.getPath());
    }
    final ArrayList<Circle> circles = Lists.newArrayList();
    final JsonNode circlesNode = setup.get("circles");
    Verify.verify(circlesNode.isArray());
    for (final JsonNode jn : circlesNode) {
      final double x = jn.get("x").asDouble();
      final double y = jn.get("y").asDouble();
      final double r = jn.get("r").asDouble();
      final Circle c = new Circle(new Point(x, y), r);
      circles.add(c);
    }
    return new CompositeRegion<>(circles);
  }

  public static InputProvider feededFrom(final String baseDirPath) {
    final File file = new File(baseDirPath);
    Verify.verify(file.isDirectory());
    return new InputProvider(file);
  }
}
