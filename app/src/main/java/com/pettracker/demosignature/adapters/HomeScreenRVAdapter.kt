package com.pettracker.demosignature.adapters

import android.content.Context
import android.graphics.Bitmap
import com.pettracker.demosignature.baseClass.BaseBindingAdapter
import com.pettracker.demosignature.databinding.LayoutHomeScreenRvBinding
import ja.burhanrashid52.photoeditor.PhotoEditor


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
            mPhotoEditor = PhotoEditor.Builder(context, photoEditorView)
                .setPinchTextScalable(true)
                .setClipSourceImage(true)
//                .setDefaultTextTypeface(mTextRobotoTf)
//                .setDefaultEmojiTypeface(mEmojiTypeFace)
                .build()

            photoEditorView.source.setImageBitmap(mBitmap)
            onImageEditor.onImageClicked(position, mPhotoEditor)

        }
    }

}
