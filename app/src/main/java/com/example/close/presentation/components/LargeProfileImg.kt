package com.example.close.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.close.R

@Composable
fun LargeProfileImg(){
    Image(
        painter = painterResource(id = R.drawable.female_dp),
        contentDescription = stringResource(id = R.string.user_profile_img),
        contentScale = ContentScale.Crop,
        alignment = Alignment.Center,
        modifier = Modifier
            .padding(20.dp)
            .size(120.dp)
            .clip(CircleShape)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            )
    )
}