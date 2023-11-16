package com.pettracker.demosignature.adapters

import android.content.Context
import android.graphics.Bitmap
import android.widget.Toast
import com.pettracker.demosignature.baseClass.BaseBindingAdapter
import com.pettracker.demosignature.databinding.LayoutHomeScreenRvBinding
import com.pettracker.demosignature.utils.imageBitmaps
import com.pettracker.demosignature.utils.logDebug
import com.pettracker.demosignature.utils.logInfo
import ja.burhanrashid52.photoeditor.PhotoEditor
import ja.burhanrashid52.photoeditor.PhotoEditorView


class HomeScreenRVAdapter(
    private val context: Context,
    private val onImageEditor: OnImageEditor
) : BaseBindingAdapter<Bitmap, LayoutHomeScreenRvBinding>(LayoutHomeScreenRvBinding::inflate) {

    lateinit var mPhotoEditor: PhotoEditor

    interface OnImageEditor {
        fun onImageClicked(position: Int, mPhotoEditor: PhotoEditor)
    }

    override fun bind(holder: ViewHolder, mBitmap: Bitmap, position: Int) {
        with(holder.binding) {
            photoEditorView.source.setImageBitmap(mBitmap)
            photoEditorView.setOnClickListener {
                mPhotoEditor = PhotoEditor.Builder(context, photoEditorView)
                    .setPinchTextScalable(true)
                    .setClipSourceImage(true)
//                .setDefaultTextTypeface(mTextRobotoTf)
//                .setDefaultEmojiTypeface(mEmojiTypeFace)
                    .build()
                onImageEditor.onImageClicked(position, mPhotoEditor)
            }

        }
    }

    fun saveAsBitmap(holder: ViewHolder): List<Bitmap> {
        val savedImagesBitmap: List<Bitmap> = ArrayList()

        val filePath = context.cacheDir.absolutePath

        for (i in 0..itemCount) {

            val picEditor = PhotoEditor.Builder(context, holder.binding.photoEditorView)
                .setPinchTextScalable(true)
                .setClipSourceImage(true)
                .build()

            picEditor.saveAsFile(
                "$filePath/pdfImage$i.jpg",
                object : PhotoEditor.OnSaveListener {
                    override fun onSuccess(imagePath: String) {
                        logInfo(imagePath)
                        Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
                    }

                    override fun onFailure(exception: Exception) {
                        Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
                    }
                })
        }

        savedImagesBitmap.forEach {
            logDebug(it.toString())
        }
        return savedImagesBitmap
    }


}
