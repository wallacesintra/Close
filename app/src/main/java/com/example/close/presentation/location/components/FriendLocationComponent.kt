package com.example.close.presentation.location.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.close.presentation.components.MediumText
import com.example.close.presentation.components.ProfileImg
import com.google.android.gms.maps.model.LatLng


@Composable
fun FriendLocationComponent(
    onFriendComponentClick: (LatLng) ->Unit,
    friendName: String,
    friendProfileImg: Int,
    distance: String,
    location: LatLng = LatLng(0.0, 0.0)
){
    Card(
        onClick = { onFriendComponentClick(location) },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.9f)
        ),
        modifier = Modifier.padding(2.dp),
        shape = RoundedCornerShape(38.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(4.dp)
        ) {
            ProfileImg(
                imageResId = friendProfileImg,
                imgSize = 50.dp,
                modifier = Modifier.padding(2.dp)
            )

            Column {
                MediumText(
                    text = friendName,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(horizontal = 6.dp)
                )
                MediumText(
                    text = "$distance away",
                    fontSize = 16.sp,
                    isBold = true,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(horizontal = 6.dp)
                )
            }
        }
    }
}