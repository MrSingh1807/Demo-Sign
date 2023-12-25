package com.pettracker.demosignature.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CaptureRequest
import android.hardware.camera2.CaptureResult
import android.hardware.camera2.TotalCaptureResult
import android.media.Image
import android.media.ImageReader
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.util.SparseIntArray
import android.view.Surface
import android.view.SurfaceView
import android.view.TextureView
import android.view.View

class Camera2(val fContext: Context) {

    val TAG = "Camera2"

    val surfaceTextureListener = object : TextureView.SurfaceTextureListener {

        override fun onSurfaceTextureAvailable(texture: SurfaceTexture, width: Int, height: Int) {

        }

        override fun onSurfaceTextureSizeChanged(texture: SurfaceTexture, width: Int, height: Int) {

        }

        override fun onSurfaceTextureDestroyed(texture: SurfaceTexture): Boolean {

            return false
        }

        override fun onSurfaceTextureUpdated(texture: SurfaceTexture) {

        }
    }

    private var cameraDevice: CameraDevice? = null
    private var cameraCaptureSession: CameraCaptureSession? = null

    private val cameraStateCallback = object : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice) {
            cameraDevice = camera
        }

        override fun onDisconnected(cameraDevice: CameraDevice) {

        }

        override fun onError(cameraDevice: CameraDevice, error: Int) {
            val errorMsg = when (error) {
                ERROR_CAMERA_DEVICE -> "Fatal (device)"
                ERROR_CAMERA_DISABLED -> "Device policy"
                ERROR_CAMERA_IN_USE -> "Camera in use"
                ERROR_CAMERA_SERVICE -> "Fatal (service)"
                ERROR_MAX_CAMERAS_IN_USE -> "Maximum cameras in use"
                else -> "Unknown"
            }
            Log.e(TAG, "Error when trying to connect camera $errorMsg")
        }
    }

    val cameraManager: CameraManager =
        fContext.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    val cameraIds: Array<String> = cameraManager.cameraIdList
    var cameraId: String = ""

    var imageReader: ImageReader? = null

    val onImageAvailableListener =
        ImageReader.OnImageAvailableListener { reader ->
            imageReader = reader
            reader.acquireLatestImage()
        }

    private val captureStateCallback = object : CameraCaptureSession.StateCallback() {
        override fun onConfigureFailed(session: CameraCaptureSession) {


        }

        override fun onConfigured(session: CameraCaptureSession) {
            cameraCaptureSession = session
        }
    }

    private lateinit var backgroundHandlerThread: HandlerThread
    private lateinit var backgroundHandler: Handler

    fun startBackgroundThread() {
        backgroundHandlerThread = HandlerThread("CameraVideoThread")
        backgroundHandlerThread.start()
        backgroundHandler = Handler(
            backgroundHandlerThread.looper
        )
    }

    fun stopBackgroundThread() {
        backgroundHandlerThread.quitSafely()
        backgroundHandlerThread.join()
    }

    @SuppressLint("MissingPermission")
    fun openCamera() {
        for (id in cameraIds) {
            val cameraCharacteristics = cameraManager.getCameraCharacteristics(id)
            //If we want to choose the rear facing camera instead of the front facing one
            if (cameraCharacteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT)
                continue


            val previewSize =
                cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)!!
                    .getOutputSizes(ImageFormat.JPEG).maxByOrNull { it.height * it.width }!!
            val imageReader =
                ImageReader.newInstance(previewSize.width, previewSize.height, ImageFormat.JPEG, 1)
            imageReader.setOnImageAvailableListener(onImageAvailableListener, backgroundHandler)
            cameraId = id
        }

        cameraManager.openCamera(cameraId, cameraStateCallback, backgroundHandler)
    }

    fun previewCamera(textureView: TextureView) {
        val surfaceTexture: SurfaceTexture? = textureView.surfaceTexture // 1
        surfaceTexture?.setDefaultBufferSize(textureView.width, textureView.height)

        val cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId) //2
//        val previewSize =
//            cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)!!
//                .getOutputSizes(ImageFormat.JPEG).maxByOrNull { it.height * it.width }!!
//
//        surfaceTexture?.setDefaultBufferSize(previewSize.width, previewSize.height) //3

        val previewSurface = Surface(surfaceTexture)

        val captureRequestBuilder =
            cameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW) //4
        captureRequestBuilder.addTarget(previewSurface) //5

        cameraDevice?.createCaptureSession(
            listOf(previewSurface, imageReader!!.surface),
            captureStateCallback,
            null
        ) //6
    }

    fun captureImage() {
        val orientations: SparseIntArray = SparseIntArray(4).apply {
            append(Surface.ROTATION_0, 0)
            append(Surface.ROTATION_90, 90)
            append(Surface.ROTATION_180, 180)
            append(Surface.ROTATION_270, 270)
        }

        val captureRequestBuilder =
            cameraDevice?.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
        captureRequestBuilder?.addTarget(imageReader!!.surface)

        val rotation = (fContext as Activity).windowManager.defaultDisplay.rotation
        captureRequestBuilder?.set(CaptureRequest.JPEG_ORIENTATION, orientations.get(rotation))
        cameraCaptureSession?.capture(captureRequestBuilder!!.build(), captureCallback, null)

    }

    val captureCallback = object : CameraCaptureSession.CaptureCallback() {

        override fun onCaptureProgressed(
            session: CameraCaptureSession,
            request: CaptureRequest,
            partialResult: CaptureResult
        ) {



            super.onCaptureProgressed(session, request, partialResult)
        }

        override fun onCaptureCompleted(
            session: CameraCaptureSession,
            request: CaptureRequest,
            result: TotalCaptureResult
        ) {

            session.close()
            cameraCaptureSession = null
            super.onCaptureCompleted(session, request, result)
        }
    }


}
