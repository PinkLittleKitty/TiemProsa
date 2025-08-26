package com.santyfisela.tiemprosa.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.widget.RemoteViews
import com.santyfisela.tiemprosa.R
import com.santyfisela.tiemprosa.data.Quote
import com.santyfisela.tiemprosa.data.QuoteRepository
import com.santyfisela.tiemprosa.utils.TextUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LiteraryClockWidget : AppWidgetProvider() {

    companion object {
        private const val PREFS_NAME = "com.santyfisela.tiemprosa.widget.LiteraryClockWidget"
        private const val PREF_PREFIX_KEY = "show_details_"
        private const val PREF_QUOTE_TEXT_KEY = "quote_text_"
        private const val PREF_QUOTE_AUTHOR_KEY = "quote_author_"
        private const val PREF_QUOTE_BOOK_KEY = "quote_book_"
        private const val PREF_QUOTE_HOUR_KEY = "quote_hour_"
        private const val PREF_QUOTE_MINUTE_KEY = "quote_minute_"
        const val ACTION_TOGGLE_DETAILS = "com.santyfisela.tiemprosa.widget.ACTION_TOGGLE_DETAILS"
        const val EXTRA_APP_WIDGET_ID = "app_widget_id"

        internal fun updateWidgetDisplay(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            val views = RemoteViews(context.packageName, R.layout.literary_clock_widget)
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val showDetails = prefs.getBoolean(PREF_PREFIX_KEY + appWidgetId, false)
            
            val cachedText = prefs.getString(PREF_QUOTE_TEXT_KEY + appWidgetId, null)
            val cachedAuthor = prefs.getString(PREF_QUOTE_AUTHOR_KEY + appWidgetId, null)
            val cachedBook = prefs.getString(PREF_QUOTE_BOOK_KEY + appWidgetId, null)
            
            var fullText: CharSequence = context.getString(R.string.widget_quote_unavailable)
            
            if (cachedText != null && cachedAuthor != null && cachedBook != null) {
                val calendar = java.util.Calendar.getInstance()
                val currentHour = calendar.get(java.util.Calendar.HOUR_OF_DAY)
                val currentMinute = calendar.get(java.util.Calendar.MINUTE)
                val currentQuote = Quote(currentHour, currentMinute, cachedText, cachedAuthor, cachedBook)
                
                val timeInfo = TextUtils.getTimeFromText(currentQuote.text)
                val quoteText = currentQuote.text
                
                if (timeInfo != null) {
                    val (timeText, timeIndex) = timeInfo
                    val spannableString = SpannableString(quoteText)
                    
                    val primaryColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        try {
                            context.getColor(android.R.color.system_accent1_600)
                        } catch (e: Exception) {
                            Color.parseColor("#1976D2")
                        }
                    } else {
                        Color.parseColor("#1976D2")
                    }
                    
                    spannableString.setSpan(
                        ForegroundColorSpan(primaryColor),
                        timeIndex,
                        timeIndex + timeText.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    spannableString.setSpan(
                        android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                        timeIndex,
                        timeIndex + timeText.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    
                    fullText = spannableString
                } else {
                    fullText = quoteText
                }

                if (showDetails) {
                    val baseText = fullText.toString()
                    val authorText = "\n\n— ${currentQuote.author}"
                    val bookText = "\n${currentQuote.book}"
                    val completeText = baseText + authorText + bookText
                    
                    val spannableComplete = SpannableString(completeText)
                    
                    if (fullText is SpannableString) {
                        val spans = fullText.getSpans(0, fullText.length, Any::class.java)
                        for (span in spans) {
                            val start = fullText.getSpanStart(span)
                            val end = fullText.getSpanEnd(span)
                            val flags = fullText.getSpanFlags(span)
                            spannableComplete.setSpan(span, start, end, flags)
                        }
                    }
                    
                    val authorStart = baseText.length + 3 
                    val authorEnd = authorStart + currentQuote.author.length
                    spannableComplete.setSpan(
                        android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                        authorStart,
                        authorEnd,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    
                    val bookStart = authorEnd + 1
                    val bookEnd = bookStart + currentQuote.book.length
                    spannableComplete.setSpan(
                        android.text.style.StyleSpan(android.graphics.Typeface.ITALIC),
                        bookStart,
                        bookEnd,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    
                    val dimmedColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        try {
                            context.getColor(android.R.color.system_neutral1_600)
                        } catch (e: Exception) {
                            Color.parseColor("#666666")
                        }
                    } else {
                        Color.parseColor("#666666")
                    }
                    
                    spannableComplete.setSpan(
                        ForegroundColorSpan(dimmedColor),
                        bookStart,
                        bookEnd,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    
                    fullText = spannableComplete
                } else {
                    val baseText = fullText.toString()
                    val instructionText = "\n\nToca la cita para ver detalles"
                    val completeText = baseText + instructionText
                    
                    val spannableComplete = SpannableString(completeText)
                    
                    if (fullText is SpannableString) {
                        val spans = fullText.getSpans(0, fullText.length, Any::class.java)
                        for (span in spans) {
                            val start = fullText.getSpanStart(span)
                            val end = fullText.getSpanEnd(span)
                            val flags = fullText.getSpanFlags(span)
                            spannableComplete.setSpan(span, start, end, flags)
                        }
                    }
                    
                    val instructionStart = baseText.length + 2
                    val instructionEnd = instructionStart + "Toca la cita para ver detalles".length
                    
                    val dimmedColor = Color.parseColor("#666666")
                    spannableComplete.setSpan(
                        ForegroundColorSpan(dimmedColor),
                        instructionStart,
                        instructionEnd,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    spannableComplete.setSpan(
                        android.text.style.RelativeSizeSpan(0.8f),
                        instructionStart,
                        instructionEnd,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    
                    fullText = spannableComplete
                }
            }

            views.setTextViewText(R.id.widget_quote, fullText)

            val toggleIntent = Intent(context, LiteraryClockWidget::class.java).apply {
                action = ACTION_TOGGLE_DETAILS
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            }

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                appWidgetId,
                toggleIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widget_quote, pendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        internal fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            val views = RemoteViews(context.packageName, R.layout.literary_clock_widget)
            val repository = QuoteRepository(context)

            GlobalScope.launch(Dispatchers.Main) {
                val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                val showDetails = prefs.getBoolean(PREF_PREFIX_KEY + appWidgetId, false)
                
                val calendar = java.util.Calendar.getInstance()
                val currentHour = calendar.get(java.util.Calendar.HOUR_OF_DAY)
                val currentMinute = calendar.get(java.util.Calendar.MINUTE)
                val cachedHour = prefs.getInt(PREF_QUOTE_HOUR_KEY + appWidgetId, -1)
                val cachedMinute = prefs.getInt(PREF_QUOTE_MINUTE_KEY + appWidgetId, -1)
                
                val currentQuote: Quote? = if (cachedHour != currentHour || cachedMinute != currentMinute) {
                    try {
                        val newQuote = repository.getQuoteForCurrentTime()
                        if (newQuote != null) {
                            prefs.edit().apply {
                                putString(PREF_QUOTE_TEXT_KEY + appWidgetId, newQuote.text)
                                putString(PREF_QUOTE_AUTHOR_KEY + appWidgetId, newQuote.author)
                                putString(PREF_QUOTE_BOOK_KEY + appWidgetId, newQuote.book)
                                putInt(PREF_QUOTE_HOUR_KEY + appWidgetId, currentHour)
                                putInt(PREF_QUOTE_MINUTE_KEY + appWidgetId, currentMinute)
                                apply()
                            }
                        }
                        newQuote
                    } catch (e: Exception) {
                        null
                    }
                } else {
                    val cachedText = prefs.getString(PREF_QUOTE_TEXT_KEY + appWidgetId, null)
                    val cachedAuthor = prefs.getString(PREF_QUOTE_AUTHOR_KEY + appWidgetId, null)
                    val cachedBook = prefs.getString(PREF_QUOTE_BOOK_KEY + appWidgetId, null)
                    
                    if (cachedText != null && cachedAuthor != null && cachedBook != null) {
                        Quote(currentHour, currentMinute, cachedText, cachedAuthor, cachedBook)
                    } else {
                        try {
                            repository.getQuoteForCurrentTime()
                        } catch (e: Exception) {
                            null
                        }
                    }
                }

                var fullText: CharSequence = context.getString(R.string.widget_quote_unavailable)

                if (currentQuote != null) {
                    val timeInfo = TextUtils.getTimeFromText(currentQuote.text)
                    val quoteText = currentQuote.text
                    
                    if (timeInfo != null) {
                        val (timeText, timeIndex) = timeInfo
                        val spannableString = SpannableString(quoteText)
                        
                        val primaryColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            try {
                                context.getColor(android.R.color.system_accent1_600)
                            } catch (e: Exception) {
                                Color.parseColor("#1976D2")
                            }
                        } else {
                            Color.parseColor("#1976D2")
                        }
                        
                        spannableString.setSpan(
                            ForegroundColorSpan(primaryColor),
                            timeIndex,
                            timeIndex + timeText.length,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        spannableString.setSpan(
                            android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                            timeIndex,
                            timeIndex + timeText.length,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        
                        fullText = spannableString
                    } else {
                        fullText = quoteText
                    }

                    if (showDetails) {
                        val baseText = fullText.toString()
                        val authorText = "\n\n— ${currentQuote.author}"
                        val bookText = "\n${currentQuote.book}"
                        val completeText = baseText + authorText + bookText
                        
                        val spannableComplete = SpannableString(completeText)
                        
                        if (fullText is SpannableString) {
                            val spans = fullText.getSpans(0, fullText.length, Any::class.java)
                            for (span in spans) {
                                val start = fullText.getSpanStart(span)
                                val end = fullText.getSpanEnd(span)
                                val flags = fullText.getSpanFlags(span)
                                spannableComplete.setSpan(span, start, end, flags)
                            }
                        }
                        
                        val authorStart = baseText.length + 3 
                        val authorEnd = authorStart + currentQuote.author.length
                        spannableComplete.setSpan(
                            android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                            authorStart,
                            authorEnd,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        
                        val bookStart = authorEnd + 1
                        val bookEnd = bookStart + currentQuote.book.length
                        spannableComplete.setSpan(
                            android.text.style.StyleSpan(android.graphics.Typeface.ITALIC),
                            bookStart,
                            bookEnd,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        
                        val dimmedColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            try {
                                context.getColor(android.R.color.system_neutral1_600)
                            } catch (e: Exception) {
                                Color.parseColor("#666666")
                            }
                        } else {
                            Color.parseColor("#666666")
                        }
                        
                        spannableComplete.setSpan(
                            ForegroundColorSpan(dimmedColor),
                            bookStart,
                            bookEnd,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        
                        fullText = spannableComplete
                    } else {
                        val baseText = fullText.toString()
                        val instructionText = "\n\nToca la cita para ver detalles"
                        val completeText = baseText + instructionText
                        
                        val spannableComplete = SpannableString(completeText)
                        
                        if (fullText is SpannableString) {
                            val spans = fullText.getSpans(0, fullText.length, Any::class.java)
                            for (span in spans) {
                                val start = fullText.getSpanStart(span)
                                val end = fullText.getSpanEnd(span)
                                val flags = fullText.getSpanFlags(span)
                                spannableComplete.setSpan(span, start, end, flags)
                            }
                        }
                        
                        val instructionStart = baseText.length + 2
                        val instructionEnd = instructionStart + "Toca la cita para ver detalles".length
                        
                        val dimmedColor = Color.parseColor("#666666")
                        spannableComplete.setSpan(
                            ForegroundColorSpan(dimmedColor),
                            instructionStart,
                            instructionEnd,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        spannableComplete.setSpan(
                            android.text.style.RelativeSizeSpan(0.8f),
                            instructionStart,
                            instructionEnd,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        
                        fullText = spannableComplete
                    }
                }

                views.setTextViewText(R.id.widget_quote, fullText)

                val toggleIntent = Intent(context, LiteraryClockWidget::class.java).apply {
                    action = ACTION_TOGGLE_DETAILS
                    putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                }

                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    appWidgetId,
                    toggleIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                views.setOnClickPendingIntent(R.id.widget_quote, pendingIntent)

                appWidgetManager.updateAppWidget(appWidgetId, views)
            }
        }
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        if (context == null || intent == null) return

        val action = intent.action
        if (ACTION_TOGGLE_DETAILS == action) {
            val appWidgetId = intent.getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID
            )
            if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                val currentShowDetails = prefs.getBoolean(PREF_PREFIX_KEY + appWidgetId, false)
                prefs.edit().putBoolean(PREF_PREFIX_KEY + appWidgetId, !currentShowDetails).apply()

                val appWidgetManager = AppWidgetManager.getInstance(context)
                updateWidgetDisplay(context, appWidgetManager, appWidgetId)
            }
        } else if (AppWidgetManager.ACTION_APPWIDGET_UPDATE == action) {
            val appWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS)
            if (appWidgetIds != null) {
                val appWidgetManager = AppWidgetManager.getInstance(context)
                this.onUpdate(context, appWidgetManager, appWidgetIds)
            }
        }
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            prefs.edit().apply {
                remove(PREF_PREFIX_KEY + appWidgetId)
                remove(PREF_QUOTE_TEXT_KEY + appWidgetId)
                remove(PREF_QUOTE_AUTHOR_KEY + appWidgetId)
                remove(PREF_QUOTE_BOOK_KEY + appWidgetId)
                remove(PREF_QUOTE_HOUR_KEY + appWidgetId)
                remove(PREF_QUOTE_MINUTE_KEY + appWidgetId)
                apply()
            }
        }
        super.onDeleted(context, appWidgetIds)
    }
}
