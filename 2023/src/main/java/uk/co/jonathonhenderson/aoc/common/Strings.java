package uk.co.jonathonhenderson.aoc.common;

public final class Strings {
  public static String stripTrailing(String s, char... c) {
    if (c.length == 0) {
      return s;
    }

    var i = s.length() - 1;
    while (i >= 0) {
      if (!contains(c, s.charAt(i))) {
        break;
      }
      i--;
    }
    return s.substring(0, i + 1);
  }

  public static String stripLeading(String s, char... c) {
    if (c.length == 0) {
      return s;
    }

    var i = 0;
    while (i < s.length()) {
      if (!contains(c, s.charAt(i))) {
        break;
      }

      i++;
    }
    return s.substring(i);
  }

  public static String strip(String s, char... c) {
    return stripTrailing(stripLeading(s, c), c);
  }

  private static boolean contains(char[] chars, char c) {
    for (var ch : chars) {
      if (ch == c) {
        return true;
      }
    }
    return false;
  }
}
