package com.santyfisela.tiemprosa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.work.*
import com.santyfisela.tiemprosa.data.Quote
import com.santyfisela.tiemprosa.data.QuoteRepository

import com.santyfisela.tiemprosa.ui.theme.TiemProsaTheme
import com.santyfisela.tiemprosa.utils.TextUtils
import com.santyfisela.tiemprosa.worker.WidgetUpdateWorker
import java.util.Calendar
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        scheduleWidgetUpdates()
        
        setContent {
            TiemProsaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LiteraryClockScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
    
    private fun scheduleWidgetUpdates() {
        val workRequest = PeriodicWorkRequestBuilder<WidgetUpdateWorker>(
            15, TimeUnit.MINUTES
        ).build()
        
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "widget_update",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}

@Composable
fun highlightTimeInQuote(text: String) = buildAnnotatedString {
    val timeInfo = TextUtils.getTimeFromText(text)
    
    if (timeInfo != null) {
        val (timeText, timeIndex) = timeInfo
        
        append(text.substring(0, timeIndex))
        
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color(0xFF1976D2))) {
            append(timeText)
        }
        
        val endIndex = timeIndex + timeText.length
        if (endIndex < text.length) {
            append(text.substring(endIndex))
        }
    } else {
        append(text)
    }
}

@Composable
fun LiteraryClockScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val repository = remember { QuoteRepository(context) }
    var currentQuote by remember { mutableStateOf<Quote?>(null) }
    var showDetails by remember { mutableStateOf(false) }
    var currentTime by remember { mutableStateOf("") }
    
    LaunchedEffect(Unit) {
        while (true) {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)
            currentTime = String.format("%02d:%02d", hour, minute)
            currentQuote = repository.getQuoteForCurrentHour()
            kotlinx.coroutines.delay(60000)
        }
    }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = currentTime,
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        
        currentQuote?.let { quote ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDetails = !showDetails },
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = highlightTimeInQuote(quote.text),
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 24.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                    if (showDetails) {
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        
                        Text(
                            text = "— ${quote.author}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.End,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 4.dp)
                        )
                        
                        Text(
                            text = quote.book,
                            fontSize = 12.sp,
                            fontStyle = FontStyle.Italic,
                            textAlign = TextAlign.End,
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } ?: run {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Text(
                    text = "No hay cita disponible para esta hora",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(20.dp)
                )
            }
        }
        
        Row(
            modifier = Modifier.padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { 
                    currentQuote = repository.getQuoteForCurrentHour()
                }
            ) {
                Text("Nueva cita")
            }
        }
        
        Text(
            text = "Toca la cita para ver detalles",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LiteraryClockScreenPreview() {
    TiemProsaTheme {
        LiteraryClockScreen()
    }
}