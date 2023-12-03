package ja.burhanrashid52.photoeditor.utils

import android.annotation.SuppressLint
import android.graphics.Matrix
import android.graphics.PointF
import android.os.SystemClock
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.sqrt


class CSTouchListener(var rootView: View) : OnTouchListener {

    private var downX = 0f
    private var downY = 0f

    private var sizeMatrix = Matrix()
    private var downMatrix = Matrix()
    private var moveMatrix = Matrix()

    private var midPoint = PointF()

    private var oldDistance = 0f
    private var oldRotation = 0f

    private var currentCenterPoint = PointF()
    private var point = FloatArray(2)
    private var tmp = FloatArray(2)


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(view: View, event: MotionEvent): Boolean {

        when (event.action) {
            MotionEvent.ACTION_DOWN -> onTouchDown(event)
            MotionEvent.ACTION_POINTER_DOWN -> onTouchActionPointerDown(event)
            MotionEvent.ACTION_MOVE -> onTouchMove(event)
            MotionEvent.ACTION_UP -> {}

        }

        return false
    }

    private fun onTouchDown(event: MotionEvent) {
        downX = event.x
        downY = event.y

        midPoint = calculateMidPoint()
        oldDistance = calculateDistance(midPoint.x, midPoint.y, downX, downY)
        oldRotation = calculateRotation(midPoint.x, midPoint.y, downX, downY)

        downMatrix.set(rootView.matrix)
        rootView.invalidate()
    }

    private fun onTouchActionPointerDown(event: MotionEvent) {
        oldDistance = calculateDistance(event)
        oldRotation = calculateRotation(event)

        midPoint = calculateMidPoint(event)
    }
    private fun onTouchMove(event: MotionEvent) {
        moveMatrix.set(downMatrix)
        moveMatrix.postTranslate(event.x - downX, event.y - downY)
        rootView.matrix.set(moveMatrix)

        constrainSticker(rootView)
    }

    private fun constrainSticker(view: View) {
        var (moveX, moveY) = Pair(0f, 0f)
        var (width, height) = Pair(rootView.width, rootView.height)

        rootView.getMappedCenterPoint(currentCenterPoint, point, tmp)
        if (currentCenterPoint.x < 0) {
            moveX = -currentCenterPoint.x
        }
        if (currentCenterPoint.x > width) {
            moveX = width - currentCenterPoint.x
        }
        if (currentCenterPoint.y < 0) {
            moveY = -currentCenterPoint.y
        }
        if (currentCenterPoint.y > height) {
            moveY = height - currentCenterPoint.y
        }

        view.matrix.postTranslate(moveX, moveY)
    }

    private fun calculateMidPoint(): PointF {
        if (rootView == null) {
            midPoint[0f] = 0f
            return midPoint
        }
        midPoint.set(rootView.x, rootView.y)
        return midPoint
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

    /**
     * calculate rotation in line with two fingers and x-axis
     */
    private fun calculateRotation(event: MotionEvent?): Float {
        return if (event == null || event.pointerCount < 2) {
            0f
        } else calculateRotation(event.getX(0), event.getY(0), event.getX(1), event.getY(1))
    }

    private fun calculateRotation(x1: Float, y1: Float, x2: Float, y2: Float): Float {
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

    private fun calculateDistance(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        val x = (x1 - x2).toDouble()
        val y = (y1 - y2).toDouble()
        return sqrt(x * x + y * y).toFloat()
    }


    private fun View.getMappedCenterPoint(
        dst: PointF, mappedPoints: FloatArray,
        src: FloatArray
    ) {
        getCenterPoint(dst)
        src[0] = dst.x
        src[1] = dst.y
        getMappedPoints(mappedPoints, src)
        dst[mappedPoints[0]] = mappedPoints[1]
    }

    fun View.zoomAndRotateSticker(event: MotionEvent) {
            val newDistance = calculateDistance(midPoint.x, midPoint.y, event.x, event.y)
            val newRotation = calculateRotation(midPoint.x, midPoint.y, event.x, event.y)
            moveMatrix.set(downMatrix)
            moveMatrix.postScale(
                newDistance / oldDistance, newDistance / oldDistance, midPoint.x,
                midPoint.y
            )
            moveMatrix.postRotate(newRotation - oldRotation, midPoint.x, midPoint.y)
            matrix.set(moveMatrix)
    }

    private fun getMappedPoints(dst: FloatArray, src: FloatArray) {
        rootView.matrix.mapPoints(dst, src)
    }

    private fun getCenterPoint(dst: PointF) {
        dst.set(rootView.width * 1f / 2, rootView.height * 1f / 2)
    }

}

