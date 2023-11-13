package com.pettracker.demosignature.utils

import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import java.io.File


fun getPdfPath(): File {
    // Implement the logic to determine the path for the PDF file
    // For example:

    val path = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString()
    } else {
        Environment.getExternalStorageDirectory().toString()
    }

    val directory = File(path, "/ScanDoc")

    if (!directory.exists()) {
        directory.mkdirs()
    }
    return File(directory, "/OutPut.pdf")
}

var imageBitmaps : List<Bitmap> = ArrayList()


