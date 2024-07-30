package com.example.close.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.close.R


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun InfoSnackBar(){
    Box(
        modifier = Modifier.padding(10.dp)
    ) {
        Row {
            Icon(painter = painterResource(id = R.mipmap.ic_launcher), contentDescription = "app icon")
            Text(text ="Snack bar information")
        }
    }
}