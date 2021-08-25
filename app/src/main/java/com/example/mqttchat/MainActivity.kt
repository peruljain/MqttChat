package com.example.mqttchat

import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import kotlinx.android.synthetic.main.activity_main.*
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.MqttClient
import java.lang.Exception

class MainActivity : AppCompatActivity(), MessageReceiver.MessageReceiverListener {


    private lateinit var pahoMqttClient: PahoMqqtClient
    private lateinit var mqttClient: MqttAndroidClient
    private var intentFilter: IntentFilter? = null
    private lateinit var messageReceiver: MessageReceiver



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        initMqttClient()
        initService()

    }

    private fun initService() {
        val intent = Intent(this, MqttMessageService::class.java)
        startService(intent)
    }

    override fun onResume() {
        super.onResume()
        messageReceiver = MessageReceiver()
        messageReceiver.setHandleMessage(this)
        intentFilter = IntentFilter(MqttMessageService.HANDLE_MESSAGE)
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, intentFilter!!)
    }

    override fun onPause() {
        super.onPause()
        if(intentFilter!=null) {
            try {
                unregisterReceiver(messageReceiver)
                intentFilter = null
            } catch (e: Exception) {

            }
        }
    }

    private fun initMqttClient() {
        pahoMqttClient = PahoMqqtClient.instance!!
        mqttClient = pahoMqttClient.getMqttClient(applicationContext, SOLACE_MQTT_HOST, CLIENT_ID)
    }

    override fun handleMessage(chat: String?) {
        Log.d("MainActivity", chat.toString())
        Toast.makeText(this,chat, Toast.LENGTH_LONG).show()
    }
}