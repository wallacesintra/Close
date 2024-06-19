package com.example.close.data.auth

import com.google.firebase.auth.FirebaseUser

interface UserSource {
    suspend fun createNewAccountWithEmailAndPassword(email: String, username: String,password:String): Resource<FirebaseUser>

    suspend fun signInExistingUserWithEmailAndPassword(email: String, password: String): FirebaseUser?

    suspend fun signOutExistingUser()

}