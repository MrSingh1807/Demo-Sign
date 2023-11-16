package com.pettracker.demosignature.activities

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.pettracker.demosignature.R
import com.pettracker.demosignature.adapters.HomeScreenRVAdapter
import com.pettracker.demosignature.baseClass.BaseBindingActivity
import com.pettracker.demosignature.databinding.ActivityHomeBinding
import com.pettracker.demosignature.utils.CustomSingleTouchListener
import com.pettracker.demosignature.utils.beGone
import com.pettracker.demosignature.utils.beVisible
import com.pettracker.demosignature.utils.drawableToBitmap
import com.pettracker.demosignature.utils.imageBitmaps
import com.pettracker.demosignature.utils.imagePathToBitMap
import com.pettracker.demosignature.utils.logDebug
import com.pettracker.demosignature.utils.logError
import com.pettracker.demosignature.utils.logInfo
import com.pettracker.demosignature.utils.toast
import ja.burhanrashid52.photoeditor.OnPhotoEditorListener
import ja.burhanrashid52.photoeditor.PhotoEditor
import ja.burhanrashid52.photoeditor.PhotoEditorView
import ja.burhanrashid52.photoeditor.ViewType
import java.lang.NullPointerException
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class HomeActivity : BaseBindingActivity<ActivityHomeBinding>(ActivityHomeBinding::inflate) {

    private lateinit var mRVAdapter: HomeScreenRVAdapter
    private var mPhotoEditor: PhotoEditor? = null

    private var addedZoom: ImageView? = null
    private var addedRootView: FrameLayout? = null
    private var addedFrameBorder: FrameLayout? = null

    private val galleryIntentLauncher =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { imageUri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (imageUri != null) {

                val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    ImageDecoder.decodeBitmap(
                        ImageDecoder.createSource(
                            mContext.contentResolver,
                            imageUri
                        )
                    )
                } else {
                    MediaStore.Images.Media.getBitmap(mContext.contentResolver, imageUri)
                }
                mPhotoEditor?.addImage(bitmap)

                Log.d("PhotoPicker", "Selected URI: $imageUri")
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

    private val photoEditorListener = object : OnPhotoEditorListener {
        override fun onEditTextChangeListener(rootView: View, text: String, colorCode: Int) {

        }

        override fun onAddViewListener(viewType: ViewType, numberOfAddedViews: Int) {
//            mContext.toast("onAddViewListener")
        }

        override fun onRemoveViewListener(viewType: ViewType, numberOfAddedViews: Int) {
//            mContext.toast("onRemoveViewListener")
        }

        override fun onStartViewChangeListener(viewType: ViewType) {
//            mContext.toast("onStartViewChangeListener")
        }

        override fun onStopViewChangeListener(viewType: ViewType) {
//            mContext.toast("onStopViewChangeListener")
        }

        override fun onTouchSourceImage(event: MotionEvent) {
//            mContext.toast("onTouchSourceImage")
        }

        override fun onViewInstance(rootView: View) {
            mContext.toast("onViewInstance")
            addedZoom =
                rootView.findViewById(ja.burhanrashid52.photoeditor.R.id.imgPhotoEditorZoom)
            addedRootView = rootView as FrameLayout
            addedFrameBorder = rootView.findViewById(ja.burhanrashid52.photoeditor.R.id.frmBorder)
        }

    }

    private var onTouch: CustomSingleTouchListener? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun initViews() {

        val isFrom = intent.getStringExtra("isFrom")

        if (!isFrom.isNullOrBlank()) {
            if (isFrom == "galleryIntent") {
                binding.rvPdfImages.beGone()
                binding.galleryPhotoEditorView.beVisible()

                mPhotoEditor = PhotoEditor.Builder(mContext, binding.galleryPhotoEditorView)
                    .setPinchTextScalable(true)
                    .setClipSourceImage(true)
                    .build()

                binding.galleryPhotoEditorView.source.setImageBitmap(imageBitmaps.first())

            } else if (isFrom == "pfdIntent") {
                binding.rvPdfImages.beVisible()
                binding.galleryPhotoEditorView.beGone()

                initRecyclerView()
            }
        }
//        initRecyclerView()
        mPhotoEditor?.setOnPhotoEditorListener(photoEditorListener)

        addedFrameBorder?.let {
            onTouch = CustomSingleTouchListener(it)
            addedZoom?.setOnTouchListener(onTouch)
        }

        addedFrameBorder?.setOnTouchListener { view, event ->
            onTouch?.zoomAndRotateSticker(addedFrameBorder, event)
            return@setOnTouchListener true
        }

        addedZoom?.setOnClickListener {
            Toast.makeText(mContext, "Zoom Clicked", Toast.LENGTH_SHORT).show()
        }

    }

    @SuppressLint("MissingPermission")
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
            btnAddText.setOnClickListener { mPhotoEditor?.addText("Mr Singh", Color.BLACK) }
            btnSave.setOnClickListener { saveAsBitmap() }
        }
    }

    private val onImageEditor = object : HomeScreenRVAdapter.OnImageEditor {
        override fun onImageClicked(position: Int, photoEditor: PhotoEditor) {
            mPhotoEditor = photoEditor
        }
    }

    private fun initRecyclerView() {
        val testingList = listOf(imageBitmaps.first())
        binding.rvPdfImages.layoutManager = LinearLayoutManager(this)
        mRVAdapter = HomeScreenRVAdapter(this, onImageEditor)
        mRVAdapter.setData(testingList)

        binding.rvPdfImages.adapter = mRVAdapter
    }

    fun saveAsBitmap(): List<Bitmap> {

        val savedImagesBitmap: ArrayList<Bitmap> = arrayListOf()
        val filePath = cacheDir.absolutePath

        for (i in imageBitmaps.indices) {
            try {
                val holder = binding.rvPdfImages.findViewHolderForAdapterPosition(i)!!
                val picEdtView = holder.itemView.findViewById<PhotoEditorView>(R.id.photoEditorView)

                val picEditor = PhotoEditor.Builder(this, picEdtView)
                    .setPinchTextScalable(true)
                    .setClipSourceImage(true)
                    .build()


                picEditor.saveAsFile(
                    "$filePath/pdfImage$i.jpg",
                    object : PhotoEditor.OnSaveListener {
                        override fun onSuccess(imagePath: String) {
                            savedImagesBitmap.add(imagePathToBitMap(imagePath))
                            logInfo(imagePath)
                            Toast.makeText(mContext, "Saved", Toast.LENGTH_SHORT).show()
                        }

                        override fun onFailure(exception: Exception) {
                            Toast.makeText(mContext, "Saved", Toast.LENGTH_SHORT).show()
                        }
                    })

            } catch (e: NullPointerException) {
                logError(e.message.toString())
            } catch (e: Exception) {
                logError(e.message.toString())
            }
        }

        savedImagesBitmap.forEach {
            logDebug(it.toString())
        }
        return savedImagesBitmap
    }

    companion object {
        val instance = this

    }
}