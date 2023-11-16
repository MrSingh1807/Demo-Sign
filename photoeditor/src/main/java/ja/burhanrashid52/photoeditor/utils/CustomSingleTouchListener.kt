package ja.burhanrashid52.photoeditor.utils

import android.annotation.SuppressLint
import android.graphics.Matrix
import android.graphics.PointF
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import kotlin.math.atan2
import kotlin.math.sqrt

class CustomSingleTouchListener(val rootView: View) : OnTouchListener {

    private var midPoint = PointF()
    private val moveMatrix = Matrix()
    private val downMatrix = Matrix()

    private var oldDistance = 0f
    private var oldRotation = 0f


    var startTouchX: Float = 0f
    var startTouchY: Float = 0f
    var startScale: Float = 1f
    var startAngle: Float = 0f

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        logDebug("Action: ACTION_POINTER_DOWN")
        val action = event?.action

        when (action) {
            MotionEvent.ACTION_POINTER_DOWN -> {
                oldDistance = calculateDistance(event)
                oldRotation = calculateRotation(event)

                midPoint = calculateMidPoint(event)

                logDebug("Action: ACTION_POINTER_DOWN")
            }

            MotionEvent.ACTION_DOWN -> {
                startTouchX = /*event.rawX*/ rootView.x + rootView.width / 2
                startTouchY = /*event.rawY*/ rootView.y + rootView.width / 2
                startScale = rootView.scaleX
                startAngle = rootView.rotation

                logDebug("Action: action Down")
                return true
            }

            MotionEvent.ACTION_MOVE -> {
//                val centerX = rootView.x + rootView.width / 2
//                val centerY = rootView.y + rootView.width / 2

                val currentTouchX = event.rawX
                val currentTouchY = event.rawY

                // Calculate the delta in touch coordinates
                val deltaX = currentTouchX - startTouchX
                val deltaY = currentTouchY - startTouchY

                // Calculate the distance from the center point
                val distanceFromCenter =
                    sqrt(deltaX * deltaX + (deltaY * deltaY).toDouble())


                // Zoom based on distance from center point
                val zoomFactor = if (distanceFromCenter > 0) 1.01f else 0.99f
                rootView.scaleX = startScale * zoomFactor
                rootView.scaleY = startScale * zoomFactor

                // Calculate rotation angle
                val angle =
                    atan2(deltaY.toDouble(), deltaX.toDouble()) * 180f / Math.PI

                // Rotate the image
                rootView.rotation = startAngle + angle.toFloat()

                // Update starting coordinates for next touch move event
                startTouchX = currentTouchX
                startTouchY = currentTouchY
                startScale = rootView.scaleX
                startAngle = rootView.rotation


                logDebug("Action: ACTION_MOVE")
                return true
            }
        }

        Log.d("Mr_Singh", "onTouchEvent: Reached ")
        return false
    }

    fun zoomAndRotateSticker(view: View?, event: MotionEvent) {
        if (view != null) {
            Log.d("Mr_Singh", "zoomAndRotateSticker: Reached")
            val newDistance: Float = calculateDistance(midPoint.x, midPoint.y, event.x, event.y)
            val newRotation: Float = calculateRotation(midPoint.x, midPoint.y, event.x, event.y)
            moveMatrix.set(downMatrix)
            moveMatrix.postScale(
                newDistance / oldDistance, newDistance / oldDistance, midPoint.x,
                midPoint.y
            )
            moveMatrix.postRotate(newRotation - oldRotation, midPoint.x, midPoint.y)
//            view.setMatrix(moveMatrix)
        }
    }

    protected fun calculateRotation(event: MotionEvent?): Float {
        return if (event == null || event.pointerCount < 2) {
            0f
        } else calculateRotation(event.getX(0), event.getY(0), event.getX(1), event.getY(1))
    }

    protected fun calculateRotation(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        val x = (x1 - x2).toDouble()
        val y = (y1 - y2).toDouble()
        val radians = Math.atan2(y, x)
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

    fun calculateDistance(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        val x = (x1 - x2).toDouble()
        val y = (y1 - y2).toDouble()
        return sqrt(x * x + y * y).toFloat()
    }

    fun calculateMidPoint(event: MotionEvent?): PointF {
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