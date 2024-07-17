package com.example.close.presentation.auth.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.close.data.database.models.CloseUserData

@Composable
fun Profile(
    user: CloseUserData,
    signOut: () -> Unit = {}
){
    Column {
        Text(text = "User Details")
//        user.email?.let { Text(text = it) }
//        Text(text = user.providerData.toString())
        Text(text = user.username)
        
        Text(text = user.uid)

        Button(onClick = signOut) {
            Text(text = "Sign out")
        }
    }
}