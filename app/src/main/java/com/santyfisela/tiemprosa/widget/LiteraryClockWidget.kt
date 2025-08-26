package com.santyfisela.tiemprosa.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.glance.*
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.background
import androidx.glance.layout.*
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.*
import com.santyfisela.tiemprosa.data.Quote
import com.santyfisela.tiemprosa.data.QuoteRepository
import com.santyfisela.tiemprosa.utils.TextUtils

class LiteraryClockWidget : GlanceAppWidget() {

    override val stateDefinition: GlanceStateDefinition<*> = PreferencesGlanceStateDefinition

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val repository = QuoteRepository(context)
        val currentQuote = repository.getQuoteForCurrentHour()

        provideContent {
            GlanceTheme {
                LiteraryClockContent(
                    quote = currentQuote
                )
            }
        }
    }

    companion object {
        val showDetailsKey = booleanPreferencesKey("show_details")
    }
}

@Composable
private fun LiteraryClockContent(
    quote: Quote?
) {
    val prefs = currentState<Preferences>()
    val showDetails = prefs[LiteraryClockWidget.showDetailsKey] ?: false

    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(GlanceTheme.colors.surface)
            .cornerRadius(16.dp)
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = GlanceModifier
                .fillMaxWidth()
                .background(GlanceTheme.colors.surfaceVariant)
                .cornerRadius(12.dp)
                .padding(20.dp)
                .clickable(actionRunCallback<ToggleDetailsAction>()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (quote != null) {
                val timeInfo = TextUtils.getTimeFromText(quote.text)

                if (timeInfo != null) {
                    val (timeText, timeIndex) = timeInfo
                    val beforeTime = quote.text.substring(0, timeIndex)
                    val afterTime = quote.text.substring(timeIndex + timeText.length)

                    Text(
                        text = quote.text,
                        style = TextStyle(
                            fontSize = 16.sp,
                            color = GlanceTheme.colors.onSurface,
                            textAlign = TextAlign.Center
                        ),
                        modifier = GlanceModifier.padding(bottom = if (showDetails) 16.dp else 0.dp),
                        maxLines = 5
                    )
                } else {
                    Text(
                        text = quote.text,
                        style = TextStyle(
                            fontSize = 16.sp,
                            color = GlanceTheme.colors.onSurface,
                            textAlign = TextAlign.Center
                        ),
                        modifier = GlanceModifier.padding(bottom = if (showDetails) 16.dp else 0.dp)
                    )
                }

                if (showDetails) {
                    Spacer(modifier = GlanceModifier.height(12.dp))

                    Text(
                        text = "— ${quote.author}",
                        style = TextStyle(
                            fontSize = 14.sp,
                            color = GlanceTheme.colors.onSurface,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.End
                        ),
                        modifier = GlanceModifier.fillMaxWidth()
                    )

                    Text(
                        text = quote.book,
                        style = TextStyle(
                            fontSize = 12.sp,
                            color = GlanceTheme.colors.onSurfaceVariant,
                            textAlign = TextAlign.End
                        ),
                        modifier = GlanceModifier.fillMaxWidth()
                    )
                }
            } else {
                Text(
                    text = "No hay cita disponible para esta hora",
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = GlanceTheme.colors.onSurface,
                        textAlign = TextAlign.Center
                    )
                )
            }
        }
    }
}

class ToggleDetailsAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: androidx.glance.action.ActionParameters
    ) {
        updateAppWidgetState(context, PreferencesGlanceStateDefinition, glanceId) { prefs ->
            val currentShowDetails = prefs[LiteraryClockWidget.showDetailsKey] ?: false
            prefs.toMutablePreferences().apply {
                this[LiteraryClockWidget.showDetailsKey] = !currentShowDetails
            }
        }
        LiteraryClockWidget().update(context, glanceId)
    }
}