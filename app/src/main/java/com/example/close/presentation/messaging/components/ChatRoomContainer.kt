package com.example.close.presentation.messaging.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.close.presentation.components.MediumText
import com.example.close.presentation.components.ProfileImg
import com.example.close.presentation.messaging.models.CloseChatRoomUI
import com.example.close.presentation.models.profileImagesMap


@Composable
fun ChatRoomContainer(
    modifier: Modifier = Modifier,
    closeChatRoomUi: CloseChatRoomUI,
    goToChatRoom: (String) -> Unit = {}
){
    Card(
        onClick = { goToChatRoom(closeChatRoomUi.chatUid) },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(55.dp)
            ) {
                Row {
                    ProfileImg(
                        imageResId = profileImagesMap[closeChatRoomUi.members[0].profileImg]!!.imgResId,
                        imgSize = 40.dp
                    )
                }
                Box(modifier = Modifier.align(Alignment.BottomEnd)) {
                    ProfileImg(
                        imageResId = profileImagesMap[closeChatRoomUi.members[1].profileImg]!!.imgResId,
                        imgSize = 40.dp
                    )
                }
            }

            Column(
                modifier =Modifier.padding(10.dp)
            ) {
                MediumText(
                    text = closeChatRoomUi.members.joinToString(separator = ", ") { member -> member.username },
                    isBold = true,
                    color = MaterialTheme.colorScheme.tertiary
                )
                MediumText(
                    text = if (closeChatRoomUi.messages.isEmpty()) "start a conversation" else closeChatRoomUi.messages.last().message ,
                    modifier = Modifier.alpha(0.5f)
                )
            }
        }
    }
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ChatRoomContainerPreview(){
//    ChatRoomContainer(
//        closeChatRoomUi = CloseChatRoomUI(
//            members =
//        )
//    )
}