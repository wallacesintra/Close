package com.example.close.presentation.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

@Composable
fun MediumText(
    text: String,
    modifier: Modifier = Modifier
){
    Text(
        text = text,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.W600,
        modifier = modifier
    )
}