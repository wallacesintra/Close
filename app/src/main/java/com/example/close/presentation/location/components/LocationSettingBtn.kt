package com.example.close.presentation.location.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier


@OptIn(ExperimentalMaterial3Api::class)
//@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LocationSettingBtn(isSharingLocation: Boolean, sharingLocation: () -> Unit){
    val sheetState = rememberModalBottomSheetState()

    var showSetting by remember {
        mutableStateOf(false)
    }


    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        IconButton(
            onClick = { showSetting = !showSetting },
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground
            )
        ) {
            Icon(imageVector = Icons.Default.Settings, contentDescription = "setting")
        }

        AnimatedVisibility(visible = showSetting) {
            ModalBottomSheet(
                onDismissRequest = { showSetting = false },
                sheetState = sheetState,
                modifier = Modifier
                    .weight(1.0f)
                    .fillMaxWidth()
//                    .fillMaxHeight(0.7f)

            ) {
                LocationSettings(isSharingLocation = isSharingLocation, sharingLocation = sharingLocation)
            }
        }

    }
}