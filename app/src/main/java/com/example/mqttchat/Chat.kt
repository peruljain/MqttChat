package com.example.mqttchat


import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date
import java.sql.Timestamp


@Entity(tableName = "Chat")
data class Chat(val senderId: String, val receiverId: String,
                val message: String,
                @PrimaryKey(autoGenerate = true) val id: Int = 0)

