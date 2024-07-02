package com.example.close.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun MediumFriendProfileContainer(
    userUid: String,
    username: String,
    goToFriendProfile: (String) -> Unit = {}
){
    Card(
        onClick = { goToFriendProfile(userUid) },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        modifier = Modifier
            .padding(top = 12.dp)
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
            }

        }
    }
}