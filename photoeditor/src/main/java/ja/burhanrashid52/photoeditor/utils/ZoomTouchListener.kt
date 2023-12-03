package ja.burhanrashid52.photoeditor.utils

import android.annotation.SuppressLint
import android.graphics.PointF
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import kotlin.math.atan2
import kotlin.math.sqrt

class ZoomTouchListener(val rootView: View) : OnTouchListener {

    private var midPoint = PointF()
    private var previousDifference: Double = 0.0

    private var startScale: Float = 1f
    private var startAngle: Float = 0f

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(view: View?, event: MotionEvent): Boolean {
        logDebug("Action: ACTION_POINTER_DOWN")

        when (event.action) {

            MotionEvent.ACTION_DOWN -> {
                calculateMidPoint(event)

                startScale = rootView.scaleX
                startAngle = rootView.rotation

                logDebug("Action: action Down")
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

                logInfo("DistanceFromCenter: $distanceFromCenter")

                // Zoom based on distance from center point
                val zoomFactor = if ((distanceFromCenter - previousDifference) > 0) 1.05f else 0.95f
                rootView.scaleX = startScale * zoomFactor
                rootView.scaleY = startScale * zoomFactor


                // Update starting coordinates for next touch move event
                midPoint = calculateMidPoint(event)
                startScale = rootView.scaleX
                previousDifference = distanceFromCenter

                // Rotate the parent FrameLayout
                logDebug("Action: ACTION_MOVE")
                return true
            }
        }

        Log.d("Mr_Singh", "onTouchEvent: Reached ")
        return false
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