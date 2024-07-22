package com.example.close.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun MediumText(
    text: String,
    modifier: Modifier = Modifier,
    isBold: Boolean = false,
    centerText: Boolean = false,
    fontSize: TextUnit = 16.sp,
    maxLines: Int = 1,
    color: Color = MaterialTheme.colorScheme.onBackground
){
    Text(
        text = text,
        textAlign = if (centerText) TextAlign.Center else TextAlign.Start,
        fontWeight = if (isBold) FontWeight.Bold else FontWeight.W300,
        maxLines = maxLines,
        color = color,
        modifier = modifier,
        fontSize = fontSize
    )
}