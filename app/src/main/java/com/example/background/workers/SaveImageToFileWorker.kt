package com.example.background.workers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.background.KEY_IMAGE_URI
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class SaveImageToFileWorker(ctx: Context, parameters: WorkerParameters) : Worker(ctx, parameters) {

    private val tittle = "Blurred Image"
    private val dateFormater =
        SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss z", Locale.getDefault())

    override fun doWork(): Result {
        makeStatusNotification("Imagem salva", context = applicationContext)
        sleep()
        val resolver = applicationContext.contentResolver
        return try {
            val resourceUri = inputData.getString(KEY_IMAGE_URI)
            val bitmap = BitmapFactory.decodeStream(
                resolver.openInputStream(Uri.parse(resourceUri)))
            val imageUrl = MediaStore.Images.Media.insertImage(resolver,
        bitmap, tittle, dateFormater.format(Date()))

            if(!imageUrl.isNullOrEmpty()){
                workDataOf(KEY_IMAGE_URI to resourceUri)
                Result.success()
            }
            Result.success()
        } catch (excpetion: Exception) {
            Result.failure()
        }
    }
}