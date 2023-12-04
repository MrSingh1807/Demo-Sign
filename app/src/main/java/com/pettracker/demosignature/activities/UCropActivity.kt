package com.pettracker.demosignature.activities

import com.pettracker.demosignature.baseClass.BaseBindingActivity
import com.pettracker.demosignature.databinding.ActivityUcropBinding
import com.pettracker.demosignature.utils.cameraImageBitmap
import com.pettracker.demosignature.utils.cameraImageUri
import com.pettracker.demosignature.utils.logDebug

class UCropActivity : BaseBindingActivity<ActivityUcropBinding>(ActivityUcropBinding::inflate) {


    override fun initViews() {
        cameraImageBitmap?.let {
            binding.cropView.cropImageView.setImageBitmap(it)
        }
        logDebug(cameraImageUri.toString())

    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
    }
}