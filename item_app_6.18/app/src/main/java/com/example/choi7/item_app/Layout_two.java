package com.example.choi7.item_app;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.UnsupportedEncodingException;


/**
 * 원격제어
 * */


public class Layout_two extends AppCompatActivity {

    private final static String TAG = "Layout_two";
    final Context context = this;

    public static TextView mTextOutput;
    public static TextView mTextOutput2;

    public static Switch switch1_1;
    public static Switch switch1_2;
    public static Switch switch1_3;
    public static Switch switch1_4;

    public static Switch switch2_1;
    public static Switch switch2_2;
    public static Switch switch2_3;
    public static Switch switch2_4;

    public static Switch switch3_1;
    public static Switch switch3_2;
    public static Switch switch3_3;
    public static Switch switch3_4;

    public static Switch switch4_1;
    public static Switch switch4_2;
    public static Switch switch4_3;
    public static Switch switch4_4;

    public static int soclo;

    ImageView ivt2_1;
    ImageView ivt2_3;
    ImageView ivt2_4;
    static int switch_count1=0;
    static int switch_count2=0;
    static int switch_count3=0;
    static int switch_count4=0;

    // UI variable
    TextView apInfoTxt;
    TextView number1;
    TextView number2;
    TextView number3;
    TextView number4;

    Button ServOn;
    Button ServOff;


    int delete_state;

    TextView cs1;
    TextView cs2;
    TextView cs3;
    TextView cs4;

    Typeface the_o = Typeface.createFromAsset(getAssets(), "the_oegyeinseolmyeongseo.ttf");
    TextView te2_1;
    TextView te2_2;
    TextView te2_3;
    TextView te2_4;
    TextView te2_5;

    public static int ccc;
    public static RelativeLayout rl2_list1, rl2_list2, rl2_list3, rl2_list4;

    SharedPreferences pref;
    View dialogView;
    EditText dlgedt1;
    int tab_selected = 0;
    static String meg;
    static Mqtt_communication mqtt=new Mqtt_communication();;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_two);

        ivt2_1 = (ImageView) findViewById(R.id.ivt2_1);
        ivt2_3 = (ImageView) findViewById(R.id.ivt2_3);
        ivt2_4 = (ImageView) findViewById(R.id.ivt2_4);

        // Setup UI
        mTextOutput = (TextView) findViewById(R.id.textOutput);
        mTextOutput2 = (TextView) findViewById(R.id.textOutput2);
        apInfoTxt = (TextView)findViewById(R.id.apInfoTxtView);
        number1 = (TextView)findViewById(R.id.number1);
        number2 = (TextView)findViewById(R.id.number2);
        number3 = (TextView)findViewById(R.id.number3);
        number4 = (TextView)findViewById(R.id.number4);

        ServOn = (Button)findViewById(R.id.ServOn);
        ServOff = (Button)findViewById(R.id.ServOff);

        te2_1 = (TextView) findViewById(R.id.te2_1);
        te2_2 = (TextView) findViewById(R.id.te2_2);
        te2_3 = (TextView) findViewById(R.id.te2_3);
        te2_4 = (TextView) findViewById(R.id.te2_4);
        te2_5 = (TextView) findViewById(R.id.te2_5);

        te2_1.setTypeface(the_o);
        te2_2.setTypeface(the_o);
        te2_3.setTypeface(the_o);
        te2_4.setTypeface(the_o);
        te2_5.setTypeface(the_o);

        final LinearLayout gone_lin1 = (LinearLayout) findViewById(R.id.gone_lin1);
        final LinearLayout gone_lin2 = (LinearLayout) findViewById(R.id.gone_lin2);
        final LinearLayout gone_lin3_1 = (LinearLayout) findViewById(R.id.gone_lin3_1);
        final LinearLayout gone_lin3_2 = (LinearLayout) findViewById(R.id.gone_lin3_2);
        final LinearLayout gone_lin3_3 = (LinearLayout) findViewById(R.id.gone_lin3_3);
        final LinearLayout gone_lin3_4 = (LinearLayout) findViewById(R.id.gone_lin3_4);



        LinearLayout scale_lin1 = (LinearLayout) findViewById(R.id.scale_lin1);

        rl2_list1 = (RelativeLayout) findViewById(R.id.rl2_list1);
        rl2_list2 = (RelativeLayout) findViewById(R.id.rl2_list2);
        rl2_list3 = (RelativeLayout) findViewById(R.id.rl2_list3);
        rl2_list4 = (RelativeLayout) findViewById(R.id.rl2_list4);
        final TableLayout gone_tb2 = (TableLayout) findViewById(R.id.gone_tb2);

        final ImageView gone_iv2 = (ImageView) findViewById(R.id.ivt2_2);
        ImageView back1_1 = (ImageView) findViewById(R.id.back1_1);
        ImageView back1_2 = (ImageView) findViewById(R.id.back1_2);
        ImageView back1_3 = (ImageView) findViewById(R.id.back1_3);
        ImageView back1_4 = (ImageView) findViewById(R.id.back1_4);
        ImageView back2 = (ImageView) findViewById(R.id.back2);


        switch1_1 = (Switch) findViewById(R.id.switch1_1);
        switch1_2 = (Switch) findViewById(R.id.switch1_2);
        switch1_3 = (Switch) findViewById(R.id.switch1_3);
        switch1_4 = (Switch) findViewById(R.id.switch1_4);

        switch2_1 = (Switch) findViewById(R.id.switch2_1);
        switch2_2 = (Switch) findViewById(R.id.switch2_2);
        switch2_3 = (Switch) findViewById(R.id.switch2_3);
        switch2_4 = (Switch) findViewById(R.id.switch2_4);

        switch3_1 = (Switch) findViewById(R.id.switch3_1);
        switch3_2 = (Switch) findViewById(R.id.switch3_2);
        switch3_3 = (Switch) findViewById(R.id.switch3_3);
        switch3_4 = (Switch) findViewById(R.id.switch3_4);

        switch4_1 = (Switch) findViewById(R.id.switch4_1);
        switch4_2 = (Switch) findViewById(R.id.switch4_2);
        switch4_3 = (Switch) findViewById(R.id.switch4_3);
        switch4_4 = (Switch) findViewById(R.id.switch4_4);

        pref= getSharedPreferences("pref", MODE_PRIVATE);
        number1.setText(pref.getString("tab01", "멀티탭 1"));
        registerForContextMenu(rl2_list1);
        number2.setText(pref.getString("tab02", "멀티탭 2"));
        registerForContextMenu(rl2_list2);
        number3.setText(pref.getString("tab03", "멀티탭 3"));
        registerForContextMenu(rl2_list3);
        number4.setText(pref.getString("tab04", "멀티탭 4"));
        registerForContextMenu(rl2_list4);


        delete_state=pref.getInt("tab_del01",0);
        if(delete_state==1) {
            rl2_list1.setVisibility(View.GONE);
        }
        delete_state=pref.getInt("tab_del02",0);
        if(delete_state==1) {
            rl2_list2.setVisibility(View.GONE);
        }
        delete_state=pref.getInt("tab_del03",0);
        if(delete_state==1) {
            rl2_list3.setVisibility(View.GONE);
        }
        delete_state=pref.getInt("tab_del04",0);
        if(delete_state==1) {
            rl2_list4.setVisibility(View.GONE);
        }


        Animation animation1 = AnimationUtils.loadAnimation(Layout_two.this, R.anim.scale_anim);
        animation1.setFillAfter(true);
        scale_lin1.startAnimation(animation1);
        scale_lin1.setAnimation(animation1);

        Handler delayHandler = new Handler();
        delayHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation animation = new AlphaAnimation(0, 1);
                animation.setDuration(780);
                Animation animation2 = AnimationUtils.loadAnimation(Layout_two.this, R.anim.scale_anim2);
                animation2.setFillAfter(true);

                gone_tb2.setVisibility(View.VISIBLE);
                gone_iv2.setVisibility(View.VISIBLE);
                gone_lin1.setVisibility(View.VISIBLE);

                gone_tb2.setAnimation(animation);
                gone_iv2.setAnimation(animation);
                gone_lin1.setAnimation(animation2);
            }

        }, 850);            // 딜레이

       if(ccc == 1)
            rl2_list1.setVisibility(View.VISIBLE);

        View.OnClickListener click_listener1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.rl2_list1:
                        gone_lin2.setVisibility(View.GONE);
                        gone_lin3_1.setVisibility(View.VISIBLE);
                        break;
                }
            }
        };          // 첫번째 전력 제어에서 기기 선택
        View.OnClickListener click_listener1_2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.rl2_list2:
                        gone_lin2.setVisibility(View.GONE);
                        gone_lin3_2.setVisibility(View.VISIBLE);
                        break;
                }
            }
        };          // 두번째 전력 제어에서 기기 선택
        View.OnClickListener click_listener1_3 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.rl2_list3:
                        gone_lin2.setVisibility(View.GONE);
                        gone_lin3_3.setVisibility(View.VISIBLE);
                        break;
                }
            }
        };          // 세번째 전력 제어에서 기기 선택
        View.OnClickListener click_listener1_4 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.rl2_list4:
                        gone_lin2.setVisibility(View.GONE);
                        gone_lin3_4.setVisibility(View.VISIBLE);
                        break;
                }
            }
        };          // 네번째 전력 제어에서 기기 선택


        View.OnClickListener click_listener2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.back1_1:
                        gone_lin3_1.setVisibility(View.GONE);
                        gone_lin2.setVisibility(View.VISIBLE);
                        break;
                }
            }
        };          // 첫 번째 원격 제어에서 기기 선택 화면으로 돌아오기

        View.OnClickListener click_listener2_2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.back1_2:
                        gone_lin3_2.setVisibility(View.GONE);
                        gone_lin2.setVisibility(View.VISIBLE);
                        break;
                }
            }
        };          // 두 번째 원격 제어에서 기기 선택 화면으로 돌아오기
        View.OnClickListener click_listener2_3 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.back1_3:
                        gone_lin3_3.setVisibility(View.GONE);
                        gone_lin2.setVisibility(View.VISIBLE);
                        break;
                }
            }
        };          // 세 번째 원격 제어에서 기기 선택 화면으로 돌아오기
        View.OnClickListener click_listener2_4 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.back1_4:
                        gone_lin3_4.setVisibility(View.GONE);
                        gone_lin2.setVisibility(View.VISIBLE);
                        break;
                }
            }
        };          // 네 번째 원격 제어에서 기기 선택 화면으로 돌아오기




        View.OnClickListener click_listener_iv2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.ivt2_1:
                        Intent im_it1 = new Intent(Layout_two.this,Layout_one.class);
                        startActivity(im_it1);
                        finish();
                        break;
                    case R.id.ivt2_3:
                        Intent im_it3 = new Intent(Layout_two.this,Layout_three.class);
                        startActivity(im_it3);
                        finish();
                        break;
                    case R.id.ivt2_4:
                        Intent im_it4 = new Intent(Layout_two.this,Layout_four.class);
                        startActivity(im_it4);
                        finish();
                        break;
                }
            }
        };          // [메뉴]다른 메뉴로 넘어가기
        ivt2_1.setOnClickListener(click_listener_iv2);
        ivt2_3.setOnClickListener(click_listener_iv2);
        ivt2_4.setOnClickListener(click_listener_iv2);




        //첫번째 멀티콘센트 스위치 전원제어
        switch1_1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    // 버튼을 눌렀을 때
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    // 버튼을 누른 상태로 움직이고 있을 때

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    // 버튼에서 손을 떼었을 때
                    DialogInterface.OnClickListener dialogClickListener1 = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    //No 버튼을 클릭했을때 처리

                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    switch1_1.toggle(); //Yes 버튼을 클릭했을때 처리
                                    if(switch_count1==0){ //스위치 off
                                        switch_count1=1;
                                        try{
                                            mqtt.init("tcp://13.125.54.181:1883");
                                            meg="12";
                                            mqtt.Publish(meg,"item/control");
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        } catch (MqttException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    else if(switch_count1==1){  //스위치 on
                                        switch_count1=0;
                                        try{
                                            mqtt.init("tcp://13.125.54.181:1883");
                                            meg="11";
                                            mqtt.Publish(meg,"item/control");
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        } catch (MqttException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    break;
                            }
                        }
                    };
                    final AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                    builder1.setMessage("Are you sure?");
                    builder1.setNegativeButton("Yes", dialogClickListener1);
                    builder1.setPositiveButton("No", dialogClickListener1);
                    builder1.show();
                }

                return true;
            }
        });

        switch1_2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    // 버튼을 눌렀을 때
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    // 버튼을 누른 상태로 움직이고 있을 때

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    // 버튼에서 손을 떼었을 때

                    DialogInterface.OnClickListener dialogClickListener1 = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    //No 버튼을 클릭했을때 처리
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    switch1_2.toggle(); //Yes 버튼을 클릭했을때 처리
                                    if(switch_count2==0){ //스위치 off
                                        switch_count2=1;
                                        try{
                                            mqtt.init("tcp://13.125.54.181:1883");
                                            meg="14";
                                            mqtt.Publish(meg,"item/control");
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        } catch (MqttException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    else if(switch_count2==1){  //스위치 on
                                        switch_count2=0;
                                        try{
                                            mqtt.init("tcp://13.125.54.181:1883");
                                            meg="13";
                                            mqtt.Publish(meg,"item/control");
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        } catch (MqttException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    break;
                            }
                        }
                    };

                    final AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                    builder1.setMessage("Are you sure?");
                    builder1.setNegativeButton("Yes", dialogClickListener1);
                    builder1.setPositiveButton("No", dialogClickListener1);
                    builder1.show();

                }

                return true;
            }
        });

        switch1_3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    // 버튼을 눌렀을 때
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    // 버튼을 누른 상태로 움직이고 있을 때


                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    // 버튼에서 손을 떼었을 때

                    DialogInterface.OnClickListener dialogClickListener1 = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    //No 버튼을 클릭했을때 처리
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    switch1_3.toggle(); //Yes 버튼을 클릭했을때 처리
                                    if(switch_count3==0){ //스위치 off
                                        switch_count3=1;
                                        try{
                                            mqtt.init("tcp://13.125.54.181:1883");
                                            meg="16";
                                            mqtt.Publish(meg,"item/control");
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        } catch (MqttException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    else if(switch_count3==1){  //스위치 on
                                        switch_count3=0;
                                        try{
                                            mqtt.init("tcp://13.125.54.181:1883");
                                            meg="15";
                                            mqtt.Publish(meg,"item/control");
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        } catch (MqttException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    break;
                            }
                        }
                    };

                    final AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                    builder1.setMessage("Are you sure?");
                    builder1.setNegativeButton("Yes", dialogClickListener1);
                    builder1.setPositiveButton("No", dialogClickListener1);
                    builder1.show();

                }

                return true;
            }
        });

        switch1_4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    // 버튼을 눌렀을 때
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    // 버튼을 누른 상태로 움직이고 있을 때

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    // 버튼에서 손을 떼었을 때

                    DialogInterface.OnClickListener dialogClickListener1 = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    //No 버튼을 클릭했을때 처리
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    switch1_4.toggle(); //Yes 버튼을 클릭했을때 처리
                                    if(switch_count4==0){ //스위치 off
                                        switch_count4=1;
                                        try{
                                            mqtt.init("tcp://13.125.54.181:1883");
                                            meg="18";
                                            mqtt.Publish(meg,"item/control");
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        } catch (MqttException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    else if(switch_count4==1){  //스위치 on
                                        switch_count4=0;
                                        try{
                                            mqtt.init("tcp://13.125.54.181:1883");
                                            meg="17";
                                            mqtt.Publish(meg,"item/control");
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        } catch (MqttException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    break;
                            }
                        }
                    };

                    final AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                    builder1.setMessage("Are you sure?");
                    builder1.setNegativeButton("Yes", dialogClickListener1);
                    builder1.setPositiveButton("No", dialogClickListener1);
                    builder1.show();

                }

                return true;
            }
        });




        rl2_list1.setOnClickListener(click_listener1);
        rl2_list2.setOnClickListener(click_listener1_2);
        rl2_list3.setOnClickListener(click_listener1_3);
        rl2_list4.setOnClickListener(click_listener1_4);
        back1_1.setOnClickListener(click_listener2);
        back1_2.setOnClickListener(click_listener2_2);
        back1_3.setOnClickListener(click_listener2_3);
        back1_4.setOnClickListener(click_listener2_4);
    }

    public boolean onContextItemSelected(MenuItem item) {
        Log.d("onContextItemSelected","clicked");
        switch (item.getItemId()){
            case R.id.menu1_change:
                Log.d("onContextItemSelected","menu1_change");
                dialogView = (View) View.inflate(Layout_two.this, R.layout.dialog1, null);
                AlertDialog.Builder dlg = new AlertDialog.Builder (Layout_two.this);
                dlg.setTitle("이름 변경");
                dlg.setView(dialogView);
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dlgedt1 =  (EditText) dialogView.findViewById(R.id.dlgedt1);
                        SharedPreferences.Editor editor = pref.edit();// editor에 put 하기
                        switch(tab_selected){
                            case 1:
                                editor.putString("tab01", dlgedt1.getText().toString()); //"tab01"라는 key값으로 id 데이터를 저장한다.
                                number1.setText(dlgedt1.getText().toString());
                                editor.commit();
                                break;
                            case 2:
                                editor.putString("tab02", dlgedt1.getText().toString());
                                number2.setText(dlgedt1.getText().toString());
                                editor.commit();
                                break;
                            case 3:
                                editor.putString("tab03", dlgedt1.getText().toString());
                                number3.setText(dlgedt1.getText().toString());
                                editor.commit();
                                break;
                            case 4:
                                editor.putString("tab04", dlgedt1.getText().toString());
                                number4.setText(dlgedt1.getText().toString());
                                editor.commit();
                                break;
                        }
                    }
                });
                dlg.setNegativeButton("취소", null);
                dlg.show();
                break;
            case R.id.delete1:
                SharedPreferences.Editor editor = pref.edit();// editor에 put 하기
                switch(tab_selected){
                    case 1:
                        rl2_list1.setVisibility(View.GONE);
                        editor.putInt("tab_del01",1); //key값으로 id 데이터를 저장한다.
                        break;
                    case 2:
                        rl2_list2.setVisibility(View.GONE);
                        editor.putInt("tab_del02",1);
                        break;
                    case 3:
                        rl2_list3.setVisibility(View.GONE);
                        editor.putInt("tab_del03",1);
                        break;
                    case 4:
                        rl2_list4.setVisibility(View.GONE);
                        editor.putInt("tab_del04",1);
                        break;
                }
                editor.commit(); //완료한다.
                break;
        }
        return super.onContextItemSelected(item);
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        Log.d("onCreateContextMenu","clicked");
        MenuInflater mInflater = getMenuInflater();
        if(v == rl2_list1){
            Log.d("onCreateContextMenu","01");
            //menu.setHeaderTitle("changed");
            tab_selected = 1;
            mInflater.inflate(R.menu.menu, menu);
        }
        if(v == rl2_list2){
            Log.d("onCreateContextMenu","02");
            //menu.setHeaderTitle("changed");
            tab_selected = 2;
            mInflater.inflate(R.menu.menu, menu);
        }
        if(v == rl2_list3){
            Log.d("onCreateContextMenu","03");
            //menu.setHeaderTitle("changed");
            tab_selected = 3;
            mInflater.inflate(R.menu.menu, menu);
        }
        if(v == rl2_list4){
            Log.d("onCreateContextMenu","04");
            //menu.setHeaderTitle("changed");
            tab_selected = 4;
            mInflater.inflate(R.menu.menu, menu);
        }
    }





    public void printToast(String messageToast) {
        Toast.makeText(this, messageToast, Toast.LENGTH_LONG).show();
    }

    public static String nameString(int ip2)
    {
        int dd = (ip2 >> 24) & 0xFF;
        return Integer.toString(dd);
    }




}
