package com.pettracker.demosignature.activities

import android.graphics.Color
import android.util.Log
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.pettracker.demosignature.R
import com.pettracker.demosignature.adapters.HomeScreenRVAdapter
import com.pettracker.demosignature.baseClass.BaseBindingActivity
import com.pettracker.demosignature.databinding.ActivityHomeBinding
import com.pettracker.demosignature.utils.drawableToBitmap
import com.pettracker.demosignature.utils.imageBitmaps
import com.pettracker.demosignature.utils.uriToBitMap
import ja.burhanrashid52.photoeditor.PhotoEditor
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class HomeActivity : BaseBindingActivity<ActivityHomeBinding>(ActivityHomeBinding::inflate) {

    private lateinit var mRVAdapter: HomeScreenRVAdapter
    private var mPhotoEditor: PhotoEditor? = null

    private val galleryIntentLauncher =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {
                val bitmap = uriToBitMap(uri)
                mPhotoEditor?.addImage(bitmap)
                Log.d("PhotoPicker", "Selected URI: $uri")
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

    override fun initViews() {
        initRecyclerView()
    }

    override fun initViewListener() {
        super.initViewListener()
        with(binding) {
            btnAddDate.setOnClickListener {
                val currentDate = LocalDate.now()
                val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                val formattedDate = currentDate.format(formatter)
                mPhotoEditor?.addText(formattedDate, Color.BLACK)
            }
            btnAddIcon.setOnClickListener {
                ContextCompat.getDrawable(mContext, R.drawable.ic_emoji_smile)?.let { it ->
                    drawableToBitmap(it)?.let { it1 ->
                        mPhotoEditor?.addImage(
                            it1
                        )
                    }
                }
            }

            btnAddImage.setOnClickListener {
                galleryIntentLauncher.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            }

            btnAddText.setOnClickListener {
                mPhotoEditor?.addText("Mr Singh", Color.BLACK)
            }


        }
    }

    private val onImageEditor = object : HomeScreenRVAdapter.OnImageEditor {
        override fun onImageClicked(position: Int, photoEditor: PhotoEditor) {
            mPhotoEditor = photoEditor
        }
    }

    private fun initRecyclerView() {
        binding.rvPdfImages.layoutManager = LinearLayoutManager(this)
        mRVAdapter = HomeScreenRVAdapter(this, onImageEditor)
        mRVAdapter.setData(imageBitmaps)

        binding.rvPdfImages.adapter = mRVAdapter
    }

    companion object {
        val instance = this

    }
}