package com.example.close.data.auth

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class UserDataSource(
    private val context: Context,
    private val auth: FirebaseAuth
): UserSource{
    override suspend fun createNewAccountWithEmailAndPassword(
        email: String,
        username: String,
        password: String
    ): FirebaseUser? {
        var user: FirebaseUser? = null

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    //Sign in success
                    Log.d("createUserWithEmail:success", task.toString())
                    user = auth.currentUser

                } else {

                    // Sign in fails
                    Log.w("createUserWithEmail:failure", task.exception)

                    Toast.makeText(
                        context,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()

                }

            }

        return user
    }

    override suspend fun signInExistingUserWithEmailAndPassword(
        email: String,
        password: String
    ): FirebaseUser? {
        var user: FirebaseUser? = null

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    Log.d("signInWithEmail:success", task.toString())
                    user = auth.currentUser
                } else {
                    task.exception?.message?.let { Log.w("signInWithEmail:failure", it) }

                }

            }

        return user
    }

}