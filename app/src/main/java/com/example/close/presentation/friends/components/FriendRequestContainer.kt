package com.example.close.presentation.friends.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.close.R
import com.example.close.presentation.components.MediumText
import com.example.close.presentation.components.ProfileImg
import com.example.close.presentation.friends.models.CloseFriendRequest
import com.example.close.presentation.friends.models.CloseUserState
import com.example.close.presentation.friends.viewmodels.CloseUserViewModel


//@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FriendRequestContainer(
    closeFriendRequest: CloseFriendRequest,
    confirmRequest: () -> Unit
){
    Box(
        modifier = Modifier
//            .heightIn(max = 800.dp)
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProfileImg(imgSize = 60.dp, modifier = Modifier.padding(10.dp))

            MediumText(text = closeFriendRequest.senderUsername)
        }

        Box(
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            IconButton(
                onClick = confirmRequest,
                colors = IconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.primary,
                    disabledContentColor = MaterialTheme.colorScheme.primaryContainer,
                    disabledContainerColor = Color.Transparent
                ),
                modifier = Modifier.padding(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = stringResource(id = R.string.confirm_request),
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }

}