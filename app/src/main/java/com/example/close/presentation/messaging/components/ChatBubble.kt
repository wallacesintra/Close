package com.example.close.presentation.messaging.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.close.R
import com.example.close.presentation.components.ProfileImg
import com.example.close.presentation.messaging.models.MessageUI
import com.example.close.presentation.models.profileImagesMap
import com.example.close.utils.shareMessage


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChatBubble(
    currentUserUid: String,
    messageUI: MessageUI,
    deleteMessage: () -> Unit
){

    val context = LocalContext.current

    var showChatInteraction by remember {
        mutableStateOf(false)
    }
    Row(horizontalArrangement = Arrangement.Center) {
        ProfileImg(
            imageResId = profileImagesMap[messageUI.sender.profileImg]!!.imgResId,
            imgSize = 30.dp,
            modifier = Modifier.padding(end = 5.dp)
        )

        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
//                .fillMaxWidth()
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioHighBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
        ) {


            Box(
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .clip(RoundedCornerShape(topEnd = 0.dp, topStart = 10.dp, bottomEnd = 10.dp, bottomStart = 10.dp ))
                    .combinedClickable(
                        onClick = { showChatInteraction = false },
                        onLongClickLabel = stringResource(id = R.string.share_or_delete_message),
                        onLongClick = { showChatInteraction = !showChatInteraction }
                    )
                    .background(
                        if (messageUI.sender.uid == currentUserUid) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground.copy(
                            alpha = 0.1f
                        )
                    )
                    .weight(1.0f, false)
            ) {

                Column(
                    modifier = Modifier.padding(7.dp)
                ) {

                    Text(
                        text = messageUI.sender.username,
                        color = if (messageUI.sender.uid == currentUserUid) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = messageUI.message,
                        color = if (messageUI.sender.uid == currentUserUid) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.onBackground,
                    )
                }
            }


            AnimatedVisibility(visible = showChatInteraction) {
                Box(
                    modifier = Modifier
                ) {
                    ChatInteract(
                        shareMessage = { context.shareMessage(sender = messageUI.sender.username, message = messageUI.message) },
                        deleteMessage = deleteMessage,
//                        color = MaterialTheme.colorScheme.tertiaryContainer
////                        if (messageUI.sender.uid == currentUserUid) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground.copy(
////                            alpha = 0.1f
////                        )
                    )
                }
            }




        }
    }
}
