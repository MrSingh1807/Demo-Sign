package ja.burhanrashid52.photoeditor.utils

import android.view.View
import com.pettracker.demosignature.modals.Line
import com.pettracker.demosignature.modals.Point
import com.pettracker.demosignature.modals.ViewCoordinates
import kotlin.math.acos
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin
import kotlin.math.sqrt

class CoordinatesUtil {

    fun getCoordinates(view: View): ViewCoordinates {
        val coordinates = IntArray(2)
        val topLeft = arrayOf(coordinates.first().toDouble(), coordinates.last().toDouble())
        val (width, height) = Pair(view.width, view.height)
        view.getLocationOnScreen(coordinates)

        val topLeftCoordinate = Point(topLeft.first(), topLeft.last())
        val bottomLeftCoordinate = Point(topLeft.first(), topLeft.last() + height)

        val topRightCoordinate = Point(topLeft.first() + width, topLeft.last())
        val bottomRightCoordinate = Point(topLeft.first() + width, topLeft.last() + height)

        val centerPoint = Point((topLeft.first() + width) / 2, (topLeft.last() + height) / 2)

        val topLineCenterCoordinates =
            calculateLineCenterPoint(topLeftCoordinate, topRightCoordinate)
        val leftLineCenterCoordinates =
            calculateLineCenterPoint(topLeftCoordinate, bottomLeftCoordinate)
        val bottomLineCenterCoordinates =
            calculateLineCenterPoint(bottomLeftCoordinate, bottomRightCoordinate)
        val rightLineCenterCoordinates =
            calculateLineCenterPoint(topRightCoordinate, bottomRightCoordinate)

        return ViewCoordinates(
            topLeftCoordinate, bottomLeftCoordinate,
            bottomRightCoordinate, topRightCoordinate,
            centerPoint,
            topLineCenterCoordinates, leftLineCenterCoordinates,
            bottomLineCenterCoordinates, rightLineCenterCoordinates
        )
    }

    fun calculateLineCenterPoint(point1: Point, point2: Point): Point { // Verified
        val x = (point1.x + point2.x) / 2.0
        val y = (point1.y + point2.y) / 2.0
        return Point(x, y)
    }

    fun View.updatedViewParams(diffPercentage: Int): Pair<Int, Int> {
        val width = layoutParams.width
        val height = layoutParams.height

        val newWidth = (diffPercentage * 100) / width
        val newHeight = (diffPercentage * 100) / height

        return Pair(newHeight, newWidth)
    }

    fun viewNewCoordinates(allPoints: ViewCoordinates, newDistance: Double): List<Double> {

        val centerPoint = allPoints.center

        val newTopLeftPoint =
            calculateEndPoint(
                line = Line(centerPoint, allPoints.topLeft),
//                angleBetweenLines(allPoints.topRight, centerPoint, allPoints.topLeft),
                newDistance
            )
        val newBottomLeftPoint =
            calculateEndPoint(
                line = Line(centerPoint, allPoints.bottomLeft),
//                angleBetweenLines(allPoints.topLeft, centerPoint, allPoints.bottomLeft),
                newDistance
            )
        val newBottomRightPoint =
            calculateEndPoint(
                line = Line(centerPoint, allPoints.bottomRight),
//                angleBetweenLines(allPoints.bottomLeft, centerPoint, allPoints.bottomRight),
                newDistance
            )
        val newTopRightPoint =
            calculateEndPoint(
                line = Line(centerPoint, allPoints.topRight),
//                angleBetweenLines(allPoints.bottomRight, centerPoint, allPoints.topRight),
                newDistance
            )

        println(
            "New TopLeft : $newTopLeftPoint" +
                    "\n BottomLeft: $newBottomLeftPoint" +
                    "\n BottomRight: $newBottomRightPoint" +
                    "\n TopRight: $newTopRightPoint"
        )

        val leftLine = calculateDistance(newTopLeftPoint, newBottomLeftPoint)
        val topLine = calculateDistance(newTopLeftPoint, newTopRightPoint)
        val rightLine = calculateDistance(newTopRightPoint, newBottomRightPoint)
        val bottomLine = calculateDistance(newBottomLeftPoint, newBottomRightPoint)

        return listOf(leftLine, topLine, rightLine, bottomLine)
    }

    fun calculateDistance(point1: Point, point2: Point): Double { // verified
        val x = (point2.x - point1.x)
        val y = (point2.y - point1.y)
        return sqrt(x * x + y * y)
    }

    fun calculateEndPoint(line: Line,/* angle: Double,*/ distance: Double): Point { // verified

        val inclination = Math.toDegrees(
            atan2(
                line.endPoint.y - line.startPoint.y,
                line.endPoint.x - line.startPoint.x
            )
        )
//        val updateAngle = angle + inclination
//        val newAngle = if (updateAngle > 0) updateAngle else 360 + updateAngle

        val newAngle = if (inclination < 0) 360 + inclination else inclination

        println(
            "Inclination: $inclination \n" +
                    "NewAngle: $newAngle"
        )
        val angleRadians = Math.toRadians(newAngle)

        val dx = distance * cos(angleRadians)
        val dy = distance * sin(angleRadians)

        val endpointX = line.startPoint.x + dx
        val endpointY = line.startPoint.y + dy

        return Point(endpointX, endpointY)
    }

    fun angleBetweenLines(pointA: Point, meetingPoint: Point, pointC: Point): Double {  // Verified
        /*
        * formula:
        *  θ = arc.cos [(vectorAB * vectorBC) / \vectorAB\ . \vectorBC\ ]
        * */
        val vectorAB = Point(meetingPoint.x - pointA.x, meetingPoint.y - pointA.y)
        val vectorBC = Point(pointC.x - meetingPoint.x, pointC.y - meetingPoint.y)

        val dotProduct = vectorAB.x * vectorBC.x + vectorAB.y * vectorBC.y
        val magnitudeAB = hypot(vectorAB.x, vectorAB.y)
        val magnitudeBC = hypot(vectorBC.x, vectorBC.y)

        val cosTheta = dotProduct / (magnitudeAB * magnitudeBC)

        // Use acos to get the angle in radians
        val angleRad = acos(cosTheta)

        // Convert radians to degrees
        return Math.toDegrees(angleRad)
    }

}

