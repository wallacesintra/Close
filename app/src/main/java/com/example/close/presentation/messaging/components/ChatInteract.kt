package com.example.close.presentation.messaging.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.close.R


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ChatInteract(
    shareMessage : () -> Unit = {},
    deleteMessage: () -> Unit = {},
    color: Color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
){
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(topEnd = 10.dp, topStart = 0.dp, bottomEnd = 10.dp, bottomStart = 0.dp ))
            .background(color)

    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.padding(4.dp)
        ) {
            IconButton(onClick =  shareMessage  , modifier = Modifier.size(28.dp)) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = stringResource(id = R.string.share_message),
                    tint = MaterialTheme.colorScheme.secondary
                )
            }

            IconButton(onClick = deleteMessage, modifier = Modifier.size(34.dp)) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(id = R.string.delete_message),
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}