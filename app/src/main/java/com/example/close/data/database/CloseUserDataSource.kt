package com.example.close.data.database

import android.util.Log
import com.example.close.data.database.models.CloseUsers
import com.example.close.data.database.models.FriendRequest
import com.example.close.presentation.models.CloseUserData
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

class CloseUserDataSource(
    private val firestoreDb: FirebaseFirestore
): CloseUserSource {

    private val friendRequestCollection = "CloseFriendRequests"
    private val closeUsersCollection = "closeUsers"

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

    override suspend fun getSignedInUser(uid: String): CloseUserData {
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

    override suspend fun findUserByUsername(username: String): List<CloseUsers> = withContext(Dispatchers.IO) {
        suspendCancellableCoroutine<List<CloseUsers>> { continuation ->
            firestoreDb.collection("closeUsers")
                .orderBy("username")
                .startAt(username)
                .endAt(username + "\uf8ff")
                .get()
                .addOnSuccessListener { users ->
                    Log.d("firestore: find by username", "searching")
                    val closeUsersList = mutableListOf<CloseUsers>()
                    for(i in users){
                        val user = i.toObject<CloseUserData>()
                        closeUsersList.add(CloseUsers(
                            uid = user.uid,
                            username = user.username,
                            bio = user.bio,
                            sharingLocation = user.shareLocation
                        ))
                        Log.d("firestore: find by username", "users found" + i.data)
                    }
                    continuation.resume(closeUsersList)
                }
                .addOnFailureListener {e ->
                    Log.w("firestore: find by username", "user not found" + e.message)
                    continuation.resume(emptyList())
                }
        }
    }

    override suspend fun getCloseUserByUid(closeUid: String): CloseUsers {
        val deferred = CompletableDeferred<CloseUsers>()

        firestoreDb.collection("closeUsers").document(closeUid)
            .get()
            .addOnSuccessListener { user ->
                val userData = user.toObject<CloseUsers>()
                Log.d("firestore:get user: successful", "user uid: " + userData?.username)

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

    override suspend fun addFriend(closeUid: String, newFriendUid: String) {
        val deferred = CompletableDeferred<Unit>()

        firestoreDb.collection(closeUsersCollection).document(closeUid)
            .update("friends", FieldValue.arrayUnion(newFriendUid))
            .addOnSuccessListener {
                Log.d("firestore: add friends", "friend added successful")
                deferred.complete(Unit)
            }
            .addOnFailureListener { e ->
                Log.w("firestore: add friends", "friend not added" + e.message)
                deferred.completeExceptionally(e)
            }

        withContext(Dispatchers.IO){
            deferred.await()
        }
    }

    override suspend fun sendFriendRequest(senderUid: String, receiverUid: String){
        val deferred = CompletableDeferred<Unit>()
        val requestUid = "$senderUid$receiverUid"
        val timeStamp = FieldValue.serverTimestamp()

        val newRequest = hashMapOf(
            "requestUid" to requestUid,
            "receiverUid" to receiverUid,
            "senderUid" to senderUid,
            "timeStamp" to FieldValue.serverTimestamp(),
            "accepted" to null
        )

        firestoreDb.collection(friendRequestCollection).document(requestUid)
            .set(newRequest)
            .addOnSuccessListener {
                Log.d("firestore: send request", "request sent successful")
                deferred.complete(Unit)
            }
            .addOnFailureListener { e ->
                Log.w("firestore: send request", "request sent failed with exception" + e.message)
                deferred.completeExceptionally(e)
            }

        withContext(Dispatchers.IO){
            deferred.await()
        }
    }

    override suspend fun getFriendRequests(receiverUid: String): List<FriendRequest> = withContext(Dispatchers.IO) {
            suspendCancellableCoroutine<List<FriendRequest>> { continuation ->
                firestoreDb.collection(friendRequestCollection)
//                    .orderBy("timeStamp")
                    .whereEqualTo("receiverUid", receiverUid)
                    .get()
                    .addOnSuccessListener { friendRequests ->
                        val requests = mutableListOf<FriendRequest>()

                        for (i in friendRequests) {
                            val friendRequest = i.toObject<FriendRequest>()
//                            val timestampLong = (friendRequest.timeStamp as Timestamp).seconds

                            requests.add(
                                FriendRequest(
                                    requestUid = friendRequest.requestUid,
                                    receiverUid = friendRequest.receiverUid,
                                    senderUid = friendRequest.senderUid,
//                                    timeStamp = friendRequest.timeStamp
//                                    timeStamp = timestampLong
                                )
                            )

                        }
                        continuation.resume(requests)

                        Log.d("firestore: fetch requests", "requests fetched successful")
                    }
                    .addOnFailureListener { e ->
                        Log.w("firestore: fetch requests", "requests failed: " + e.message)
                        continuation.resume(emptyList())
                    }
            }
        }

    override suspend fun acceptFriendRequest(requestUid: String) {
        val deferred = CompletableDeferred<Unit>()

        firestoreDb.collection(friendRequestCollection).document(requestUid)
            .update("accepted", true)
            .addOnSuccessListener {
                Log.d("firestore: accept request", "request accepted")
                deferred.complete(Unit)
            }
            .addOnFailureListener { e ->
                Log.w("firestore: accept request", "request not accepted " + e.message)
                deferred.completeExceptionally(e)
            }

        withContext(Dispatchers.IO){
            deferred.await()
        }
    }

}

