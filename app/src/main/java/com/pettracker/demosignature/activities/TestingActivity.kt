package com.pettracker.demosignature.activities

import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.os.Environment.*
import android.util.Log
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.core.content.ContextCompat
import com.pettracker.demosignature.R
import com.pettracker.demosignature.baseClass.BaseBindingActivity
import com.pettracker.demosignature.databinding.ActivityTestingBinding
import com.pettracker.demosignature.utils.FileUtils
import com.pettracker.demosignature.utils.imageBitmaps
import com.pettracker.demosignature.utils.logDebug
import com.pettracker.demosignature.utils.logError
import com.pettracker.demosignature.utils.logInfo
import com.pettracker.demosignature.utils.pdfToBitmap
import com.xiaopo.flying.sticker.DrawableSticker
import java.io.File

class TestingActivity :
    BaseBindingActivity<ActivityTestingBinding>(ActivityTestingBinding::inflate) {
    override fun initViews() {
        with(binding) {

            val drw = ContextCompat.getDrawable(mContext, R.drawable.ic_emoji_smile)
            val drawableSticker = DrawableSticker(drw)
            stickerView.addSticker(drawableSticker)


            val matrix = button.matrix.postRotate(45f, 0f, 0f)

            button.setOnClickListener {
                renameFile()

//                val intent = Intent()
//                intent.action = Intent.ACTION_OPEN_DOCUMENT
//                intent.type = "application/pdf"
//                pdfIntentLauncher.launch(intent)
            }
        }
    }


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
//
//                imageBitmaps = images
//                val pdfIntent = Intent(mContext, HomeActivity::class.java)
//                pdfIntent.putExtra("isFrom", "pfdIntent")
//                startActivity(pdfIntent)

                /* binding.apply {
                     clPdfView.visibility = View.VISIBLE
                     wvPdfView.settings.javaScriptEnabled = true
                     wvPdfView.loadUrl("http://docs.google.com/gview?embedded=true&url=$filePath")
                     cvPickMedia.visibility = View.GONE
                 }*/
            }
        }

    private fun renameFile() {

        val oldFile = File("/storage/emulated/0/Download/Ch14PS.pdf")
        val newFile = File("/storage/emulated/0/Download/Mr_Singh.pdf")

//        val file =
//            getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS + File.separator + "Ch8_PS.pdf")
//        logDebug("OldPath: ${file.path}")
//        val newFile =
//            getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS + File.separator + "Mr_Singh.pdf")

        try {
            val isTrue = oldFile.renameTo(newFile)
            logDebug("File rename: $isTrue")

        } catch (e: Exception) {
            e.printStackTrace()
            logError("Error: ${e.message}")
        }


    }
}