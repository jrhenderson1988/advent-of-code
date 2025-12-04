package uk.co.jonathonhenderson.adventofcode.utils

import groovy.transform.Canonical
import groovy.transform.EqualsAndHashCode

@Canonical
@EqualsAndHashCode
class Coord {
    final int y
    final int x

    Coord(int y, int x) {
        this.y = y
        this.x = x
    }

    static Coord of(int y, int x) {
        new Coord(y, x)
    }

    @Override
    String toString() {
        "(y=$y, x=$x)"
    }

    List<Coord> adjacent() {
        [-1, 0, 1].collectMany { dy -> [-1, 0, 1].collect { dx -> of(dy, dx) } }
                .findAll { delta -> !(delta.x == 0 && delta.y == 0) }
                .collect { delta -> of(this.y + delta.y, this.x + delta.x) }
    }
}
