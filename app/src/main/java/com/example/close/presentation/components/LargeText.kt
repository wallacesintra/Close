package com.example.close.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun LargeText(
    text: String,
    modifier: Modifier = Modifier,
    isBold: Boolean = false,
    centerText: Boolean = false,
    fontSize: TextUnit = 24.sp
){
    Text(
        text = text,
        color = MaterialTheme.colorScheme.primary,
        textAlign = if (centerText) TextAlign.Center else TextAlign.Start,
        fontWeight = if (isBold) FontWeight.Bold else FontWeight.W300,
        modifier = modifier,
        fontSize = fontSize,
    )
}