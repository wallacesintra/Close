package com.example.close.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun LargeActionContainer(
    containerAction: () -> Unit = {},
    iconPainter: Painter,
    iconDescription: String,
    actionText: String,
    containerBackground: Color = MaterialTheme.colorScheme.background
){
    Card(
        onClick = containerAction,
        colors = CardDefaults.cardColors(
            containerColor = containerBackground
        )
        ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(0.99f)
        ) {
            Icon(
                painter = iconPainter,
                contentDescription = iconDescription,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            
            MediumText(text = actionText, modifier = Modifier.padding(horizontal = 24.dp))
        }
    }
}