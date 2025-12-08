package uk.co.jonathonhenderson.adventofcode.days


import static org.junit.jupiter.api.Assertions.assertEquals

class DayTest {
    def expectPart1(String expected) {
        expectPart1(input() as String, expected)
    }

    def expectPart1(String content, String expected) {
        expectPart1(createSubjectUnderTest(content), expected)
    }

    def expectPart1(Day day, String expected) {
        def result = day.part1()
        assertEquals(expected, result)
    }

    def expectPart2(String expected) {
        expectPart2(input() as String, expected)
    }

    def expectPart2(String content, String expected) {
        expectPart2(createSubjectUnderTest(content), expected)
    }

    def expectPart2(Day day, String expected) {
        def result = day.part2()
        assertEquals(expected, result)
    }

    Day createSubjectUnderTest(String content) {
        def testClassName = this.getClass().name
        if (!testClassName.endsWith("Test")) {
            throw new IllegalStateException("Unexpected class name '${testClassName}'")
        }
        def clazz = Class.forName(testClassName.substring(0, testClassName.length() - 4))
        def constructor = clazz.getDeclaredConstructor(String.class)

        def day = constructor.newInstance(content) as Day
        day.setTest(true)

        day
    }

    def input() {
        throw new IllegalStateException("Input must be provided")
    }
}
