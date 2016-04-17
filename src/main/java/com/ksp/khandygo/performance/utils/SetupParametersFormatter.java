package com.ksp.khandygo.performance.utils;

public class SetupParametersFormatter {

  public static void print(
      final String inputDirPath,
      final int trials,
      final int mcSampleSize,
      final int parallelismLevel) {
    print(inputDirPath, trials, mcSampleSize);
    System.out.format("\tPARALLELISM LEVEL = %d%n", parallelismLevel);
  }

  public static void print(
      final String inputDirPath,
      final int trials,
      final int mcSampleSize) {
    System.out.format("SETUP:%n");
    System.out.format("\tINPUT DIR = %s%n", inputDirPath);
    System.out.format("\tTRIALS = %d%n", trials);
    System.out.format("\tESTIMATION SAMPLE SIZE = %d%n", mcSampleSize);
  }
}
