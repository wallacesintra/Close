package com.example.close.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.close.R

@Composable
fun ProfileImg(
    modifier: Modifier = Modifier,
    imageResId: Int,
    imgSize:  Dp
){
    Image(
        painter = painterResource(id = imageResId),
        contentDescription = stringResource(id = R.string.user_profile_img),
        contentScale = ContentScale.Crop,
        alignment = Alignment.Center,
        modifier = modifier
            .size(imgSize)
            .clip(CircleShape)
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.tertiary,
                shape = CircleShape
            )
    )
}