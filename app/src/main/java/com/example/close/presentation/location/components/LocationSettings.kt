package com.example.close.presentation.location.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.close.R
import com.example.close.presentation.components.MediumText


//@Preview(showSystemUi = true, showBackground = true)
@Composable
fun LocationSettings(isSharingLocation:Boolean, sharingLocation: () -> Unit){

    var shareLocation by remember {
        mutableStateOf(isSharingLocation)
    }

    if (shareLocation){
        sharingLocation()
    }

    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioHighBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
    ){
        MediumText(
            text = stringResource(id = R.string.setting),
            color = MaterialTheme.colorScheme.secondary,
            centerText = true,
            isBold = true,
            fontSize = 24.sp,
            modifier = Modifier.padding(10.dp)
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {

            MediumText(text = stringResource(id = R.string.share_location))

            Switch(
                checked = shareLocation,
                onCheckedChange = { shareLocation = it },
                thumbContent = {
                    if (shareLocation){
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "sharing location"
                        )
                    }
                },
                colors = SwitchDefaults.colors(
                    checkedIconColor = MaterialTheme.colorScheme.onTertiary,
                    checkedThumbColor = MaterialTheme.colorScheme.tertiary,
                    checkedTrackColor = MaterialTheme.colorScheme.tertiaryContainer,
                    uncheckedThumbColor = MaterialTheme.colorScheme.background,
                    uncheckedTrackColor = MaterialTheme.colorScheme.tertiaryContainer,
                    uncheckedBorderColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            )
        }

        if (shareLocation){
            MediumText(
                text = stringResource(id = R.string.who_can_see),
                isBold = true,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier
                    .padding(10.dp)
                    .align(Alignment.Start)
            )
            FriendLocationShareList(enabled = true, text = stringResource(id = R.string.all_friends))
            FriendLocationShareList(enabled = true, text = stringResource(id = R.string.all_friends_except))
            FriendLocationShareList(enabled = true, text = stringResource(id = R.string.only_friends))

        }
    }
}


@Composable
fun FriendLocationShareList(
    text: String,
    enabled: Boolean = true
){
    var selected by remember {
        mutableStateOf(false)
    }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        MediumText(text = text)
        RadioButton(
            selected = selected,
            onClick = { selected = !selected },
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.tertiary
            ),
            enabled = enabled
//            modifier = Modifier.size(70.dp)
        )
    }

}