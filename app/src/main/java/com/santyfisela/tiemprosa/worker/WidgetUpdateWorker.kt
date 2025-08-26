package com.santyfisela.tiemprosa.worker

import android.content.Context
import androidx.glance.appwidget.updateAll
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.santyfisela.tiemprosa.widget.LiteraryClockWidget

class WidgetUpdateWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            LiteraryClockWidget().updateAll(applicationContext)
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}