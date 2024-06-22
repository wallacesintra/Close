package com.example.close.data.auth

import com.google.firebase.auth.FirebaseUser

interface UserSource {
    suspend fun createNewAccountWithEmailAndPassword(email: String, username: String,password:String): Resource<FirebaseUser>
//    suspend fun createNewAccountWithEmailAndPassword(email: String, password:String): CloseAuthResult

    suspend fun signInExistingUserWithEmailAndPassword(email: String, password: String): Resource<FirebaseUser>

    suspend fun getSignedInUser(): Resource<FirebaseUser>

    suspend fun signOutExistingUser()

}