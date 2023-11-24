package com.pettracker.demosignature.utils

import android.annotation.SuppressLint
import android.graphics.PointF
import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlin.math.atan2
import kotlin.math.sqrt

class CustomSingleTouchListener2(val rootView: View) : View.OnTouchListener {

    private var midPoint = PointF()
    private var previousDifference: Double = 0.0

    var startScale: Float = 1f
    var startAngle: Float = 0f

    private var lastX: Float = 0f
    private var rotationAngle: Float = 0f

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(view: View?, event: MotionEvent): Boolean {
        ja.burhanrashid52.photoeditor.utils.logDebug("Action: ACTION_POINTER_DOWN")
//        val x = event.x

        val rootX = rootView.x
        val rootY = rootView.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                calculateMidPoint(event)

                lastX = rootX

                startScale = rootView.scaleX
                startAngle = rootView.rotation

                ja.burhanrashid52.photoeditor.utils.logDebug("Action: action Down")
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                val currentTouchX = event.rawX
                val currentTouchY = event.rawY

                // Calculate the delta in touch coordinates
                val deltaX = currentTouchX - midPoint.x
                val deltaY = currentTouchY - midPoint.y


                // Calculate the distance from the center point
                val distanceFromCenter =
                    sqrt(deltaX * deltaX + (deltaY * deltaY).toDouble())

                ja.burhanrashid52.photoeditor.utils.logInfo("DistanceFromCenter: $distanceFromCenter")

                // Zoom based on distance from center point
                val zoomFactor = if ((distanceFromCenter - previousDifference) > 0) 1.02f else 0.98f
                rootView.scaleX = startScale * zoomFactor
                rootView.scaleY = startScale * zoomFactor


                val touchAngle = atan2(deltaY, deltaX) * 360f / Math.PI

//                val delX = rootX - lastX
                // Rotate the view based on the drag direction
                if (touchAngle > startAngle) {
                    // Clockwise rotation
                    rotationAngle += 3
                } else {
                    // Anti-clockwise rotation
                    rotationAngle -= 3
                }

                // Update starting coordinates for next touch move event
                midPoint = calculateMidPoint(event)
                startScale = rootView.scaleX
                previousDifference = distanceFromCenter
                lastX = rootX

                // Rotate the parent FrameLayout
                rootView.rotation = rotationAngle
                ja.burhanrashid52.photoeditor.utils.logDebug("Action: ACTION_MOVE")
                return true
            }

        }

        Log.d("Mr_Singh", "onTouchEvent: Reached ")
        return false
    }

    protected fun calculateRotation(event: MotionEvent?): Float {
        return if (event == null || event.pointerCount < 2) {
            0f
        } else calculateRotation(event.getX(0), event.getY(0), event.getX(1), event.getY(1))
    }

    protected fun calculateRotation(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        val x = (x1 - x2).toDouble()
        val y = (y1 - y2).toDouble()
        val radians = atan2(y, x)
        return Math.toDegrees(radians).toFloat()
    }

    /**
     * calculate Distance in two fingers
     */
    fun calculateDistance(event: MotionEvent?): Float {
        return if (event == null || event.pointerCount < 2) {
            0f
        } else calculateDistance(event.getX(0), event.getY(0), event.getX(1), event.getY(1))
    }

    private fun calculateDistance(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        val x = (x1 - x2).toDouble()
        val y = (y1 - y2).toDouble()
        return sqrt(x * x + y * y).toFloat()
    }

    private fun calculateMidPoint(event: MotionEvent?): PointF {
        if (event == null || event.pointerCount < 2) {
            midPoint[0f] = 0f
            return midPoint
        }
        val x = (event.getX(0) + event.getX(1)) / 2
        val y = (event.getY(0) + event.getY(1)) / 2
        midPoint[x] = y
        return midPoint
    }
}