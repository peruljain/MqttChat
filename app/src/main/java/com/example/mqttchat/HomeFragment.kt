package com.example.mqttchat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_home.*
import org.eclipse.paho.android.service.MqttAndroidClient

class HomeFragment : Fragment() {

    private lateinit var pahoMqttClient: PahoMqqtClient
    private lateinit var mqttClient: MqttAndroidClient


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        pahoMqttClient = PahoMqqtClient.instance!!
        mqttClient = pahoMqttClient.getMqttClient(activity?.applicationContext, SOLACE_MQTT_HOST, CLIENT_ID)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribe.setOnClickListener {
            pahoMqttClient.subscribe(mqttClient, topic.text.toString(), 2)
        }

        publish.setOnClickListener {
            pahoMqttClient.publishMessage(mqttClient, message.text.toString(), 2, topic.text.toString())
        }
    }

}