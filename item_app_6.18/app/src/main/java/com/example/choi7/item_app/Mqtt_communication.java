package com.example.choi7.item_app;

/**
 * mqtt 통신
 */
import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;


public class Mqtt_communication {
    static final String TOPIC = "hello";
    private MqttClient mqttClient;
    JSONObject json = new JSONObject();
    byte[] encodedPayload = new byte[0];
    //IMqttAsyncClient mqttClient;
    //IMqttToken token;


    public void init(String url) throws MqttException, UnsupportedEncodingException{
        //mqttClient = new MqttAsyncClient(url,MqttAsyncClient.generateClientId(), null);
        //token=mqttClient.connect();
        //token.waitForCompletion(2000);
         mqttClient=new MqttClient(url,MqttClient.generateClientId(),null);
         mqttClient.connect();
    }

    // 데이터 publish
    public void Publish(String msg,String pub_topic) throws MqttException, UnsupportedEncodingException {
        encodedPayload = msg.getBytes("UTF-8");
        MqttMessage message = new MqttMessage(encodedPayload);
        mqttClient.publish(pub_topic,message);
        Log.d("Tag_pub","Mqtt pub start");
    }

    static String sub;
    public String Subscribe(int qos,String sub_topic) throws MqttException, UnsupportedEncodingException{
        Log.d("Tag_sub:","Mqtt sub start");

        mqttClient.subscribe(sub_topic,qos);
        mqttClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                Log.d("Tag_sub_connectionlost:","Mqtt ReConnect");

                try{
                }catch(Exception e)
                {Log.d("Tag_sub_error:","MqttReConnect Error");}
            }
            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                sub=message.toString();
                Log.d("received",sub);

                //JSONObject json_1 = new JSONObject(new String(message.getPayload(), "UTF-8"));
                //String msg_1 = new String(message.getPayload(), "UTF-8");
                //int clone_num1 = msg_1.indexOf(':');
                //int midle_bar_num1 = msg_1.indexOf('}');
                //sub = msg_1.substring(clone_num1 + 1, midle_bar_num1); //현재 전력 사용량

            }
            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }

        });

        //token=mqttClient.subscribe(sub_topic,qos);
        //token.waitForCompletion(1000);
        return sub;
    }

    //연결종료
    public void disconnect() throws MqttException {
        mqttClient.disconnect();
    }

}
