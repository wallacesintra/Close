package com.example.close.presentation.auth.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.close.R

@Composable
fun AuthMain(
    modifier: Modifier = Modifier,
    signUpEvent: () -> Unit = {},
    signInEvent: () -> Unit = {}
){
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(120.dp))
                .background(MaterialTheme.colorScheme.primary)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.close_app_icon),
                contentDescription = "app icon",
                tint = MaterialTheme.colorScheme.background
            )
        }
        
        Button(
            onClick = signInEvent,
            modifier = modifier
                .fillMaxWidth(0.80f)
                .padding(24.dp)
        ) {
            Text(
                text = stringResource(id = R.string.sign_in),
                fontSize = 20.sp
            )
        }

        Text(
            text = stringResource(id = R.string.create_account),
            fontSize = 18.sp,
            modifier = Modifier
                .clickable(
                    onClick = signUpEvent
                )
                .padding(3.dp)
        )
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AuthScreenPreview(){
    AuthMain()
}