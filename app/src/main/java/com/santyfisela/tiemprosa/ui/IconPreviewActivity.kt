package com.santyfisela.tiemprosa.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.santyfisela.tiemprosa.ui.theme.TiemProsaTheme
import java.util.Calendar

class IconPreviewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TiemProsaTheme {
                IconPreviewScreen()
            }
        }
    }
}

@Composable
fun IconPreviewScreen() {
    val currentHour = Calendar.getInstance().get(Calendar.HOUR)
    val displayHour = if (currentHour == 0) 12 else currentHour
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "TiemProsa Dynamic Icons",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        Text(
            text = "Current Hour: $displayHour",
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items((1..12).toList()) { hour ->
                IconPreviewCard(
                    hour = hour,
                    isCurrentHour = hour == displayHour
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "The app icon changes every hour to show the current time, reflecting the literary clock concept.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Composable
fun IconPreviewCard(hour: Int, isCurrentHour: Boolean) {
    Card(
        modifier = Modifier
            .size(80.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isCurrentHour) 8.dp else 4.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (isCurrentHour) 
                MaterialTheme.colorScheme.primaryContainer 
            else 
                MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "$hour:00",
                fontSize = 12.sp,
                fontWeight = if (isCurrentHour) FontWeight.Bold else FontWeight.Normal,
                color = if (isCurrentHour) 
                    MaterialTheme.colorScheme.onPrimaryContainer 
                else 
                    MaterialTheme.colorScheme.onSurface
            )
        }
    }
}