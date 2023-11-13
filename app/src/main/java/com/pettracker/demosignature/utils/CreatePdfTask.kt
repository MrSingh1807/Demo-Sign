package com.pettracker.demosignature.utils

import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import android.widget.Toast
import com.itextpdf.text.BadElementException
import com.itextpdf.text.Document
import com.itextpdf.text.DocumentException
import com.itextpdf.text.Image
import com.itextpdf.text.PageSize
import com.itextpdf.text.pdf.PdfWriter
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class CreatePdfTask(private val context: Context, private val files: ArrayList<File>) :
    AsyncTask<String, Int, File>() {
    private var progressDialog: ProgressDialog? = null

    override fun onPreExecute() {
        super.onPreExecute()
        progressDialog = ProgressDialog(context)
        progressDialog?.apply {
            setTitle("Please wait...")
            setMessage("Creating pdf...")
            setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
            setIndeterminate(false)
            setMax(100)
            setCancelable(true)
            show()
        }
    }

    override fun doInBackground(vararg strings: String): File? {
        val outputMediaFile = getPdfPath() // path in which you want to save pdf

        val document = Document(PageSize.A4, 38.0f, 38.0f, 50.0f, 38.0f)
        try {
            PdfWriter.getInstance(document, FileOutputStream(outputMediaFile))
        } catch (e: DocumentException) {
            e.printStackTrace()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            return null
        }
        document.open()

        var i = 0
        while (i < files.size) {
            try {
                val image = Image.getInstance(files[i].absolutePath)

                val scaler =
                    ((document.pageSize.width - document.leftMargin() - document.rightMargin() - 0) / image.width) * 100 // 0 means you have no indentation. If you have any, change it.
                image.scalePercent(scaler)
                image.setAlignment(Image.ALIGN_CENTER or Image.ALIGN_TOP)
                image.setAbsolutePosition(
                    (document.pageSize.width - image.scaledWidth) / 2.0f,
                    (document.pageSize.height - image.scaledHeight) / 2.0f
                )

                document.add(image)
                document.newPage()
                publishProgress(i)
                i++
            } catch (e: BadElementException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: DocumentException) {
                e.printStackTrace()
            }
        }
        document.close()
        return outputMediaFile
    }

    override fun onProgressUpdate(vararg values: Int?) {
        super.onProgressUpdate(*values)
        progressDialog?.apply {
            progress = ((values[0]!! + 1) * 100) / files.size
            setTitle("Processing images (${values[0]!! + 1}/${files.size})")
        }
    }

    override fun onPostExecute(file: File?) {
        super.onPostExecute(file)
        progressDialog?.dismiss()
        Toast.makeText(context, "Pdf store at ${file?.absolutePath}", Toast.LENGTH_SHORT).show()
    }
}
