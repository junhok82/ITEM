package com.example.choi7.item_app;
 /*
 * 로딩 화면
 * */
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.UnsupportedEncodingException;



public class Layout_load extends AppCompatActivity
{
    static Mqtt_communication mqtt;
    static String total_use_watt;
    static String day_use_watt;
    static String now_use_watt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_load);

        final ImageView im_ing = (ImageView) findViewById(R.id.im_ing);
        final ImageView im_ot = (ImageView) findViewById(R.id.im_ot);
        final ImageView im_et = (ImageView) findViewById(R.id.im_et);
        final ImageView im_rid = (ImageView) findViewById(R.id.im_rid);

        Handler delayHandler = new Handler();
        delayHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

        Animation animation1 = AnimationUtils.loadAnimation(Layout_load.this , R.anim.translate_load);
        animation1.setFillAfter(true);
        im_ing.startAnimation(animation1);
        im_ing.setAnimation(animation1);

            }

        }, 1300);            // 딜레이

        Handler delayHandler1 = new Handler();
        delayHandler1.postDelayed(new Runnable() {
            @Override
            public void run() {

                im_ot.setVisibility(View.VISIBLE);

                Animation animation2 = AnimationUtils.loadAnimation(Layout_load.this , R.anim.scale_load);
                animation2.setFillAfter(true);
                im_ot.startAnimation(animation2);
                im_ot.setAnimation(animation2);

            }

        }, 1600);            // 딜레이

        Handler delayHandler2 = new Handler();
        delayHandler2.postDelayed(new Runnable() {
            @Override
            public void run() {

                im_et.setVisibility(View.VISIBLE);

                Animation animation3 = AnimationUtils.loadAnimation(Layout_load.this , R.anim.scale_load);
                animation3.setFillAfter(true);
                im_et.startAnimation(animation3);
                im_et.setAnimation(animation3);

            }

        }, 1700);            // 딜레이

        Handler delayHandler3 = new Handler();
        delayHandler3.postDelayed(new Runnable() {
            @Override
            public void run() {

                im_rid.setVisibility(View.VISIBLE);

                Animation animation4 = AnimationUtils.loadAnimation(Layout_load.this , R.anim.scale_load);
                animation4.setFillAfter(true);
                im_rid.startAnimation(animation4);
                im_rid.setAnimation(animation4);

            }

        }, 1800);            // 딜레이


/*
        try {
            do {
                mqtt=new Mqtt_communication();
                mqtt.init("tcp://13.125.54.181:1883");
            }while (false);
            mqtt.Publish("msg","item/watt/receive");
            total_use_watt=mqtt.Subscribe(2,"item/watt/total_use_watt");
            day_use_watt=mqtt.Subscribe(2,"item/watt/day_use_watt");
            now_use_watt=mqtt.Subscribe(2,"item/watt/now_use_watt");

           // mqtt.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
*/


        Handler handler = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                Intent intent_load = new Intent(Layout_load.this, MainActivity.class);
                startActivity(intent_load);
                finish();
            }
        };

        handler.sendEmptyMessageDelayed(0,2500);
    }
}
