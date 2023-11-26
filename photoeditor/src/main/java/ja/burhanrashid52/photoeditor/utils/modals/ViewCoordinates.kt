package com.pettracker.demosignature.modals


data class ViewCoordinates(
    val topLeft: Point,
    val bottomLeft: Point,
    val bottomRight: Point,
    val topRight: Point,

    val center: Point,

    val topLineCenter: Point,
    val leftLineCenter: Point,
    val bottomLineCenter: Point,
    val rightLineCenter: Point
) {
    constructor(
        topLeft: Point, bottomLeft: Point, bottomRight: Point, topRight: Point, center: Point
    ) : this(
        topLeft,
        bottomLeft,
        bottomRight,
        topRight,
        center,
        Point(0.0, 0.0),
        Point(0.0, 0.0),
        Point(0.0, 0.0),
        Point(0.0, 0.0),
    )
}

data class Point(val x: Double, val y: Double)

data class Line(val startPoint: Point, val endPoint: Point)
