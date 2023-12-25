package com.pettracker.demosignature.stickerViewDemo

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.pettracker.demosignature.R
import com.pettracker.demosignature.stickerViewDemo.util.FileUtil
import com.xiaopo.flying.sticker.BitmapStickerIcon
import com.xiaopo.flying.sticker.DeleteIconEvent
import com.xiaopo.flying.sticker.DrawableSticker
import com.xiaopo.flying.sticker.FlipHorizontallyEvent
import com.xiaopo.flying.sticker.Sticker
import com.xiaopo.flying.sticker.StickerView
import com.xiaopo.flying.sticker.StickerView.OnStickerOperationListener
import com.xiaopo.flying.sticker.TextSticker
import com.xiaopo.flying.sticker.ZoomIconEvent
import java.util.Arrays


class StickerViewTestActivity : AppCompatActivity() {
    private var stickerView: StickerView? = null
    private var sticker: TextSticker? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sticker_view_test)
        stickerView = findViewById<StickerView>(R.id.sticker_view)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        //currently you can config your own icons and icon event
        //the event you can custom
        val deleteIcon = BitmapStickerIcon(
            ContextCompat.getDrawable(
                this,
                com.xiaopo.flying.sticker.R.drawable.sticker_ic_close_white_18dp
            ),
            BitmapStickerIcon.LEFT_TOP
        )
        deleteIcon.iconEvent = DeleteIconEvent()
        val zoomIcon = BitmapStickerIcon(
            ContextCompat.getDrawable(
                this,
                com.xiaopo.flying.sticker.R.drawable.sticker_ic_scale_white_18dp
            ),
            BitmapStickerIcon.RIGHT_BOTOM
        )
        zoomIcon.iconEvent = ZoomIconEvent()
        val flipIcon = BitmapStickerIcon(
            ContextCompat.getDrawable(
                this,
                com.xiaopo.flying.sticker.R.drawable.sticker_ic_flip_white_18dp
            ),
            BitmapStickerIcon.RIGHT_TOP
        )
        flipIcon.iconEvent = FlipHorizontallyEvent()
        val heartIcon = BitmapStickerIcon(
            ContextCompat.getDrawable(this, R.drawable.ic_favorite_white_24dp),
            BitmapStickerIcon.LEFT_BOTTOM
        )
        heartIcon.iconEvent = HelloIconEvent()
        stickerView!!.setIcons(Arrays.asList(deleteIcon, zoomIcon, flipIcon, heartIcon))

        //default icon layout
        //stickerView.configDefaultIcons();
        stickerView!!.setBackgroundColor(Color.WHITE)
        stickerView!!.setLocked(false)
        stickerView!!.setConstrained(true)
        sticker = TextSticker(this)
        sticker!!.setDrawable(
            ContextCompat.getDrawable(
                applicationContext,
                com.xiaopo.flying.sticker.R.drawable.sticker_transparent_background
            )!!
        )
        sticker!!.setText("Hello, world!")
        sticker!!.setTextColor(Color.BLACK)
        sticker!!.setTextAlign(Layout.Alignment.ALIGN_CENTER)
        sticker!!.resizeText()
        stickerView!!.setOnStickerOperationListener(object : OnStickerOperationListener {
            override fun onStickerAdded(sticker: Sticker) {
                Log.d(TAG, "onStickerAdded")
            }

            override fun onStickerClicked(sticker: Sticker) {
                //stickerView.removeAllSticker();
                if (sticker is TextSticker) {
                    sticker.setTextColor(Color.RED)
                    stickerView!!.replace(sticker)
                    stickerView!!.invalidate()
                }
                Log.d(TAG, "onStickerClicked")
            }

            override fun onStickerDeleted(sticker: Sticker) {
                Log.d(TAG, "onStickerDeleted")
            }

            override fun onStickerDragFinished(sticker: Sticker) {
                Log.d(TAG, "onStickerDragFinished")
            }

            override fun onStickerTouchedDown(sticker: Sticker) {
                Log.d(TAG, "onStickerTouchedDown")
            }

            override fun onStickerZoomFinished(sticker: Sticker) {
                Log.d(TAG, "onStickerZoomFinished")
            }

            override fun onStickerFlipped(sticker: Sticker) {
                Log.d(TAG, "onStickerFlipped")
            }

            override fun onStickerDoubleTapped(sticker: Sticker) {
                Log.d(TAG, "onDoubleTapped: double tap will be with two click")
            }
        })
        if (toolbar != null) {
            toolbar.setTitle(R.string.app_name)
            toolbar.inflateMenu(R.menu.menu_save)
            toolbar.setOnMenuItemClickListener { item ->
                if (item.itemId == R.id.item_save) {
                    val file = FileUtil.getNewFile(this@StickerViewTestActivity, "Sticker")
                    if (file != null) {
                        stickerView!!.save(file)
                        Toast.makeText(
                            this@StickerViewTestActivity, "saved in " + file.absolutePath,
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this@StickerViewTestActivity,
                            "the file is null",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                false
            }
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                PERM_RQST_CODE
            )
        } else {
            loadSticker()
        }
    }

    private fun loadSticker() {
        val drawable = ContextCompat.getDrawable(this, R.drawable.haizewang_215)
        val drawable1 = ContextCompat.getDrawable(this, R.drawable.haizewang_23)
        stickerView!!.addSticker(DrawableSticker(drawable))
        stickerView!!.addSticker(
            DrawableSticker(drawable1),
            Sticker.Position.BOTTOM or Sticker.Position.RIGHT
        )
        val bubble = ContextCompat.getDrawable(this, R.drawable.bubble)
        stickerView!!.addSticker(
            TextSticker(applicationContext)
                .setDrawable(bubble!!)
                .setText("Sticker\n")
                .setMaxTextSize(14f)
                .resizeText(), Sticker.Position.TOP
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERM_RQST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadSticker()
        }
    }

    fun testReplace(view: View?) {
        if (stickerView!!.replace(sticker)) {
            Toast.makeText(this@StickerViewTestActivity, "Replace Sticker successfully!", Toast.LENGTH_SHORT)
                .show()
        } else {
            Toast.makeText(this@StickerViewTestActivity, "Replace Sticker failed!", Toast.LENGTH_SHORT).show()
        }
    }

    fun testLock(view: View?) {
        stickerView!!.setLocked(!stickerView!!.isLocked)
    }

    fun testRemove(view: View?) {
        if (stickerView!!.removeCurrentSticker()) {
            Toast.makeText(
                this@StickerViewTestActivity,
                "Remove current Sticker successfully!",
                Toast.LENGTH_SHORT
            )
                .show()
        } else {
            Toast.makeText(this@StickerViewTestActivity, "Remove current Sticker failed!", Toast.LENGTH_SHORT)
                .show()
        }
    }

    fun testRemoveAll(view: View?) {
        stickerView!!.removeAllStickers()
    }

    fun reset(view: View?) {
        stickerView!!.removeAllStickers()
        loadSticker()
    }

    fun testAdd(view: View?) {
        val sticker = TextSticker(this)
        sticker.setText("Hello, world!")
        sticker.setTextColor(Color.BLUE)
        sticker.setTextAlign(Layout.Alignment.ALIGN_CENTER)
        sticker.resizeText()
        stickerView!!.addSticker(sticker)
    }

    companion object {
        private val TAG = StickerViewTestActivity::class.java.simpleName
        const val PERM_RQST_CODE = 110
    }
}

