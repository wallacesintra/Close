package com.example.close.presentation.models

import com.example.close.R

data class ProfileImage(
    val name: String,
    val imgResId: Int
)


val ProfileImages = listOf(
    ProfileImage(name = "female_black1", R.drawable.female_black1),
    ProfileImage(name = "female_black2", R.drawable.female_black2),
    ProfileImage(name = "female_dp", R.drawable.female_dp),
    ProfileImage(name =  "male_black1", imgResId = R.drawable.male_black1),
    ProfileImage(name = "male_white", imgResId = R.drawable.male_white)
)

val profileImagesMap = ProfileImages.associateBy { it.name }