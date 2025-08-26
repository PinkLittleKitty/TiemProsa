package com.santyfisela.tiemprosa.icon

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import java.util.Calendar

class DynamicIconManager(private val context: Context) {
    
    companion object {
        private const val PREFS_NAME = "dynamic_icon_prefs"
        private const val KEY_LAST_HOUR = "last_hour"
        private const val TAG = "DynamicIconManager"
    }
    
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    
    fun updateIconForCurrentHour() {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR)
        val hour12 = if (currentHour == 0) 12 else currentHour
        
        val lastHour = prefs.getInt(KEY_LAST_HOUR, -1)
        
        if (lastHour != hour12) {
            Log.d(TAG, "Hour changed from $lastHour to $hour12")
            
            prefs.edit().putInt(KEY_LAST_HOUR, hour12).apply()
            
            Log.d(TAG, "Dynamic icon would update to hour $hour12")
        }
    }
    
    fun getCurrentHourIcon(): Int {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR)
        val hour12 = if (currentHour == 0) 12 else currentHour
        
        return getIconResourceForHour(hour12)
    }
    
    private fun getIconResourceForHour(hour: Int): Int {
        val resourceName = "ic_dynamic_hour_$hour"
        return context.resources.getIdentifier(resourceName, "drawable", context.packageName)
    }
    
    fun getAllHourIcons(): List<Pair<Int, Int>> {
        return (1..12).map { hour ->
            Pair(hour, getIconResourceForHour(hour))
        }
    }
    
    fun scheduleIconUpdates() {
        updateIconForCurrentHour()
    }
}