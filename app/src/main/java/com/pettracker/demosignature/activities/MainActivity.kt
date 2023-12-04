package com.pettracker.demosignature.activities

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.akshay.harsoda.permission.helper.AksPermission
import com.pettracker.demosignature.BuildConfig
import com.pettracker.demosignature.baseClass.BaseBindingActivity
import com.pettracker.demosignature.databinding.ActivityMainBinding
import com.pettracker.demosignature.utils.FileUtils
import com.pettracker.demosignature.utils.cameraImageBitmap
import com.pettracker.demosignature.utils.cameraImageUri
import com.pettracker.demosignature.utils.imageBitmaps
import com.pettracker.demosignature.utils.logDebug
import com.pettracker.demosignature.utils.logInfo
import com.pettracker.demosignature.utils.pdfToBitmap
import java.io.File


class MainActivity : BaseBindingActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private val pdfIntentLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.resultCode != RESULT_CANCELED) {
                val filePath = result.data?.data
                Log.d("Result", filePath.toString())

                val path = FileUtils.getPath(this, filePath!!)
                logDebug("AbsolutePath: $path")

                val images = pdfToBitmap(File(path))
                images.forEach {
                    logInfo(it.toString())
                }

                imageBitmaps = images
                val pdfIntent = Intent(mContext, HomeActivity::class.java)
                pdfIntent.putExtra("isFrom", "pfdIntent")
                startActivity(pdfIntent)

                /* binding.apply {
                     clPdfView.visibility = View.VISIBLE
                     wvPdfView.settings.javaScriptEnabled = true
                     wvPdfView.loadUrl("http://docs.google.com/gview?embedded=true&url=$filePath")
                     cvPickMedia.visibility = View.GONE
                 }*/
            }
        }

    private val galleryIntentLauncher =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                val img = listOf(bitmap)

                imageBitmaps = img
                val pdfIntent = Intent(mContext, HomeActivity::class.java)
                pdfIntent.putExtra("isFrom", "galleryIntent")
                startActivity(pdfIntent)

                Log.d("PhotoPicker", "Selected URI: $uri")
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

    private val cameraIntentLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val bitmap = it.data?.extras?.get("data") as Bitmap
            val uri = it.data!!.data

            cameraImageBitmap = bitmap
            cameraImageUri = uri
            logDebug(uri.toString())

            val intent = Intent(this, UCropActivity::class.java)
            startActivity(intent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun initViews() {
        grantManageStoragePermission()

        with(binding) {

            cvPickMedia.setOnClickListener {
                askPermissions {
                    val dialog = AlertDialog.Builder(mContext)
                    dialog.setTitle("Pick Media")
                        .setMessage("Pick Media From External Storage!")
                        .setCancelable(true)
                        .setPositiveButton("Gallery") { _, _ ->
                            galleryIntentLauncher.launch(
                                PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageOnly
                                )
                            )
                        }
                        .setNegativeButton("Pdf") { _, _ ->
                            val intent = Intent()
                            intent.action = Intent.ACTION_OPEN_DOCUMENT
                            intent.type = "application/pdf"
                            pdfIntentLauncher.launch(intent)
                        }
                        .setNeutralButton("Camera") { _, _ ->
                            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            cameraIntentLauncher.launch(cameraIntent)
                        }
                        .create()

                    dialog.show()
                }
            }
            btnPrevious.setOnClickListener {
                cvPickMedia.visibility = View.VISIBLE
                clPdfView.visibility = View.GONE
            }
            btnNext.setOnClickListener {
                startActivity(Intent(mContext, HomeActivity::class.java))
            }

            btnTesting.setOnClickListener {
                startActivity(Intent(mContext, TestingActivity::class.java))
            }
        }
    }

    private fun askPermissions(doWhatEver: () -> Unit = {}) {

        val permissions = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            arrayListOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
            )
        } else {
            arrayListOf(Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES)
        }

        AksPermission.with(this)
            .permissions(permissions)
            .isShowDefaultSettingDialog(true)
            .request { fGrantedList ->
                logInfo("Permission granted")
                doWhatEver.invoke()
            }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    val storagePermissionLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                if (Environment.isExternalStorageManager()) {
                    // Permission granted. Now resume your workflow.
                }
            }
        }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun grantManageStoragePermission() {
        if (!Environment.isExternalStorageManager()) {
            val uri = Uri.parse("package:${BuildConfig.APPLICATION_ID}")
            val storageIntent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri)
            storagePermissionLauncher.launch(storageIntent)
        }
    }

}
