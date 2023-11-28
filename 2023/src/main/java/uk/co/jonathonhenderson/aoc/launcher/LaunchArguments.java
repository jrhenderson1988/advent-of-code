package uk.co.jonathonhenderson.aoc.launcher;

public record LaunchArguments(Days day, Parts part) {

  public static LaunchArguments of(String[] args) {
    if (args.length == 2) {
      return new LaunchArguments(parseDay(args[0]), parsePart(args[1]));
    } else if (args.length == 1) {
      return new LaunchArguments(parseDay(args[0]), new Parts.Both());
    } else if (args.length == 0) {
      return new LaunchArguments(new Days.All(), new Parts.Both());
    } else {
      throw new IllegalArgumentException("Unexpected number of arguments");
    }
  }

  private static Days parseDay(String value) {
    if (value.equals("*")) {
      return Days.all();
    } else {
      try {
        var day = Integer.parseInt(value);
        return Days.single(day);
      } catch (NumberFormatException ex) {
        throw new IllegalArgumentException("Unexpected day");
      }
    }
  }

  private static Parts parsePart(String value) {
    return switch (value) {
      case "*" -> Parts.both();
      case "1" -> Parts.first();
      case "2" -> Parts.second();
      default -> throw new IllegalArgumentException("Unexpected part");
    };
  }
}
