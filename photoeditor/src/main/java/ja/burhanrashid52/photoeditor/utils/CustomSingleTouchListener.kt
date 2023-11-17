package ja.burhanrashid52.photoeditor.utils

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.graphics.Matrix
import android.graphics.PointF
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.View.ROTATION
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.sqrt

class CustomSingleTouchListener(val rootView: View) : OnTouchListener {

    private val rotationThreshold =
        10f // This value determines the minimum angle difference for rotation

    private var midPoint = PointF()
    private var previousDifference: Double = 0.0


    //    var startTouchX: Float = 0f
//    var startTouchY: Float = 0f
    var startScale: Float = 1f
    var startAngle: Float = 0f

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        logDebug("Action: ACTION_POINTER_DOWN")

        when (event?.action) {
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

//                val touchAngle = calculateRotation(event)
                val touchAngle = atan2(deltaY, deltaX) * 360f / Math.PI

                val angleDifference = abs(touchAngle - startAngle)

//                // Animate the rotation
//                ObjectAnimator.ofFloat(rootView, ROTATION, startAngle, touchAngle.toFloat())
//                    .setDuration(150).start()

                if (angleDifference > rotationThreshold) {
                    // Animate the rotation
                    ObjectAnimator.ofFloat(rootView, ROTATION, startAngle, touchAngle.toFloat())
                        .setDuration(150).start()
                    // Rotate the image
//                rootView.rotation = startAngle + angle.toFloat()

                    // Update starting angle for next touch move event
                    startAngle = touchAngle.toFloat()
                }

                // Update starting coordinates for next touch move event
                midPoint = calculateMidPoint(event)
                startScale = rootView.scaleX
                previousDifference = distanceFromCenter

                logDebug("Action: ACTION_MOVE")
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