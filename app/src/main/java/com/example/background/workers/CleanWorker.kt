package com.example.background.workers

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.background.OUTPUT_PATH
import java.io.File
import java.lang.Exception

class CleanWorker(context: Context, parameters: WorkerParameters) : Worker(context, parameters) {

    override fun doWork(): Result {
        val appContext = applicationContext
        makeStatusNotification("Limpando os arquivos temporarios", applicationContext)
        sleep()
        return try {
            val outputDirectory = File(appContext.filesDir, OUTPUT_PATH)
            Result.success()
            if (outputDirectory.exists()) {
                val entries = outputDirectory.listFiles()
                if (entries != null) {
                    for (entry in entries) {
                        val name = entry.name
                        if (name.isNotEmpty() && name.endsWith(".png")) {
                            entry.delete()
                            Log.i(TAG, "deleted $name - ${entry.delete()}")
                        }
                    }
                }
            }
            Result.success()
        } catch (error: Exception) {
            error.printStackTrace()
            Result.failure()
        }

    }
}