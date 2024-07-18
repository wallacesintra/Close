package com.example.close.presentation.profile.components

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.close.presentation.components.ProfileImg
import com.example.close.presentation.models.ProfileImages

//@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileImgOptions(
    changeProfileImageEvent: (String) -> Unit
){

    var profileImg by remember {
        mutableStateOf("female_dp")
    }
    var confirmNewProfileImage by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(confirmNewProfileImage ) {
        if (confirmNewProfileImage){
            changeProfileImageEvent(profileImg)
            Log.d("Change profile image", "change to $profileImg")
            confirmNewProfileImage = false
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 80.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(ProfileImages){ photo ->
            ProfileImg(
                imgSize = 90.dp,
                imageResId = photo.imgResId,
                modifier = Modifier
                    .padding(5.dp)
                    .clickable(
                        onClick = {
                            profileImg = photo.name
                            confirmNewProfileImage = true
                        }
                    )
            )
        }
    }
}