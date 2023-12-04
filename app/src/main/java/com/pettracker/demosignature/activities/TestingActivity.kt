package com.pettracker.demosignature.activities

import android.graphics.Canvas
import android.net.Uri
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.github.barteksc.pdfviewer.listener.OnDrawListener
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import com.github.barteksc.pdfviewer.listener.OnRenderListener
import com.github.barteksc.pdfviewer.listener.OnTapListener
import com.pettracker.demosignature.R
import com.pettracker.demosignature.baseClass.BaseBindingActivity
import com.pettracker.demosignature.databinding.ActivityTestingBinding
import com.xiaopo.flying.sticker.DrawableSticker
import java.io.InputStream

class TestingActivity :
    BaseBindingActivity<ActivityTestingBinding>(ActivityTestingBinding::inflate) {
    override fun initViews() {
        with(binding) {

            val drw = ContextCompat.getDrawable(mContext, R.drawable.ic_emoji_smile)
            val drawableSticker = DrawableSticker(drw)
            stickerView.addSticker(drawableSticker)


            val matrix = button.matrix.postRotate(45f, 0f, 0f)



        }

    }

    fun configIconMatrix(icon: View, x: Float, y: Float, rotation: Float) {}

    fun testing() {

        val drawListener = object : OnDrawListener {
            override fun onLayerDrawn(
                canvas: Canvas?,
                pageWidth: Float,
                pageHeight: Float,
                displayedPage: Int
            ) {
                TODO("Not yet implemented")
            }

        }


        val uri = Uri.parse("")
        binding.pdfView.fromUri(uri)
            .onDraw(drawListener)
            .onDrawAll(drawListener)
            .onLoad { it -> }
            .onPageChange { page, pageCount -> TODO("Not yet implemented") }
            .onRender { nbPages, pageWidth, pageHeight -> TODO("Not yet implemented") }
            .onTap { TODO("Not yet implemented") }
            .enableAnnotationRendering(true)


    }

}