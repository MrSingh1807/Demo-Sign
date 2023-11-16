package com.pettracker.demosignature.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.shockwave.pdfium.PdfDocument
import com.shockwave.pdfium.PdfiumCore
import java.io.File
import java.io.FileOutputStream
import kotlin.math.floor


fun Context.pdfToBitmap(pdfFile: File): ArrayList<Bitmap> {
    val bitmaps = ArrayList<Bitmap>()
    try {
        val renderer =
            PdfRenderer(ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY))
        var bitmap: Bitmap
        val pageCount = renderer.pageCount
        for (i in 0 until pageCount) {
            val page = renderer.openPage(i)
            val width: Int = resources.displayMetrics.densityDpi / 72 * page.width
            val height: Int = resources.displayMetrics.densityDpi / 72 * page.height
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
            bitmaps.add(bitmap)
            logInfo(bitmap.toString())
            // close the page
            page.close()
        }

        // close the renderer
        renderer.close()
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
    return bitmaps
}

fun Context.renderToBitmap(filePath: String): List<Bitmap> {
    val images: MutableList<Bitmap> = ArrayList()
    val pdfiumCore = PdfiumCore(this)
    try {
        val f = File(filePath)
        val fd: ParcelFileDescriptor =
            ParcelFileDescriptor.open(f, ParcelFileDescriptor.MODE_READ_ONLY)
        val pdfDocument: PdfDocument = pdfiumCore.newDocument(fd)
        val pageCount: Int = pdfiumCore.getPageCount(pdfDocument)
        for (i in 0 until pageCount) {
            pdfiumCore.openPage(pdfDocument, i)
            val width: Int = pdfiumCore.getPageWidthPoint(pdfDocument, i)
            val height: Int = pdfiumCore.getPageHeightPoint(pdfDocument, i)
            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            pdfiumCore.renderPageBitmap(pdfDocument, bmp, i, 0, 0, width, height)
            images.add(bmp)
        }
        pdfiumCore.closeDocument(pdfDocument)
    } catch (e: Exception) {
        // todo with exception
    }
    return images
}

fun List<Bitmap>.createPdf(pdfFile: File) {
    // Create a new PdfDocument
    val pdfDocument = android.graphics.pdf.PdfDocument()

    // Iterate through the list of Bitmaps and add each to the PDF document
    for (bitmap in this) {
        val pageInfo =
            android.graphics.pdf.PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1)
                .create()
        val page = pdfDocument.startPage(pageInfo)

        // Draw the Bitmap on the PDF page using a Canvas
        val canvas = page.canvas
        canvas.drawBitmap(bitmap, 0f, 0f, null)

        pdfDocument.finishPage(page)
    }

    // Save the PDF document to the specified file
    try {
        val outputStream = FileOutputStream(pdfFile)
        pdfDocument.writeTo(outputStream)
        pdfDocument.close()
        outputStream.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Context.uriToBitMap(uri: Uri) = Glide.with(this).asBitmap().load(uri).submit().get()

fun drawableToBitmap(drawable: Drawable): Bitmap? {
    if (drawable is BitmapDrawable) {
        return drawable.bitmap
    }
    var width = drawable.intrinsicWidth
    width = if (width > 0) width else 1
    var height = drawable.intrinsicHeight
    height = if (height > 0) height else 1
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}

private fun getPowerOfTwoForSampleRatio(ratio: Double): Int {
    val k = Integer.highestOneBit(floor(ratio).toInt())
    return if (k == 0) 1 else k
}

fun View.beVisible() {
    if (isGone || isInvisible) visibility = VISIBLE
}

fun View.beInvisible() {
    if (isVisible) visibility = INVISIBLE
}

fun View.beGone() {
    if (isVisible) visibility = GONE
}


fun logDebug(value: String) {
    Log.d("MR_SINGH", "Debug: $value ")
}

fun logInfo(value: String) {
    Log.i("MR_SINGH", "Info: $value ")
}

fun logError(value: String) {
    Log.e("MR_SINGH", "Error: $value ")
}

fun Context.toast(value: String) {
    Toast.makeText(this, value, Toast.LENGTH_SHORT).show()
}

//todo: Usage example
//  val bitmapList: List<Bitmap> = // Your list of Bitmaps
//  val pdfFile = File("/path/to/your/file.pdf") // Replace with the desired file path
//  bitmapList.createPdf( pdfFile)
