package com.example.mqttchat

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.mqttchat.MqttMessageService

class MessageReceiver : BroadcastReceiver() {

    private var listener: MessageReceiverListener? = null

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("Perul", "Inside Receiver")
        if (intent.action == MqttMessageService.HANDLE_MESSAGE) {
            val chat: String? = intent.getSerializableExtra(DATA) as String?
            listener!!.handleMessage(chat)
        }
    }

    fun setHandleMessage(listener: MessageReceiverListener?) {
        this.listener = listener
    }

    interface MessageReceiverListener {
        fun handleMessage(chat: String?)
    }
}