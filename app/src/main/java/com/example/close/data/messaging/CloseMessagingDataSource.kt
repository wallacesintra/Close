package com.example.close.data.messaging

import android.util.Log
import com.example.close.data.messaging.models.CloseChatRoom
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

        withContext(Dispatchers.IO){
            deferred.await()
        }
    }

    override suspend fun sendMessage(roomUid: String, senderUid: String,textMessage: String) {
        val deferred = CompletableDeferred<Unit>()

        val newMessage = hashMapOf(
            "messageUid" to UUID.randomUUID().timestamp(),
            "senderUid" to senderUid,
            "message" to textMessage,
            "timeStamp" to FieldValue.serverTimestamp(),
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

        withContext(Dispatchers.IO){
            deferred.await()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getChatRoomsForUid(userUid: String): List<CloseChatRoom> = withContext(Dispatchers.IO){
        suspendCancellableCoroutine { continuation ->
            firestoreDb.collection(closeChatsCollection)
                .whereArrayContains("members", userUid)
                .addSnapshotListener { value, error ->
                    if (error != null){
                        if (continuation.isActive){
                            continuation.resumeWithException(error)
                        }
                        Log.w("CloseChat: message listener", "listen failed, $error")
                        continuation.resume(emptyList())
                    }

                    val chatRoomList = mutableListOf<CloseChatRoom>()

                    for (doc in value!!){
                        val chatRoom = doc.toObject<CloseChatRoom>()

                        chatRoomList.add(
                            CloseChatRoom(
                                chatUid = chatRoom.chatUid,
                                members = chatRoom.members,
                                messages = chatRoom.messages
                            )
                        )
                    }
                    Log.d("CloseChat: message listener", " listen successful")
                    continuation.resume(chatRoomList)
                }
        }
    }


}