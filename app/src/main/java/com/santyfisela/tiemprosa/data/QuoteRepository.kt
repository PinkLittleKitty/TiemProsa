package com.santyfisela.tiemprosa.data

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.Calendar

class QuoteRepository(private val context: Context) {
    
    private var quotes: List<Quote> = emptyList()
    
    init {
        loadQuotes()
    }
    
    private fun loadQuotes() {
        try {
            val inputStream = context.assets.open("quotes.csv")
            val reader = BufferedReader(InputStreamReader(inputStream))
            
            quotes = reader.useLines { lines ->
                lines.drop(1)
                    .map { line -> parseCsvLine(line) }
                    .filterNotNull()
                    .toList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    private fun parseCsvLine(line: String): Quote? {
        val parts = mutableListOf<String>()
        var current = StringBuilder()
        var inQuotes = false
        
        for (char in line) {
            when {
                char == '"' -> inQuotes = !inQuotes
                char == ',' && !inQuotes -> {
                    parts.add(current.toString().trim())
                    current = StringBuilder()
                }
                else -> current.append(char)
            }
        }
        parts.add(current.toString().trim())
        
        return if (parts.size >= 4) {
            Quote(
                hour = parts[0].toIntOrNull() ?: 0,
                text = parts[1].trim('"'),
                author = parts[2].trim('"'),
                book = parts[3].trim('"')
            )
        } else null
    }
    
    fun getQuoteForCurrentHour(): Quote? {
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val hour24 = if (currentHour == 0) 24 else currentHour
        val quotesForHour = quotes.filter { it.hour == hour24 }
        return if (quotesForHour.isNotEmpty()) {
            quotesForHour.random()
        } else null
    }
    
    fun getQuoteForHour(hour: Int): Quote? {
        val hour24 = if (hour == 0) 24 else hour
        val quotesForHour = quotes.filter { it.hour == hour24 }
        return if (quotesForHour.isNotEmpty()) {
            quotesForHour.random()
        } else null
    }
    
    fun getAllQuotes(): List<Quote> = quotes
}