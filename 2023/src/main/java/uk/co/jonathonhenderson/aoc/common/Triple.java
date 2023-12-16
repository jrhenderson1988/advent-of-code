package uk.co.jonathonhenderson.aoc.common;

public record Triple<A, B, C>(A first, B second, C third) {
  public static <A, B, C> Triple<A, B, C> of(A first, B second, C third) {
    return new Triple<>(first, second, third);
  }

  public A left() {
    return first();
  }

  public B middle() {
    return second();
  }

  public C right() {
    return third();
  }
}
