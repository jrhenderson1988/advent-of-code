package uk.co.jonathonhenderson.adventofcode.utils

import java.nio.file.Files
import java.nio.file.Path

class Utils {
    static String readPuzzle(int day) {
        def path = Path.of("puzzles", "${day}.txt")
        def absolute = path.toAbsolutePath()
        readFile(absolute)
    }

    static String readFile(Path path) {
        Files.readString(path)
    }

    static int mathMod(int a, int n) {
        ((a % n) + n) % n
    }

    static List<String> splitByEmptyLines(String content) {
        content.split("((\r\n|\r|\n)\\s*){2}").collect { it }
    }
}
