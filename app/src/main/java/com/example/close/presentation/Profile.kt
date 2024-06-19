package com.example.close.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.google.firebase.auth.FirebaseUser

@Composable
fun Profile(
    user: FirebaseUser,
    signOut: () -> Unit = {}
){
    Column {
        Text(text = "User Details")
        user.email?.let { Text(text = it) }
//        Text(text = user.providerData.toString())
        Text(text = user.uid)

        Button(onClick = signOut) {
            Text(text = "Sign out")
        }
    }
}