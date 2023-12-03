package ja.burhanrashid52.photoeditor.utils

import android.graphics.PointF
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import kotlin.math.atan2
import kotlin.math.sqrt


class RotateTouchListener(var rootView: View) : OnTouchListener {

    private var midPoint = PointF()
    private var previousDifference: Double = 0.0

    private var startScale: Float = 1f
    private var startAngle: Float = 0f

    private var rotationAngle: Float = 0f

    override fun onTouch(p0: View?, event: MotionEvent): Boolean {

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

                val touchAngle = atan2(deltaY, deltaX) * 360f / Math.PI

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
                previousDifference = distanceFromCenter

                // Rotate the parent FrameLayout
                rootView.rotation = rotationAngle
                logDebug("Action: ACTION_MOVE")
                return true
            }

        }

        Log.d("Mr_Singh", "onTouchEvent: Reached ")
        return true
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
