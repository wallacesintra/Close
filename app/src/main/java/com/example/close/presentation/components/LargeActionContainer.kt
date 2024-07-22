package com.example.close.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
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
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .alpha(0.9f)
                    .padding(vertical = 4.dp)
            )

            
            MediumText(
                text = actionText,
                modifier = Modifier
                    .alpha(0.7f)
                    .padding(horizontal = 24.dp)
            )
        }
    }
}