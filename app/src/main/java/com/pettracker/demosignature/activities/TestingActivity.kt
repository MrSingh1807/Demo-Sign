package com.pettracker.demosignature.activities

import androidx.core.content.ContextCompat
import com.pettracker.demosignature.R
import com.pettracker.demosignature.baseClass.BaseBindingActivity
import com.pettracker.demosignature.databinding.ActivityTestingBinding
import com.xiaopo.flying.sticker.DrawableSticker
import com.xiaopo.flying.sticker.Sticker

class TestingActivity : BaseBindingActivity<ActivityTestingBinding>(ActivityTestingBinding::inflate) {
    override fun initViews() {
        with(binding){

            val drw = ContextCompat.getDrawable(mContext, R.drawable.ic_emoji_smile)
            val drawableSticker = DrawableSticker(drw)
            stickerView.addSticker(drawableSticker)
        }

    }


}