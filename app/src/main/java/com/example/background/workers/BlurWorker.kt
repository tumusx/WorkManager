package com.example.background.workers

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.background.KEY_IMAGE_URI
import com.example.background.R
import java.io.File
import java.lang.Exception

class BlurWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    val resourceUri = inputData.getString(KEY_IMAGE_URI)
    val appContext = applicationContext

    override fun doWork(): Result {
        if (resourceUri == null) return Result.failure();
        makeStatusNotification("Blurring image", applicationContext)
        sleep()
        return try {
            val resolver = appContext.contentResolver
            val imagem =
                BitmapFactory.decodeStream(resolver.openInputStream(Uri.parse(resourceUri)))
            val desfocarImagem = blurBitmap(imagem, appContext)
            writeBitmapToFile(bitmap = desfocarImagem, applicationContext = appContext)
            val output = blurBitmap(imagem, appContext)
            val outputUri = writeBitmapToFile(appContext, output)
            val outputData = workDataOf(KEY_IMAGE_URI to outputUri.toString())
            Result.success(outputData)
        } catch (exception: Exception) {
            exception.message.toString()
            Result.failure()
        }
    }
}