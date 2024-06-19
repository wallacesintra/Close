package com.example.close.data.auth

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserDataSource(
    private val context: Context,
    private val auth: FirebaseAuth
): UserSource{
    override suspend fun createNewAccountWithEmailAndPassword(
        email: String,
        username: String,
        password: String

    ): Resource<FirebaseUser> {
        return withContext(Dispatchers.Main){
            try {
                val result =auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task->
                        if (task.isSuccessful){

                            Log.d("createUserWithEmail:success", task.toString())

                        }else{
                            Log.w("createUserWithEmail:failure", task.exception)

                            Toast.makeText(
                                context,
                                "Authentication failed.",
                                Toast.LENGTH_SHORT,
                            ).show()

                        }
                    }

                if (result.isSuccessful){
                    Resource.Success(result.result.user)
                }else{
                    Resource.Error(result.exception?.message.toString())
                }

            }catch (exception: Exception){
                Resource.Error(exception.message ?: "An error occurred")
            }
        }
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

    override suspend fun signOutExistingUser() {
        auth.signOut()
        Log.d("signOut:success", "sign out successful")
    }

}