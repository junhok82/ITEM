package com.example.choi7.item_app;

/**
 * 이용량 조회
 */
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.applikeysolutions.cosmocalendar.selection.MultipleSelectionManager;
import com.applikeysolutions.cosmocalendar.selection.criteria.BaseCriteria;
import com.applikeysolutions.cosmocalendar.selection.criteria.WeekDayCriteria;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;


import static com.example.choi7.item_app.Layout_load.total_use_watt;
import static com.example.choi7.item_app.Layout_load.day_use_watt;
import static com.example.choi7.item_app.Layout_load.now_use_watt;
import static com.example.choi7.item_app.Mqtt_communication.sub;

import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;


public class Layout_one extends AppCompatActivity implements View.OnClickListener{

    LinearLayout layout_one_linear1;
    LinearLayout window_layout;
    TableLayout layout_one_table;
    ImageView back1;
    ImageView back2;
    ImageView back3;
    ImageView ivt1_2;
    ImageView ivt1_3;
    ImageView ivt1_4;
    ImageView gone_iv1;
    ImageView calendar_icon;

    /***** category *****/
    LinearLayout category_main_layout;
    LinearLayout category_layout_1;
    LinearLayout category_layout_2;
    LinearLayout category_layout_3;

    LinearLayout id_category_1;
    LinearLayout id_category_2;
    LinearLayout id_category_3;
    /*****  category *****/

    /***** chart *****/
    private CombinedChart chart;
    static int count;
    Button btn_date;
    /***** chart *****/

    TextView now_use_watttext;
    TextView day_use_watttext;
    LinearLayout linear_pay;
    static int c = 0;

    private int port = 9000;
    private final String ip = "15.164.14.98";


    SpannableStringBuilder now_use_watttext_builder;
    SpannableStringBuilder day_use_watttext_builder;

    static NotificationManager notificationManager;
    PendingIntent intent;


    /****************목표치 설정*******************/
    CircleDisplay mCircleDisplay;
    TextView circleDisplay_text;
    Button goaltext;
    String leave_goal_watt_text;
    int leave_goal_watt=0;
    SharedPreferences goal_watt;


    //static String meg;
    static Mqtt_communication mqtt = new Mqtt_communication();
    Mqtt_communication mqtt2 = new Mqtt_communication();

    static StringBuilder selected_First_day = null;
    static StringBuilder selected_Last_day = null;
    private String sum_fee_Last_day = null;
    private String sum_fee_First_day = null;


    //폰트 바꿔주기
    Typeface typeface;
    Typeface the_o;
    TextView te;
    TextView te2;
    TextView te3;
    Handler mMainHandler;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_one);

        layout_one_linear1 = (LinearLayout) findViewById(R.id.layout_one_linear1);
        window_layout = (LinearLayout) findViewById(R.id.window_layout);
        layout_one_table = (TableLayout) findViewById(R.id.layout_one_table);

        category_main_layout = (LinearLayout) findViewById(R.id.category_main_layout);
        category_layout_1 = (LinearLayout) findViewById(R.id.category_layout_1);
        category_layout_2 = (LinearLayout) findViewById(R.id.category_layout_2);
        category_layout_3 = (LinearLayout) findViewById(R.id.category_layout_3);

        id_category_1 = (LinearLayout) findViewById(R.id.id_category_1);
        id_category_2 = (LinearLayout) findViewById(R.id.id_category_2);
        id_category_3 = (LinearLayout) findViewById(R.id.id_category_3);

        back1 = (ImageView) findViewById(R.id.back1);
        back2 = (ImageView) findViewById(R.id.back2);
        back3 = (ImageView) findViewById(R.id.back3);

        ivt1_2 = (ImageView) findViewById(R.id.ivt1_2);
        ivt1_3 = (ImageView) findViewById(R.id.ivt1_3);
        ivt1_4 = (ImageView) findViewById(R.id.ivt1_4);
        gone_iv1 = (ImageView) findViewById(R.id.ivt1_1);
        calendar_icon = (ImageView) findViewById(R.id.calendar_icon) ;

        now_use_watttext = (TextView) findViewById(R.id.now_use_watttext);
        day_use_watttext = (TextView) findViewById(R.id.day_use_watttext);

        linear_pay = (LinearLayout) findViewById(R.id.linear_pay);

        circleDisplay_text=(TextView)findViewById(R.id.circleDisplay_text);
        goaltext=(Button)findViewById(R.id.goaltext);
        goal_watt= getSharedPreferences("goal_watt",MODE_PRIVATE);

        te = (TextView) findViewById(R.id.te);
        te2 = (TextView) findViewById(R.id.te2);
        te3 = (TextView) findViewById(R.id.te3);

        the_o = Typeface.createFromAsset(getAssets(), "the_oegyeinseolmyeongseo.ttf");
        te.setTypeface(the_o);
        te2.setTypeface(the_o);
        te3.setTypeface(the_o);

        intent = PendingIntent.getActivity(this, 0,
                new Intent(getApplicationContext(), Layout_one.class), PendingIntent.FLAG_UPDATE_CURRENT);

        Animation animation1 = AnimationUtils.loadAnimation(Layout_one.this, R.anim.scale_anim);
        animation1.setFillAfter(true);
        window_layout.startAnimation(animation1);
        window_layout.setAnimation(animation1);

        Handler delayHandler = new Handler();
        delayHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation animation = new AlphaAnimation(0, 1);
                animation.setDuration(780);
                Animation animation2 = AnimationUtils.loadAnimation(Layout_one.this, R.anim.scale_anim2);
                animation2.setFillAfter(true);

                layout_one_table.setVisibility(View.VISIBLE);
                window_layout.setVisibility(View.VISIBLE);

                window_layout.setAnimation(animation);
                window_layout.setAnimation(animation2);
            }

        }, 850);            // 딜레이


        View.OnClickListener click_listener1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.id_category_1:
                        category_main_layout.setVisibility(View.GONE);
                        category_layout_1.setVisibility(View.VISIBLE);
                        break;
                    case R.id.id_category_2:
                        category_main_layout.setVisibility(View.GONE);
                        category_layout_2.setVisibility(View.VISIBLE);
                        break;
                    case R.id.id_category_3:
                        category_main_layout.setVisibility(View.GONE);
                        category_layout_3.setVisibility(View.VISIBLE);
                        break;
                }
            }
        };          // 3가지 카테고리 선택 화면
        id_category_1.setOnClickListener(click_listener1);
        id_category_2.setOnClickListener(click_listener1);
        id_category_3.setOnClickListener(click_listener1);


        View.OnClickListener click_listener_iv = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.ivt1_2:
                        Intent im_it2 = new Intent(Layout_one.this, Layout_two.class);
                        startActivity(im_it2);
                        finish();
                        break;
                    case R.id.ivt1_3:
                        Intent im_it3 = new Intent(Layout_one.this, Layout_three.class);
                        startActivity(im_it3);
                        finish();
                        break;
                    case R.id.ivt1_4:
                        Intent im_it4 = new Intent(Layout_one.this, Layout_four.class);
                        startActivity(im_it4);
                        finish();
                        break;
                }
            }
        };          // [메뉴]다른 메뉴로 넘어가기
        ivt1_2.setOnClickListener(click_listener_iv);
        ivt1_3.setOnClickListener(click_listener_iv);
        ivt1_4.setOnClickListener(click_listener_iv);

        c = 1;


        View.OnClickListener back_click_listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.back1:
                        category_layout_1.setVisibility(View.GONE);
                        category_main_layout.setVisibility(View.VISIBLE);
                        break;

                    case R.id.back2:
                        category_layout_2.setVisibility(View.GONE);
                        category_main_layout.setVisibility(View.VISIBLE);
                        break;

                    case R.id.back3:
                        category_layout_3.setVisibility(View.GONE);
                        category_main_layout.setVisibility(View.VISIBLE);
                        break;

                }
            }
        };          // back -> 3가지 카테고리 선택 화면
        back1.setOnClickListener(back_click_listener);
        back2.setOnClickListener(back_click_listener);
        back3.setOnClickListener(back_click_listener);


        //전력 측정 결과 출력 부분 (mqtt)
        try {
            do
                mqtt.init("tcp://13.125.54.181:1883");
            while (false);

            for(int i=0;i<2;i++) {
                mqtt.Publish("msg1", "item/watt/receive");
                total_use_watt = mqtt.Subscribe(0, "item/watt/total_use_watt");
            }
            for(int i=0;i<2;i++) {
                mqtt.Publish("msg2", "item/watt/receive");
                day_use_watt = mqtt.Subscribe(0, "item/watt/day_use_watt");
            }
            for(int i=0;i<2;i++) {
                mqtt.Publish("msg3", "item/watt/receive");
                now_use_watt = mqtt.Subscribe(0, "item/watt/now_use_watt");
            }
            mqtt.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
     }
        now_use_watttext.setText(now_use_watt);
        day_use_watttext.setText(day_use_watt);


        typeface = ResourcesCompat.getFont(this, R.font.the_oegyeinseolmyeongseo); // 글씨체 선택을 위한 변수


        //현재 전력 사용량의 W를 표시해주기 위한 객체(색상과 글씨체를 고름)
        now_use_watttext_builder = new SpannableStringBuilder("W");
        now_use_watttext_builder.setSpan(new ForegroundColorSpan(Color.parseColor("#47576D")), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //현재 전력 사용량을 출력
        now_use_watttext.append(" " + now_use_watttext_builder);
        now_use_watttext.setGravity(Gravity.CENTER);
        now_use_watttext.setTextSize(27);
        now_use_watttext.setTypeface(typeface);


        //일일 전력 사용량의 kw/h를 표시해주기 위한 객체(색상과 글씨체를 고름)
        day_use_watttext_builder = new SpannableStringBuilder("kW/h");
        day_use_watttext_builder.setSpan(new ForegroundColorSpan(Color.parseColor("#47576D")), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //일일 전력 사용량을 출력
        day_use_watttext.append(" " + now_use_watttext_builder);
        day_use_watttext.setGravity(Gravity.CENTER);
        day_use_watttext.setTextSize(27);
        day_use_watttext.setTypeface(typeface);



        //아이콘 깜빡거림 애니메이션 효과
        final Animation startAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink_anim);
        calendar_icon.startAnimation(startAnimation);



        btn_date=(Button)findViewById(R.id.btn_date);
        btn_date.setOnClickListener(this);

        //글씨체 변환
        typeface = ResourcesCompat.getFont(this,R.font.the_oegyeinseolmyeongseo);
        btn_date.setTypeface(typeface);
//----------------------------------목표치 설정 부분---------------------------------
        float tmp_watt=0;

        if(goal_watt.getInt("goal_watt",0)!=0){
            leave_goal_watt=goal_watt.getInt("goal_watt",0)-(int)Float.parseFloat(total_use_watt); // 이부분에서 totalwatt가 서버로 부터 받아온 전체 전력값임
           // leave_goal_watt=goal_watt.getInt("goal_watt",0)-0;
            tmp_watt=goal_watt.getInt("goal_watt",0);
        }



         if(leave_goal_watt<0)
            leave_goal_watt=0;

        leave_goal_watt_text=String.valueOf(leave_goal_watt);
        circleDisplay_text.setTypeface(typeface);
        circleDisplay_text.setText("목표까지 앞으로"+leave_goal_watt_text+"Kh");




        mCircleDisplay=(CircleDisplay) findViewById(R.id.circleDisplay);
        //원 게이지 설정
        mCircleDisplay.setColor(Color.rgb(255,246,143)); //원 그래프 색상 선택
        mCircleDisplay.setVisibility(View.VISIBLE);
        mCircleDisplay.setAnimDuration(4000);   //퍼센트가 올라가는 애니메이션 속도 설정
        mCircleDisplay.setValueWidthPercent(55);       //그래프 넓이 설정
        mCircleDisplay.setFormatDigits(2);      //퍼센트가 올라가는 형식 설정
        mCircleDisplay.setDimAlpha(80);         //그래프 투명도 배경

        //mCircleDisplay.setSelectionListener(this);  //이건 뭔지 모르겟다
        //mCircleDisplay.setTouchEnabled(true);   //그래프에 터치를 하면 터치한 곳 까지 퍼센트가 변함
        mCircleDisplay.setUnit("Kh");            //중앙에 %텍스트 설정
        mCircleDisplay.setStepSize(5);
        mCircleDisplay.showValue(Float.parseFloat(total_use_watt),tmp_watt, true);
                /*게이지의 범위를 설정함 첫번 째는 얼마큼 게이지가 찰건지, 두번째는 전체 게이지의 양, 세 번째는 애니메이션 실행 여부
                  만약에 하게 된다면 첫번 째에는 현재 실행된 전력값, 두번 째에는 목표한 값을 넣어야 될 듯
                  근데 여기서 만약에 목표치를 초과하게 되었을때 어떻게 문장으로 표현해 줄 것인지 결정해야함
                */
        goaltext.setTypeface(typeface);
        //예측버튼 클릭시
        goaltext.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View view) {
                show();
            }
        });


    }//oncreate

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_date:
                CalendarDialog dialog = new CalendarDialog(this);
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    sum_fee_Last_day = init_mqtt("date " + selected_Last_day.toString(), "item/sub", 1);
                    sum_fee_First_day = init_mqtt("date " + selected_First_day.toString(), "item/sub", 1);
                    init_chart();
                    Log.e("첫날 요금 :", sum_fee_First_day);
                    Log.e("마지막날 요금 :", sum_fee_Last_day);
                }
            });
                dialog.show();
                break;
        }
    }

    private String init_mqtt(String msg, String topic, int check)
    {
        String temp = null;
        try {

                mqtt2.init("tcp://13.125.54.181:1883");
                for(int i=0;i<2;i++) {
                    mqtt2.Publish(msg, "item/pub");
                    if (check == 1)
                        temp = mqtt2.Subscribe(2, "item/sub");

                }
            mqtt2.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return temp;
    }

    private void init_chart()
    {

        the_o = Typeface.createFromAsset(getAssets(), "the_oegyeinseolmyeongseo.ttf");
        /****** 그래프 *****/
        chart = findViewById(R.id.chart1);
        chart.getDescription().setEnabled(false);
        chart.setBackgroundColor(Color.TRANSPARENT);
        chart.setDrawGridBackground(false);
        chart.setDrawBarShadow(false);
        chart.setHighlightFullBarEnabled(false);
        calendar_icon.clearAnimation();
        calendar_icon.setVisibility(View.GONE);
        chart.setVisibility(View.VISIBLE);
        

        // draw bars behind lines
        chart.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.LINE,
        });


        Legend l = chart.getLegend();
        l.setFormSize(15);
        l.setTextSize(11);
        l.setTextColor(Color.BLACK);
        l.setTypeface(the_o);
        l.setWordWrapEnabled(true);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);

        YAxis lefttAxis = chart.getAxisLeft();
        lefttAxis.setTextColor(Color.BLACK);
        lefttAxis.setTextSize(12);
        lefttAxis.setTypeface(the_o);
        lefttAxis.setDrawGridLines(false);
        lefttAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rigthtAxix=chart.getAxisRight();
        rigthtAxix.setDrawAxisLine(false);
        rigthtAxix.setDrawLabels(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setTextColor(Color.BLACK);
        xAxis.setTypeface(the_o);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(12);
        //xAxis.setAxisMinimum(0f);
        //xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                String[] date_split = selected_Last_day.toString().split("-");

                return date_split[1] + "-" + date_split[2];
            }
        });

        CombinedData data = new CombinedData();

        data.setData(generateLineData());
        data.setData(generateBarData());
        data.setValueTypeface(the_o);

        xAxis.setAxisMaximum(data.getXMax() + 0.7f);

        chart.setData(data);
        chart.invalidate();
        chart.setTouchEnabled(true);                                    //그래프를 터치할 수 없게 false로 설정
        chart.setDragEnabled(true);
        /****** 그래프 *****/

    }

    private BarData generateBarData() {

        ArrayList<BarEntry> entries1 = new ArrayList<>();

        for (int index = 0; index < count; index++) {
            entries1.add(new BarEntry(1, Float.parseFloat((sum_fee_First_day))));

        }

        BarDataSet set1 = new BarDataSet(entries1, "누적 요금");
        set1.setColors(Color.BLACK);
        set1.setValueTextColor(Color.BLACK);
        set1.setValueTextSize(10);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);

        BarData d = new BarData(set1);
        d.setBarWidth(0.4f);  //바 그래프 넓이


        return d;
    }

    private LineData generateLineData() {

        LineData d = new LineData();

        ArrayList<Entry> entries = new ArrayList<>();

        for (int index = 0; index < count; index++)
            entries.add(new Entry(index + 0.5f, getRandom(15, 5)));

        LineDataSet set = new LineDataSet(entries, "평균");
        set.setColor(Color.rgb(255, 255, 255));
        set.setLineWidth(2.5f);
        set.setCircleColor(Color.rgb(255, 255, 255));
        set.setCircleRadius(5f);
        set.setFillColor(Color.rgb(255, 255, 255));
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(true);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.rgb(255, 255, 255));

        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set);

        return d;
    }


    float getRandom(float range, float start) {
        return (float) (Math.random() * range) + start;
    }


    //목표치 입력창 (다이얼로그)
    public void show()
    {
        final EditText edittext = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("목표치 입력 창");
        builder.setMessage("목표로 하는 전력 사용량을 입력해주세요.");
        builder.setView(edittext);
        builder.setPositiveButton("입력",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = goal_watt.edit();
                        String input=edittext.getText().toString();
                        try {
                            mqtt.init("tcp://13.125.54.181:1883");
                            mqtt.Publish("pushy " + input, "item/pub");
                            mqtt.disconnect();
                        } catch (MqttException e) {
                            e.printStackTrace();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        editor.putInt("goal_watt",Integer.parseInt(input)); // key, value를 이용하여 저장하는 형태
                        editor.commit();
                        dialog.dismiss();

                        leave_goal_watt=goal_watt.getInt("goal_watt",0)-(int)Float.parseFloat(total_use_watt); // 이부분에서 totalwatt가 서버로 부터 받아온 전체 전력값임
                        if(leave_goal_watt<0)
                            leave_goal_watt=0;

                        leave_goal_watt_text=String.valueOf(leave_goal_watt);
                        circleDisplay_text.setTypeface(typeface);
                        circleDisplay_text.setText("목표까지 앞으로"+leave_goal_watt_text+"Kh");

                        //Toast.makeText(getApplicationContext(),String.valueOf(goal_watt.getInt("goal_watt",0)) ,Toast.LENGTH_LONG).show();
                        mCircleDisplay.showValue(Float.parseFloat(total_use_watt),goal_watt.getInt("goal_watt",0), true);
                    }
                });
        builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();

    }




}//Layout_one.class


