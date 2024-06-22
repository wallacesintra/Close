package com.example.close.data.database

import android.util.Log
import com.example.close.presentation.models.CloseUserData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CloseUserDataSource(
    private val firestoreDb: FirebaseFirestore
): CloseUserSource {
    override suspend fun addNewCloseUser(newUser: CloseUserData) {
        val deferred = CompletableDeferred<Unit>()

        firestoreDb.collection("closeUsers").document(newUser.uid)
            .set(newUser)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("firestore:add user", "user added successful: ${newUser.username}")
                } else {
                    Log.w("firestore:add user", task.exception.toString())
                }
                deferred.complete(Unit)
            }

        withContext(Dispatchers.IO) {
            deferred.await()
        }
    }

    override suspend fun getSignInUser(uid: String): CloseUserData {
        val deferred = CompletableDeferred<CloseUserData>()

        firestoreDb.collection("closeUsers").document(uid)
            .get()
            .addOnSuccessListener { user ->
                val userData = user.toObject<CloseUserData>()
                Log.d("firestore:get user: successful", "user uid: " + userData?.email)

                if (userData != null){
                    deferred.complete(userData)
                }

            }
            .addOnFailureListener {e ->
                Log.w("firestore:get user: failure", e.message.toString())
                deferred.completeExceptionally(e)
            }

        return withContext(Dispatchers.IO){
            deferred.await()
        }
    }

    override suspend fun updateDetail(detailToUpdate: String, userUid: String, newValue: String) {

        val deferred = CompletableDeferred<Unit>()

        firestoreDb.collection("closeUsers").document(userUid)
            .update(
                detailToUpdate, newValue
            )
            .addOnSuccessListener {
                Log.d("firestore:update user detail", "user updated successful")
                deferred.complete(Unit)
            }
            .addOnFailureListener { e ->
                Log.w("firestore:update user detail", e.message.toString())
                deferred.completeExceptionally(e)
            }

        withContext(Dispatchers.IO){
            deferred.await()
        }
    }

}