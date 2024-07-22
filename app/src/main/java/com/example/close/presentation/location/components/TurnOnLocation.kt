package com.example.close.presentation.location.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.close.R

@Preview
@Composable
fun TurnOnLocation(modifier: Modifier = Modifier) {
    AlertDialog(
        onDismissRequest = { },
        confirmButton = {},
        icon = {
            Icon(painter = painterResource(id = R.drawable.location_off), contentDescription = stringResource(id = R.string.turn_on_location))
        },
        title = {
            Text(text = stringResource(id = R.string.turn_on_location))
        },
//        modifier = Modifier.fillMaxWidth()
    )
}