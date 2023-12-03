package com.pettracker.demosignature.activities

import android.view.View
import androidx.core.content.ContextCompat
import com.pettracker.demosignature.R
import com.pettracker.demosignature.baseClass.BaseBindingActivity
import com.pettracker.demosignature.databinding.ActivityTestingBinding
import com.xiaopo.flying.sticker.DrawableSticker

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

    fun configIconMatrix(icon: View, x: Float, y: Float, rotation: Float) { }


}