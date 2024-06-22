package com.example.close.presentation.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

@Composable
fun MediumText(
    text: String,
    modifier: Modifier = Modifier
){
    Text(
        text = text,
        fontWeight = FontWeight.W600,
        modifier = modifier
    )
}