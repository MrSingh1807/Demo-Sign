package com.pettracker.demosignature.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import org.jetbrains.anko.dip


object DrawableUtil {

    fun writeTextOnDrawableInternal(context: Context, drawableId: Int, text: String,
                                    textSizeDp: Int, horizontalOffset: Int, verticalOffset: Int): BitmapDrawable {

        val bm = BitmapFactory.decodeResource(context.resources, drawableId)
            .copy(Bitmap.Config.ARGB_8888, true)

        val tf = Typeface.create("Helvetica", Typeface.BOLD)

        val paint = Paint()
        paint.style = Paint.Style.FILL
        paint.color = Color.WHITE
        paint.typeface = tf
        paint.textAlign = Paint.Align.LEFT
        paint.textSize = context.dip(textSizeDp).toFloat()

        val textRect = Rect()
        paint.getTextBounds(text, 0, text.length, textRect)

        val canvas = Canvas(bm)

        //If the text is bigger than the canvas , reduce the font size
        if (textRect.width() >= canvas.getWidth() - 4)
        //the padding on either sides is considered as 4, so as to appropriately fit in the text
            paint.textSize = context.dip(12).toFloat()

        //Calculate the positions
        val xPos = canvas.width.toFloat()/2 + horizontalOffset

        //"- ((paint.descent() + paint.ascent()) / 2)" is the distance from the baseline to the center.
        val yPos = (canvas.height / 2 - (paint.descent() + paint.ascent()) / 2) + verticalOffset

        canvas.drawText(text, xPos, yPos, paint)

        return BitmapDrawable(context.resources, bm)
    }
}

