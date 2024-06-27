package com.example.close.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp


//@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MediumFriendProfileContainer(
    userUid: String,
    username: String,
    userEmail: String,
    goToFriendProfile: (String) -> Unit = {}
){
    Card(
        onClick = { goToFriendProfile(userUid) },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        modifier = Modifier
//            .padding(vertical = 8.dp)
            .padding(top = 12.dp)
//            .height(80.d
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth(0.99f)
        ) {
            ProfileImg(imgSize = 60.dp, modifier = Modifier.padding(horizontal = 15.dp))

            Column {
                MediumText(text = username)
//                Text(
//                    text = userEmail,
//                    overflow = TextOverflow.Ellipsis,
//                    maxLines = 2
//                )
            }

        }
    }
}