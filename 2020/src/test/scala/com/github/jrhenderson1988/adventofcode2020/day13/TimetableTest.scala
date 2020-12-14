package com.github.jrhenderson1988.adventofcode2020.day13

import org.scalatest.FunSuite

import scala.util.control.Breaks.{break, breakable}

class TimetableTest extends FunSuite {
  val input: String = "939\n7,13,x,x,59,x,31,19"
  val timetable: Timetable = Timetable.parse(input.split("\\n").toList)

  test("parse") {
    assert(timetable == Timetable(939, List(7, 13, -1, -1, 59, -1, 31, 19)))
  }

  test("earliestBusTimesWaitTime") {
    assert(timetable.earliestBusTimesWaitTime() == 295)
  }

  test("earliestTimestampWhereBusesDepartSequentially") {
//    assert(timetable.earliestTimestampWhereBusesDepartSequentially() == 1068781)
    assert(Timetable(0, List(17, -1, 13, 19)).earliestTimestampWhereBusesDepartSequentially() == 3417)
//    assert(Timetable(0, List(67, 7, 59, 61)).earliestTimestampWhereBusesDepartSequentially() == 754018)
//    assert(Timetable(0, List(67, -1, 7, 59, 61)).earliestTimestampWhereBusesDepartSequentially() == 779210)
//    assert(Timetable(0, List(67, 7, -1, 59, 61)).earliestTimestampWhereBusesDepartSequentially() == 1261476)
//    assert(Timetable(0, List(1789, 37, 47, 1889)).earliestTimestampWhereBusesDepartSequentially() == 1202161486)
  }
}
