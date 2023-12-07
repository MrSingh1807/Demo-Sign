package com.pettracker.demosignature.activities

import com.pettracker.demosignature.baseClass.BaseBindingActivity
import com.pettracker.demosignature.databinding.ActivityCameraxBinding
import com.pettracker.demosignature.utils.CameraX
import java.util.concurrent.Executors

class CameraXActivity :
    BaseBindingActivity<ActivityCameraxBinding>(ActivityCameraxBinding::inflate) {

    private val cameraX: CameraX by lazy { CameraX(this) }
    override fun initViews() {
        with(binding) {

            cameraX.cameraExecutor = Executors.newSingleThreadExecutor()
            cameraX.startCamera(binding.viewFinder, this@CameraXActivity)

            cameraCaptureBTN.setOnClickListener {
                cameraX.takePhoto()
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraX.cameraExecutor.shutdown()
    }
}
