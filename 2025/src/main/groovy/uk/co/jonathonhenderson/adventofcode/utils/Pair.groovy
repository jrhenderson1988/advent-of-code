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

    static <FIRST, SECOND> Pair<FIRST, SECOND> of(FIRST first, SECOND second) {
        new Pair<>(first, second)
    }
}
