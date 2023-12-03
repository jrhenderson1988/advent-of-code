package uk.co.jonathonhenderson.aoc.common;

public record Pair<A, B>(A first, B second) {
  public static <A, B> Pair<A, B> of(A a, B b) {
    return new Pair<>(a, b);
  }

  public A left() {
    return first();
  }

  public B right() {
    return second();
  }
}
