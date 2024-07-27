package com.example.close.data.messaging

import android.util.Log
import com.example.close.data.messaging.models.CloseChatRoom
import com.example.close.data.messaging.models.CloseMessage
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.UUID
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class CloseMessagingDataSource(
    private val firestoreDb: FirebaseFirestore
): CloseMessaging {

    private val closeChatsCollection = "CloseChats"


    override suspend fun createChatRoom(closeChatRoom: CloseChatRoom) {
        val deferred = CompletableDeferred<Unit>()

        val newChatRoom = hashMapOf(
            "chatUid" to closeChatRoom.chatUid,
            "members" to closeChatRoom.members,
            "messages" to closeChatRoom.messages,
            "timeStamp" to FieldValue.serverTimestamp(),
        )

        firestoreDb.collection(closeChatsCollection).document(closeChatRoom.chatUid)
            .set(newChatRoom)
            .addOnFailureListener { e ->
                Log.w("CloseChat: create room", "creation failed" + e.message)
                deferred.completeExceptionally(e)
            }
            .addOnSuccessListener {
                Log.d("CloseChat: create room", "creation successful")
                deferred.complete(Unit)
            }

        withContext(Dispatchers.IO) {
            deferred.await()
        }
    }

    override suspend fun sendMessage(roomUid: String, senderUid: String, textMessage: String) {
        val deferred = CompletableDeferred<Unit>()

        val newMessage = hashMapOf(
            "messageUid" to UUID.randomUUID().toString(),
            "senderUid" to senderUid,
            "message" to textMessage,
//            "timeStamp" to FieldValue.serverTimestamp(),
        )

        firestoreDb.collection(closeChatsCollection).document(roomUid)
            .update("messages", FieldValue.arrayUnion(newMessage))
            .addOnSuccessListener {
                Log.d("CloseChat: send message", "message sent")
                deferred.complete(Unit)
            }
            .addOnFailureListener { e ->
                Log.w("CloseChat: send message", "message not sent" + e.message)
                deferred.completeExceptionally(e)
            }

        withContext(Dispatchers.IO) {
            deferred.await()
        }
    }


    override suspend fun getChatRoomsForUid(userUid: String): List<CloseChatRoom> =
        withContext(Dispatchers.IO) {
            suspendCancellableCoroutine { continuation ->
                var hasResumed = false // Track if the continuation has already been resumed

                val listenerRegistration = firestoreDb.collection(closeChatsCollection)
                    .whereArrayContains("members", userUid)
                    .addSnapshotListener { value, error ->
                        if (error != null) {
                            if (!hasResumed) {
                                continuation.resumeWithException(error)
                                hasResumed = true
                            }
                            return@addSnapshotListener
                        }

                        if (!hasResumed) {
                            val chatRoomList =
                                value?.documents?.mapNotNull { it.toObject<CloseChatRoom>() }
                                    ?: emptyList()
                            continuation.resume(chatRoomList)
                            hasResumed = true
                        }
                    }

              //remove the listener after resuming to prevent memory leaks
                continuation.invokeOnCancellation {
                    listenerRegistration.remove()
                }
            }
        }

    override suspend fun getChatRoomMessages(chatRoomUid: String): Flow<List<CloseMessage>> =
        callbackFlow {
            val listener = firestoreDb.collection(closeChatsCollection)
                .document(chatRoomUid)
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        close() // Close the flow on error
                        Log.w("CloseChats: receiving message", e.message!!)
                        return@addSnapshotListener
                    }

                    val textMessages = snapshot?.toObject<CloseChatRoom>()
                    Log.d("CloseChats: receiving message", "messages received ${textMessages!!.messages.size}")

                    trySend(textMessages.messages)
                }

        awaitClose { listener.remove() }
    }


}