package com.santyfisela.tiemprosa.worker

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.santyfisela.tiemprosa.icon.DynamicIconManager
import com.santyfisela.tiemprosa.widget.LiteraryClockWidget

class WidgetUpdateWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val appWidgetManager = AppWidgetManager.getInstance(applicationContext)
            val componentName = ComponentName(applicationContext, LiteraryClockWidget::class.java)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(componentName)

            if (appWidgetIds.isNotEmpty()) {
                val intent = Intent(applicationContext, LiteraryClockWidget::class.java).apply {
                    action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                    putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
                }
                applicationContext.sendBroadcast(intent)
            }
            
            val iconManager = DynamicIconManager(applicationContext)
            iconManager.updateIconForCurrentHour()
            
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}
