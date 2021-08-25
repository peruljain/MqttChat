package com.example.mqttchat

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ChatDao {

   @Insert()
   suspend fun insert(chat: Chat)

   @Query("SELECT * FROM Chat WHERE senderId=:senderId AND receiverId=:receiverId")
   fun getChats(senderId: String, receiverId: String) : LiveData<List<Chat>>


}