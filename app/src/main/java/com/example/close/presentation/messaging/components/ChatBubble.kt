package com.example.close.presentation.messaging.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.close.presentation.components.ProfileImg
import com.example.close.presentation.messaging.models.MessageUI
import com.example.close.presentation.models.profileImagesMap


//@Preview(showSystemUi = true, showBackground = true)
@Composable
fun ChatBubble(
    currentUserUid: String,
    messageUI: MessageUI
){
    Box(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(
                if (messageUI.sender.uid == currentUserUid) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground.copy(
                    alpha = 0.1f
                )
            )
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ProfileImg(
                    imageResId = profileImagesMap[messageUI.sender.profileImg]!!.imgResId,
                    imgSize = 30.dp,
                    modifier = Modifier.padding(end = 5.dp)
                )
                Text(
                    text = messageUI.sender.username,
                    color = if (messageUI.sender.uid == currentUserUid) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.primary
                )
            }
            Text(text = messageUI.message, color = if (messageUI.sender.uid == currentUserUid) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.onBackground)
        }
    }
}
