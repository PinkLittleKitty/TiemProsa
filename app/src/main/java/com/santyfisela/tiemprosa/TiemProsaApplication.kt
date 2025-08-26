package com.santyfisela.tiemprosa

import android.app.Application
import androidx.work.*
import com.santyfisela.tiemprosa.worker.WidgetUpdateWorker
import java.util.concurrent.TimeUnit

class TiemProsaApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        scheduleWidgetUpdates()
    }
    
    private fun scheduleWidgetUpdates() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .build()
            
        val workRequest = PeriodicWorkRequestBuilder<WidgetUpdateWorker>(
            15, TimeUnit.MINUTES
        )
            .setConstraints(constraints)
            .build()
        
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "widget_update",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}