package com.example.mqttchat


import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.annotation.Nullable
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.mqttchat.PahoMqqtClient
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttMessage
import java.lang.Exception
import java.lang.UnsupportedOperationException

/**
 * @author yuana <andhikayuana></andhikayuana>@gmail.com>
 * @since 8/11/17
 */
class MqttMessageService : Service() {
    private var pahoMqqtClient: PahoMqqtClient? = null
    private var mqttAndroidClient: MqttAndroidClient? = null
    private var baseUrl: String? = null
    private var clientId: String? = null

    companion object {
        const val HANDLE_MESSAGE = "handle_message"
        private val TAG = "MqttMessageService"
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")
        baseUrl = SOLACE_MQTT_HOST
        clientId = CLIENT_ID
        pahoMqqtClient = PahoMqqtClient.instance
        mqttAndroidClient = pahoMqqtClient!!
            .getMqttClient(applicationContext, baseUrl, clientId)
        mqttAndroidClient!!.setCallback(object : MqttCallbackExtended {
            override fun connectComplete(reconnect: Boolean, serverURI: String) {}
            override fun connectionLost(cause: Throwable) {}

            @Throws(Exception::class)
            override fun messageArrived(topic: String, message: MqttMessage) {
                Log.d(TAG, "Messsage Arrived")
                handleMessage(message.toString())
            }

            override fun deliveryComplete(token: IMqttDeliveryToken) {}
        })
    }

    private fun handleMessage(msg: String) {
        val intent = Intent()
        intent.action = HANDLE_MESSAGE
        intent.putExtra(DATA, msg)
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand")
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }

    @Nullable
    override fun onBind(intent: Intent): IBinder? {
        Log.d(TAG, "onBind")
        throw UnsupportedOperationException("Not yet implemented")
    }


}