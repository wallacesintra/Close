package com.example.close.presentation.profile.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.close.R
import com.example.close.presentation.components.ProfileImg

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileImgOptions(){
    val profileImgList = listOf(
        R.drawable.male_white,
        R.drawable.male_black1,
        R.drawable.female_dp,
        R.drawable.female_black1,
        R.drawable.female_black2
    )

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 80.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(profileImgList){photo ->
            ProfileImg(
                imgSize = 90.dp,
                imageResId = photo,
                modifier = Modifier.padding(5.dp)
            )
        }
    }
}