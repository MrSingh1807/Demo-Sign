package com.pettracker.demosignature.activities

import com.pettracker.demosignature.baseClass.BaseBindingActivity
import com.pettracker.demosignature.databinding.ActivityCameraxBinding
import com.pettracker.demosignature.utils.Camera2
import com.pettracker.demosignature.utils.CameraX
import java.util.concurrent.Executors

class CameraXActivity :
    BaseBindingActivity<ActivityCameraxBinding>(ActivityCameraxBinding::inflate) {

    private val cameraX: CameraX by lazy { CameraX(this) }
    private val camera2: Camera2 by lazy { Camera2(this) }

    override fun initViews() {
        with(binding) {

            camera2.startBackgroundThread()
            camera2.openCamera()
            camera2.previewCamera(binding.viewFinder)
            binding.viewFinder.surfaceTextureListener = camera2.surfaceTextureListener


//            cameraX.cameraExecutor = Executors.newSingleThreadExecutor()
//            cameraX.startCamera(binding.viewFinder, this@CameraXActivity)

//            cameraCaptureBTN.setOnClickListener {
//                cameraX.takePhoto()
//            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
//        cameraX.cameraExecutor.shutdown()

        camera2.stopBackgroundThread()
    }
}
