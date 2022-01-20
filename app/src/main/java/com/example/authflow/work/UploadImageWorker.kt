package com.example.authflow.work

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.authflow.KEY_IMAGE_URI
import com.example.authflow.makeStatusNotification
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream

class UploadImageWorker (appContext: Context, workerParams: WorkerParameters):
    Worker(appContext, workerParams){

    val appContext = applicationContext

    val resourceUri = inputData.getString(KEY_IMAGE_URI)


    private val storage: FirebaseStorage = FirebaseStorage.getInstance()



    override fun doWork(): Result {
          uploadImages()

        return Result.success()
    }

    private fun uploadImages() {

        val resolver = appContext.contentResolver

        val picture = BitmapFactory.decodeStream(
            resolver.openInputStream(Uri.parse(resourceUri)))

        val baos = ByteArrayOutputStream()
        picture.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var imageRef = storage.reference.child("testing" +  ".jpg")


        try {

        imageRef.putBytes(data).addOnProgressListener { uploadTask ->
                var progress = (100.0 * uploadTask.bytesTransferred) / uploadTask.totalByteCount
                makeStatusNotification("$progress % complete", appContext)
            }



        } catch (e: Exception) {
          Log.d("WORKER: ", e.toString())
        }

    }
}