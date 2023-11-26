package com.pettracker.demosignature.modals

import android.graphics.Point


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
)
