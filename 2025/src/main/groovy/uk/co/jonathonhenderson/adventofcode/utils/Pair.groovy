package uk.co.jonathonhenderson.adventofcode.utils

import groovy.transform.Canonical

@Canonical
class Pair<A, B> {
    final A first
    final B second

    A first() {
        this.first
    }

    B second() {
        this.second
    }

    @Override
    String toString() {
        "Pair($first, $second)"
    }

    static <A, B> Pair<A, B> of(A first, B second) {
        new Pair<>(first, second)
    }
}
