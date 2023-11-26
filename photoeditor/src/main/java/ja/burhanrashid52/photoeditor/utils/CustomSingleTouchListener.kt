package ja.burhanrashid52.photoeditor.utils

import android.annotation.SuppressLint
import android.graphics.PointF
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import com.pettracker.demosignature.modals.Point
import com.pettracker.demosignature.modals.ViewCoordinates
import kotlin.math.atan2
import kotlin.math.sqrt

class CustomSingleTouchListener(val rootView: View) : OnTouchListener {

    private var midPoint = PointF()
    private var previousDifference: Float = 0f

    var startScale: Float = 1f
    var startAngle: Float = 0f

    var startX = 0f
    var startY = 0f

    val coordinatesUtil = CoordinatesUtil()
    var allPoints: ViewCoordinates? = null

    private var rotationAngle: Float = 0f

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(view: View?, event: MotionEvent): Boolean {
        logDebug("Action: ACTION_POINTER_DOWN")



        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                midPoint = calculateMidPoint(event)

                startX = event.x
                startY = event.y

                startScale = rootView.scaleX
                startAngle = rootView.rotation

                allPoints = coordinatesUtil.getCoordinates(rootView)

                logDebug("Action: action Down")
                return true
            }

            MotionEvent.ACTION_MOVE -> {

                // Update the view area
                /* val points = rootView.getCoordinates()
                 val distance = calculateDistance(points.center, points.bottomRight)

                 // Calculate the distance from the center point
                 val differenceDistance = event.calculateDistance(startX, startY)
                 val newDistance = distance + differenceDistance.toInt()


                 rootView.updateView(newDistance)

                 logInfo(
                     "PreviousDistance = $distance " +
                             "\nDifference = $differenceDistance" +
                             "\nNewDistance = $newDistance"
                 )*/

                /*               // Zoom based on distance from center point
                               val zoomFactor = if ((distanceFromCenter - previousDifference) > 0) 1.05f else 0.95f
               //                val zoomFactor = diffPercentage(distanceFromCenter)
                               rootView.scaleX = startScale * zoomFactor
                               rootView.scaleY = startScale * zoomFactor
               */

                /* val touchAngle = atan2(deltaY, deltaX) * 360f / Math.PI

 //                val delX = rootX - lastX
                 // Rotate the view based on the drag direction
                 if (touchAngle > startAngle) {
                     // Clockwise rotation
                     rotationAngle += 3
                 } else {
                     // Anti-clockwise rotation
                     rotationAngle -= 3
                 }
 */

                allPoints = coordinatesUtil.getCoordinates(rootView)

                val newDistance = coordinatesUtil.calculateDistance(
                    allPoints!!.center,
                    Point(event.x.toDouble(), event.y.toDouble())
                )

                val newCoordinates = coordinatesUtil.viewNewCoordinates(allPoints!!, newDistance)
                rootView.layout(
                    newCoordinates.component1().beInt(), newCoordinates.component2().beInt(),
                    newCoordinates.component3().beInt(), newCoordinates.component4().beInt()
                )



                // Update starting coordinates for next touch move event
                midPoint = calculateMidPoint(event)
                startScale = rootView.scaleX
//                previousDifference = differenceDistance

                // Rotate the parent FrameLayout
//                rootView.rotation = rotationAngle
                logDebug("Action: ACTION_MOVE")
                return true
            }

            MotionEvent.ACTION_UP -> {
                // Update the view area
                rootView.invalidate()
            }

        }

        Log.d("Mr_Singh", "onTouchEvent: Reached ")
        return false
    }

    private fun MotionEvent.calculateDistance(x2: Float, y2: Float): Float {
        return calculateDistance(rawX, rawY, x2, y2)
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
    private fun calculateDistance(event: MotionEvent?): Float {
        return if (event == null || event.pointerCount < 2) {
            0f
        } else calculateDistance(event.getX(0), event.getY(0), event.getX(1), event.getY(1))
    }


    fun MotionEvent.calculateDiffPercentage(value: Float): Float {
        val calculateDistance = calculateDistance(this)

        val diffPercentage = (calculateDistance * 100) / value
        return diffPercentage * 100
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

    private fun diffPercentage(value: Float): Float {
        val percentage = if (previousDifference == 0f) 100f
        else ((value * 100) / previousDifference)

        return percentage / 100
    }

}

