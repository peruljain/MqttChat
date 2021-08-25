package com.example.mqttchat

import android.content.Context
import android.util.Log
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*



class PahoMqqtClient {
    private var mqttAndroidClient: MqttAndroidClient? = null

    companion object {
        private const val TAG = "PahoMqttClient"
        private var pahoMqqtClient: PahoMqqtClient? = null
        val instance: PahoMqqtClient?
            get() {
                if (pahoMqqtClient == null) pahoMqqtClient = PahoMqqtClient()
                return pahoMqqtClient
            }
    }

    fun getMqttClient(context: Context?, brokerUrl: String?, clientId: String?): MqttAndroidClient {

        if(mqttAndroidClient == null) {
            mqttAndroidClient = MqttAndroidClient(context, brokerUrl, clientId)
        }

        try {
            val mqttConnectOptions = MqttConnectOptions()
            mqttConnectOptions.userName = SOLACE_CLIENT_USER_NAME
            mqttConnectOptions.password = SOLACE_CLIENT_PASSWORD.toCharArray()
            mqttAndroidClient!!.connect(mqttConnectOptions , null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken) {
                    mqttAndroidClient!!.setBufferOpts(disconnectedBufferOptions)
                    Log.d(TAG, "connect : Success")
                }

                override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                    Log.d(TAG, "connect : Failure $exception")
                }
            })
        } catch (e: MqttException) {
            e.printStackTrace()
        }
        return mqttAndroidClient!!
    }

    @Throws(MqttException::class)
    fun disconnect(client: MqttAndroidClient) {
        val mqttToken = client.disconnect()
        mqttToken.actionCallback = object : IMqttActionListener {
            override fun onSuccess(iMqttToken: IMqttToken) {
                Log.d(TAG, "Successfully disconnected")
            }

            override fun onFailure(iMqttToken: IMqttToken, throwable: Throwable) {
                Log.d(
                    TAG,
                    "Failed to disconnected $throwable"
                )
            }
        }
    }

    private val disconnectedBufferOptions: DisconnectedBufferOptions
        private get() {
            val disconnectedBufferOptions = DisconnectedBufferOptions()
            disconnectedBufferOptions.isBufferEnabled = true
            disconnectedBufferOptions.bufferSize = 100
            disconnectedBufferOptions.isPersistBuffer = false
            disconnectedBufferOptions.isDeleteOldestMessages = false
            return disconnectedBufferOptions
        }


    private val mqttConnectionOption: MqttConnectOptions
        private get() {
            val mqttConnectOptions = MqttConnectOptions()
            mqttConnectOptions.isCleanSession = false
            mqttConnectOptions.isAutomaticReconnect = true
            return mqttConnectOptions
        }


    fun publishMessage(client: MqttAndroidClient, msg: String, qos: Int, topic: String) {
        var encodedPayload = ByteArray(0)
        encodedPayload = msg.toByteArray(charset("UTF-8"))
        try {
            val message = MqttMessage(encodedPayload)
            message.id = 320
            message.isRetained = true
            message.qos = qos
            client.publish(topic, message)
            Log.d(TAG, "Publish Success $topic")
        } catch (e: MqttException) {
            Log.d(TAG, "Error Publishing to $topic: " + e.message)
            e.printStackTrace()
        }
    }

    @Throws(MqttException::class)
    fun subscribe(client: MqttAndroidClient, topic: String, qos: Int) {
        val token = client.subscribe(topic, qos)
        token.actionCallback = object : IMqttActionListener {
            override fun onSuccess(iMqttToken: IMqttToken) {
                Log.d(TAG, "Subscribe Successfully $topic")
            }

            override fun onFailure(iMqttToken: IMqttToken, throwable: Throwable) {
                Log.e(TAG, "Subscribe Failed $topic")
            }
        }
    }

    @Throws(MqttException::class)
    fun unSubscribe(client: MqttAndroidClient, topic: String) {
        val token = client.unsubscribe(topic)
        token.actionCallback = object : IMqttActionListener {
            override fun onSuccess(iMqttToken: IMqttToken) {
                Log.d(TAG, "UnSubscribe Successfully $topic")
            }

            override fun onFailure(iMqttToken: IMqttToken, throwable: Throwable) {
                Log.e(TAG, "UnSubscribe Failed $topic")
            }
        }
    }


}