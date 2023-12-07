package com.pettracker.demosignature.utils

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Environment
import com.pettracker.demosignature.R
import java.io.File


fun getSavedPdfPath(): File {
    val path =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DOCUMENTS
        ).absolutePath
        else Environment.getExternalStorageDirectory().absolutePath

    val directory = File(path, "/ScanPdf")

    if (!directory.exists()) {
        directory.mkdirs()
    }

    return directory
}

fun getDownloadDirectory(): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).absolutePath
    } else {
        Environment.getExternalStorageDirectory().absolutePath
    }

}

fun Context.getOutputDirectory(): File {
    val mediaDir = externalMediaDirs.firstOrNull()?.let {
        File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
    }
    return if (mediaDir != null && mediaDir.exists())
        mediaDir else filesDir
}


fun getPdfPath(): File {
    // Implement the logic to determine the path for the PDF file
    // For example:

    val path = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).absolutePath
    } else {
        Environment.getExternalStorageDirectory().absolutePath
    }

    val directory = File(path, "/ScanDoc")

    if (!directory.exists()) {
        directory.mkdirs()
    }
    return File(directory, "/OutPut.pdf")
}

fun imagePathToBitMap(imagePath: String) = BitmapFactory.decodeFile(imagePath);

