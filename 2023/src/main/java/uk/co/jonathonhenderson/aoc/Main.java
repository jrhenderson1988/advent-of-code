package uk.co.jonathonhenderson.aoc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import uk.co.jonathonhenderson.aoc.days.Day;
import uk.co.jonathonhenderson.aoc.launcher.Days;
import uk.co.jonathonhenderson.aoc.launcher.Days.All;
import uk.co.jonathonhenderson.aoc.launcher.Days.Single;
import uk.co.jonathonhenderson.aoc.launcher.LaunchArguments;
import uk.co.jonathonhenderson.aoc.launcher.Parts;
import uk.co.jonathonhenderson.aoc.launcher.Parts.Both;
import uk.co.jonathonhenderson.aoc.launcher.Parts.First;
import uk.co.jonathonhenderson.aoc.launcher.Parts.Second;

public class Main {
  private static final String DAYS_PACKAGE_NAME = "days";
  private static final int MAX_DAYS = 25;

  public static void main(String[] args) {
    execute(LaunchArguments.of(args));
  }

  private static void execute(LaunchArguments args) {
    switch (args.day()) {
      case All ignored -> IntStream.rangeClosed(1, MAX_DAYS)
          .forEach(d -> executeDay(Days.single(d), args.part()));
      case Single single -> executeDay(single, args.part());
    }
  }

  private static void executeDay(Days.Single day, Parts part) {
    try {
      var instance = createInstance(day);
      System.out.printf("%n=== Day %d ===%n", day.day());
      executeParts(instance, part);
    } catch (ClassNotFoundException ignored) {
    } catch (NoSuchMethodException
        | InvocationTargetException
        | InstantiationException
        | IllegalAccessException
        | IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static void executeParts(Day day, Parts part) {
    switch (part) {
      case Both ignored -> {
        executeParts(day, Parts.first());
        executeParts(day, Parts.second());
      }
      case First ignored -> executeAndPrint("Part 1", day::part1);
      case Second ignored -> executeAndPrint("Part 2", day::part2);
    }
  }

  private static void executeAndPrint(String label, Supplier<Optional<String>> fn) {
    var start = System.currentTimeMillis();
    var result = fn.get().orElse("Skipped");
    var end = System.currentTimeMillis();
    var duration = end - start;

    System.out.printf("> %s (%dms): %s%n", label, duration, result);
  }

  private static String loadInput(Days.Single day) throws IOException {
    var filename = getFilename(day);
    var path = Paths.get(filename);
    if (path.toFile().isFile()) {
      return loadInputFromFilesystem(path);
    }

    return loadInputFromResource(filename);
  }

  private static String loadInputFromResource(String filename) throws IOException {
    try (var stream = Main.class.getClassLoader().getResourceAsStream(filename)) {
      if (stream == null) {
        throw new IOException("No input resource exists with filename: %s".formatted(filename));
      }

      return new BufferedReader(new InputStreamReader(stream))
          .lines()
          .parallel()
          .collect(Collectors.joining("\n"));
    }
  }

  private static String loadInputFromFilesystem(Path path) throws IOException {
    return Files.readString(path);
  }

  private static String getFilename(Days.Single day) {
    return "day%02d.txt".formatted(day.day());
  }

  private static String getClassName(Days.Single day) {
    return "%s.%s.Day%02d".formatted(Main.class.getPackageName(), DAYS_PACKAGE_NAME, day.day());
  }

  private static Day createInstance(Days.Single day)
      throws ClassNotFoundException,
          NoSuchMethodException,
          InvocationTargetException,
          InstantiationException,
          IllegalAccessException,
          IOException {
    var className = getClassName(day);
    var clazz = Class.forName(className);
    var constructor = clazz.getConstructor(String.class);
    var input = loadInput(day);
    var instance = constructor.newInstance(input);

    return (Day) instance;
  }
}
