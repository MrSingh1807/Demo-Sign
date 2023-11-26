package ja.burhanrashid52.photoeditor

import com.pettracker.demosignature.modals.Line
import com.pettracker.demosignature.modals.Point
import com.pettracker.demosignature.modals.ViewCoordinates
import ja.burhanrashid52.photoeditor.utils.CoordinatesUtil
import org.junit.Test


class CoordinateTest {

    val coordinateUtil = CoordinatesUtil()

    @Test
    fun calculateLineCenterPoint() {
        val centerPoint = coordinateUtil.calculateLineCenterPoint(Point(1.0, 2.0), Point(4.0, 4.0))

        println("centerPoint: $centerPoint")

    }

    @Test
    fun calculateDistance() {
        val distance = coordinateUtil.calculateDistance(Point(2.5, 3.0), Point(4.0, 2.0))
        val distance2 = coordinateUtil.calculateDistance(Point(3.0, 3.0), Point(5.0, 1.0))

        println(distance)
        println("Distance2 : $distance2")
    }

    @Test
    fun calculateAngle() {

//        val angle =
//            coordinateUtil.angleBetweenLines(Point(4.0, 4.0), Point(3.0, 3.0), Point(4.0, 2.0))

        val angle =
            coordinateUtil.angleBetweenLines(Point(4.0, 4.0), Point(3.0, 3.0), Point(4.0, 2.0))
        println("Angle: $angle")

    }

    @Test
    fun calculateEndPoint() {

        val point1 =
            coordinateUtil.calculateEndPoint(Line(Point(1.0, 2.0), Point(4.0, 2.0)), 2.0)

        val point =
            coordinateUtil.calculateEndPoint(Line(Point(2.5, 3.0), Point(4.0, 2.0)), 1.80)

        val point3 =
            coordinateUtil.calculateEndPoint(Line(Point(3.0, 3.0), Point(4.0, 2.0)), 1.50)

        println(
            "EntPoint : $point" +
                    "\nPoint1 : $point1" +
                    "\nPoint3 : $point3"
        )
    }

    @Test
    fun viewNewCoordinates() {
        val newCoordinates = coordinateUtil.viewNewCoordinates(
            ViewCoordinates(
                Point(2.0, 4.0), Point(2.0, 2.0),
                Point(4.0, 2.0), Point(4.0, 4.0),
                center = Point(3.0, 3.0)
            ), 2.82
        )

        newCoordinates.forEach {
            println("NewLineDistance: $it")
        }

    }


}