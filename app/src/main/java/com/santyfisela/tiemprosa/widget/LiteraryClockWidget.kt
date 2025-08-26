package com.santyfisela.tiemprosa.widget

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.santyfisela.tiemprosa.MainActivity
import com.santyfisela.tiemprosa.data.Quote
import com.santyfisela.tiemprosa.data.QuoteRepository

class LiteraryClockWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val repository = QuoteRepository(context)
        val currentQuote = repository.getQuoteForCurrentHour()
        
        provideContent {
            GlanceTheme {
                LiteraryClockContent(
                    context = context,
                    quote = currentQuote
                )
            }
        }
    }
}

@Composable
private fun LiteraryClockContent(
    context: Context,
    quote: Quote?
) {
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(ColorProvider(androidx.compose.ui.graphics.Color.White))
            .padding(16.dp)
            .clickable(
                actionStartActivity(
                    Intent(context, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                )
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (quote != null) {
            // Main quote text - we'll make the time bold by formatting the text
            Text(
                text = formatQuoteWithBoldTime(quote.text),
                style = TextStyle(
                    fontSize = 13.sp,
                    color = ColorProvider(androidx.compose.ui.graphics.Color.Black),
                    textAlign = TextAlign.Center
                ),
                modifier = GlanceModifier.padding(bottom = 8.dp)
            )
            
            // Author and book info
            Text(
                text = "— ${quote.author}",
                style = TextStyle(
                    fontSize = 10.sp,
                    color = ColorProvider(androidx.compose.ui.graphics.Color(0xFF1976D2)),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                ),
                modifier = GlanceModifier.padding(bottom = 2.dp)
            )
            
            Text(
                text = quote.book,
                style = TextStyle(
                    fontSize = 9.sp,
                    color = ColorProvider(androidx.compose.ui.graphics.Color(0xFF666666)),
                    textAlign = TextAlign.Center
                )
            )
        } else {
            Text(
                text = "No hay cita disponible para esta hora",
                style = TextStyle(
                    fontSize = 13.sp,
                    color = ColorProvider(androidx.compose.ui.graphics.Color.Black),
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}

private fun formatQuoteWithBoldTime(text: String): String {
    // Since Glance doesn't support rich text, we'll use uppercase for emphasis
    val timePatterns = listOf(
        "una de la madrugada", "dos de la mañana", "tres de la madrugada", "cuatro de la mañana",
        "cinco de la mañana", "seis de la mañana", "siete en punto", "ocho de la mañana",
        "nueve menos diez", "diez de la mañana", "once de la mañana", "mediodía", "doce en punto",
        "una de la tarde", "dos de la tarde", "tres de la tarde", "cuatro de la tarde",
        "cinco de la tarde", "seis de la tarde", "siete de la noche", "ocho de la noche",
        "nueve de la noche", "diez de la noche", "once de la noche", "medianoche",
        "las doce", "las trece", "las catorce", "las quince", "las dieciséis", "las diecisiete",
        "las dieciocho", "las diecinueve", "las veinte", "las veintiuna", "las veintidós", "las veintitrés",
        "doce de la noche", "la una", "las dos", "las tres", "las cuatro", "las cinco",
        "las seis", "las siete", "las ocho", "las nueve", "las diez", "las once"
    )
    
    var result = text
    val lowerText = text.lowercase()
    
    for (pattern in timePatterns) {
        val index = lowerText.indexOf(pattern.lowercase())
        if (index != -1) {
            val originalTime = text.substring(index, index + pattern.length)
            val upperTime = originalTime.uppercase()
            result = text.replaceRange(index, index + pattern.length, upperTime)
            break
        }
    }
    
    return result
}