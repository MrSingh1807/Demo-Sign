package com.pettracker.demosignature.utils

import android.graphics.Point
import android.view.View
import android.view.ViewGroup
import com.pettracker.demosignature.modals.ViewCoordinates
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


fun View.getCoordinates(): ViewCoordinates {

    val topLeft = IntArray(2)
    rootView.getLocationOnScreen(topLeft)

    val topLeftCoordinate = Point(topLeft.first(), topLeft.last())
    val bottomLeftCoordinate = Point(topLeft.first(), topLeft.last() + height)

    val topRightCoordinate = Point(topLeft.first() + width, topLeft.last())
    val bottomRightCoordinate = Point(topLeft.first() + width, topLeft.last() + height)

    val centerPoint = Point((topLeft.first() + width) / 2, (topLeft.last() + height) / 2)

    val topLineCenterCoordinates = calculateLineCenterPoint(topLeftCoordinate, topRightCoordinate)
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

fun calculateLineCenterPoint(point1: Point, point2: Point): Point {
    val x = (point1.x + point2.x) / 2.0
    val y = (point1.y + point2.y) / 2.0
    return Point(x.toInt(), y.toInt())
}

fun View.zoomView(diffPercentage: Int) {
    val width = layoutParams.width
    val height = layoutParams.height

    val newWidth = (diffPercentage * 100) / width
    val newHeight = (diffPercentage * 100) / height

    layoutParams = ViewGroup.LayoutParams(newWidth, newHeight)
}

fun View.updateView(newDistance: Int) {

    val allPoints = getCoordinates()
    val centerPoint = allPoints.center

    val newTopLeftPoint =
        calculateEndPoint(centerPoint, calculateAngle(centerPoint, allPoints.topLeft), newDistance)
    val newBottomLeftPoint =
        calculateEndPoint(
            centerPoint,
            calculateAngle(centerPoint, allPoints.bottomLeft),
            newDistance
        )
    val newBottomRightPoint =
        calculateEndPoint(
            centerPoint,
            calculateAngle(centerPoint, allPoints.bottomRight),
            newDistance
        )
    val newTopRightPoint =
        calculateEndPoint(centerPoint, calculateAngle(centerPoint, allPoints.topRight), newDistance)

    val leftLine = calculateDistance(newTopLeftPoint, newBottomLeftPoint)
    val topLine = calculateDistance(newTopLeftPoint, newTopRightPoint)
    val rightLine = calculateDistance(newTopRightPoint, newBottomRightPoint)
    val bottomLine = calculateDistance(newBottomLeftPoint, newBottomRightPoint)

    layout(leftLine, topLine, rightLine, bottomLine)
}

fun calculateDistance(point1: Point, point2: Point): Int {
    val x = (point2.x - point1.x).toDouble()
    val y = (point2.y - point1.y).toDouble()
    return sqrt(x * x + y * y).toInt()
}

fun calculateAngle(point1: Point, point2: Point): Double {
    val x = (point2.x - point1.x).toDouble()
    val y = (point2.y - point1.y).toDouble()
    val radians = atan2(y, x)
    return Math.toDegrees(radians)
}

fun calculateEndPoint(startPoint: Point, angle: Double, distance: Int): Point {
    val angleRadians = angle * PI / 180.0

    val dx = distance * cos(angleRadians)
    val dy = distance * sin(angleRadians)

    val endpointX = startPoint.x + dx
    val endpointY = startPoint.y + dy

    return Point(endpointX.toInt(), endpointY.toInt())
}

