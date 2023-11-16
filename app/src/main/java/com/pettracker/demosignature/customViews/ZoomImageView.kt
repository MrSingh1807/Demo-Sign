package com.pettracker.demosignature.customViews

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Matrix
import android.graphics.PointF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatImageView

class ZoomImageView(context: Context, attr: AttributeSet) : AppCompatImageView(context, attr) {


    private var midPoint = PointF()
    private val moveMatrix = Matrix()
    private val downMatrix = Matrix()

    private var oldDistance = 0f
    private var oldRotation = 0f

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {

        val action = event?.action

        when (action) {
            MotionEvent.ACTION_POINTER_DOWN -> {
                oldDistance = calculateDistance(event)
                oldRotation = calculateRotation(event)

                midPoint = calculateMidPoint(event)
                zoomAndRotateSticker(this, event)
            }
        }


        return super.onTouchEvent(event)
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
        return Math.sqrt(x * x + y * y).toFloat()
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