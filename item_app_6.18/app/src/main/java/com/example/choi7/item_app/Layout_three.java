package com.example.choi7.item_app;
/**
 * 전력 예측
 */
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.graphics.Color.rgb;
import static com.example.choi7.item_app.noti_asynctask.entries;
import static com.example.choi7.item_app.noti_asynctask.entries2;


public class Layout_three extends AppCompatActivity {

    TableLayout layout_3_table;
    LinearLayout layout_3_linear1;

    LinearLayout layout_3_total_Linear;
    static LinearLayout pred_result_layout;  //예측 결과 레이아웃

    static RelativeLayout lay3_rel1; //예측 아이콘 레이아웃
    ImageView predict_icon;
    //static ImageView pred_image;  //예측 결과 이미지뷰
    TextView product_use_text;

    static Mqtt_communication mqtt=new Mqtt_communication();

    static String wattsetting="0";
    static String paysetting="0";

    static LineChart lineChart;

    ImageView back3_1;
    ImageView gone_iv3;

    ImageView ivt3_1;
    ImageView ivt3_2;
    ImageView ivt3_4;

    SharedPreferences pref;

    static int predict_count=0; //예측중인지 아닌지 판단하기 위한 변수
    static int predicted_num=0;

    noti_asynctask noti;
    noti_asynctask noti_graph;
    TextView pre_tv;
    TextView predicting_text;
    TextView re_prediction;


    Typeface typeface;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_three);

        layout_3_total_Linear=(LinearLayout)findViewById(R.id.layout_three_total_Linear) ;
        layout_3_table=(TableLayout)findViewById(R.id.layout_three_table);
        layout_3_linear1=(LinearLayout)findViewById(R.id.layout_three_linear1);
        pred_result_layout=(LinearLayout)findViewById(R.id.pred_result_layout);

        ivt3_1 = (ImageView) findViewById(R.id.ivt3_1);
        ivt3_2 = (ImageView) findViewById(R.id.ivt3_2);
        ivt3_4 = (ImageView) findViewById(R.id.ivt3_4);


        lineChart = (LineChart)findViewById(R.id.pred_image);

        predict_icon=(ImageView)findViewById(R.id.predict_icon);
        pre_tv=(TextView)findViewById(R.id.pre_tv);
        predicting_text=(TextView)findViewById(R.id.predicting_text);
        product_use_text=(TextView)findViewById(R.id.product_use_text);
        re_prediction=(TextView)findViewById(R.id.re_prediction);

        lay3_rel1=(RelativeLayout) findViewById(R.id.lay3_rel1);
        gone_iv3 = (ImageView)findViewById(R.id.ivt3_3);

        noti_graph=new noti_asynctask(this.getApplicationContext());

        Animation animation1 = AnimationUtils.loadAnimation(Layout_three.this, R.anim.scale_anim);
        animation1.setFillAfter(true);
        layout_3_total_Linear.startAnimation(animation1);
        layout_3_total_Linear.setAnimation(animation1);

        Handler delayHandler = new Handler();
        delayHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation animation = new AlphaAnimation(0, 1);
                animation.setDuration(780);
                Animation animation2 = AnimationUtils.loadAnimation(Layout_three.this, R.anim.scale_anim2);
                animation2.setFillAfter(true);

                layout_3_table.setVisibility(View.VISIBLE);
                layout_3_linear1.setVisibility(View.VISIBLE);

                layout_3_linear1.setAnimation(animation);
                layout_3_linear1.setAnimation(animation2);
            }

        }, 850);            // 딜레이


        View.OnClickListener click_listener_iv = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.ivt3_1:
                        Intent im_it1 = new Intent(Layout_three.this, Layout_one.class);
                        startActivity(im_it1);
                        finish();
                        break;
                    case R.id.ivt3_2:
                        Intent im_it2 = new Intent(Layout_three.this, Layout_two.class);
                        startActivity(im_it2);
                        finish();
                        break;
                    case R.id.ivt3_4:
                        Intent im_it4 = new Intent(Layout_three.this, Layout_four.class);
                        startActivity(im_it4);
                        finish();
                        break;
                }
            }
        };          // [메뉴]다른 메뉴로 넘어가기
        ivt3_1.setOnClickListener(click_listener_iv);
        ivt3_2.setOnClickListener(click_listener_iv);
        ivt3_4.setOnClickListener(click_listener_iv);

        //예측 버튼이 눌렸을 때 (예측 중이라는 메세지가 계속 뜸)
        if(predict_count==1){
            predict_icon.clearAnimation();
            predict_icon.setVisibility(View.GONE);
            pre_tv.setVisibility(View.GONE);
            predicting_text.setVisibility(View.VISIBLE);
        }
        //그래프가 그려졌을 때 (계속 그래프가 그려져 있음)
        else if(predict_count==2){
            lay3_rel1.setVisibility(View.GONE);
            pred_result_layout.setVisibility(View.VISIBLE);
            /*
            noti=new noti_asynctask(getApplicationContext());
            noti.setChart();
            */
            setChart();

        }


        //프로그레스 다이얼 로그
        final ProgressDialog asyncDialog = new ProgressDialog(Layout_three.this);
        asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        asyncDialog.setTitle("데이터분석 안내");
        asyncDialog.setMessage("데이터 분석 중입니다..."+"\n"+"(약 2~3분의 시간이 소요됩니다.)");
        asyncDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "종료",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        asyncDialog.dismiss();
                    }
                });


        //아이콘 깜빡거림 애니메이션 효과
        final Animation startAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink_anim);
        predict_icon.startAnimation(startAnimation);

        //글씨체 변환
        typeface = ResourcesCompat.getFont(this,R.font.the_oegyeinseolmyeongseo);
        pre_tv.setTypeface(typeface);
        predicting_text.setTypeface(typeface);

        product_use_text.setText("제품별"+"\n"+"이용량"+"\n"+"패턴분석 >");
        product_use_text.setTypeface(typeface);

        re_prediction.setText("< 예측 다시하기");
        re_prediction.setTypeface(typeface);








        //예측버튼 클릭시
        predict_icon.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View view) {
                noti=new noti_asynctask(getApplicationContext());
                noti.initChart();
                noti.execute();
                asyncDialog.show();

                predict_icon.clearAnimation();
                predict_icon.setVisibility(View.GONE);
                pre_tv.setVisibility(View.GONE);

                predicting_text.setVisibility(View.VISIBLE);
                predicting_text.startAnimation(startAnimation);
                predict_count=1;
                //noti_graph=noti;

            }
        });

        re_prediction.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View view) {

                //noti.cancel(true);
                //noti_graph.cancel(true);


                predicting_text.clearAnimation();
                predict_icon.startAnimation(startAnimation);

                predict_icon.setVisibility(View.VISIBLE);
                pre_tv.setVisibility(View.VISIBLE);
                predicting_text.setVisibility(View.GONE);
                lay3_rel1.setVisibility(View.VISIBLE);
                pred_result_layout.setVisibility(View.GONE);
                predict_count=0;
            }
        });


    }
    //그래프 그리기
    void setChart(){

        //lineChart.clear();

        Typeface typeface = ResourcesCompat.getFont(getApplicationContext(),R.font.the_oegyeinseolmyeongseo);
        LineDataSet lineDataSet = new LineDataSet(entries, "실제 사용량");
        LineDataSet lineDataSet2 = new LineDataSet(entries2, "예측 사용량");

        //예측 그래프 그리기
        lineDataSet2.setLineWidth(3);    //선 두께
        lineDataSet2.setCircleRadius(3);  //지점 넓이
        lineDataSet2.setCircleHoleRadius(2); //지점 내부 원 넓이
        lineDataSet2.setCircleColor(Color.WHITE); //지점 색
        lineDataSet2.setCircleColorHole(Color.rgb(240,128,128));  //지점 구멍색
        lineDataSet2.setColor(Color.rgb(240,128,128)); //라인 선
        lineDataSet2.setDrawCircleHole(true); //지점 구멍 선택
        lineDataSet2.setDrawCircles(true);    //지점을 동그랗게 함
        lineDataSet2.setDrawHighlightIndicators(true);  //높이 지시선
        lineDataSet2.setDrawValues(false);   //데이터 값 보이기
        lineDataSet2.setValueTextSize(10);

        lineDataSet2.setValueTypeface(typeface);
        lineDataSet2.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);

        //예측그래프 밑부분 채우기
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.prediction_chart_fill);
        drawable.setAlpha(180);
        lineDataSet2.setFillDrawable(drawable);
        lineDataSet2.setDrawFilled(true);


        //실제그래프 그리기
        lineDataSet.setLineWidth(3);    //선 두께
        lineDataSet.setCircleRadius(2);  //지점 넓이
        lineDataSet.setCircleHoleRadius(1); //지점 내부 원 넓이
        lineDataSet.setCircleColor(Color.WHITE); //지점 색
        lineDataSet.setCircleColorHole(Color.rgb(131,237,255));  //지점 구멍색
        lineDataSet.setColor(Color.rgb(131,237,255)); //라인 선
        lineDataSet.setDrawCircleHole(true); //지점 구멍 선택
        lineDataSet.setDrawCircles(true);    //지점을 동그랗게 함
        lineDataSet.setDrawHighlightIndicators(true);  //높이 지시선
        lineDataSet.setDrawValues(false);   //데이터 값 보이기
        lineDataSet.setValueTextSize(10);
        lineDataSet.setValueTypeface(typeface);

        //실제그래프 밑부분 채우기
        Drawable drawable2 = ContextCompat.getDrawable(getApplicationContext(),R.drawable.actual_chart_fill);
        drawable.setAlpha(180);
        lineDataSet.setFillDrawable(drawable2);
        lineDataSet.setDrawFilled(true);

        //그래프 그리기
        LineData lineData = new LineData();
        lineData.addDataSet(lineDataSet);
        lineData.addDataSet(lineDataSet2);
        lineChart.setData(lineData);

        //x축 설정
        XAxis xAxis = lineChart.getXAxis(); // x 축 설정
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //x 축 표시에 대한 위치 설정

        xAxis.setTextColor(Color.BLACK); // X축 텍스트컬러설정
        xAxis.setGridColor(Color.BLACK); // X축 줄의 컬러 설정
        xAxis.setTextSize(11);
        xAxis.setLabelRotationAngle(30);
        xAxis.setTypeface(typeface);
        xAxis.setGranularity(24);
        //x축의 형식을 변환함 (월/일 시간 순으로)
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                Log.d("value",String.valueOf(value));
                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd HH시  ", Locale.KOREA);

                Date date = new Date ( (long)value );

                return formatter.format(date);
            }
        });


        //y축 설정
        YAxis yLAxis = lineChart.getAxisLeft();
        yLAxis.setTextColor(Color.BLACK);
        yLAxis.setTextSize(10);
        yLAxis.setTypeface(typeface);
        //y축 형식을 변환함
        yLAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                Log.d("value",String.valueOf(value));

                return String.valueOf((int)value+"Kh");
            }
        });

        //그래프의 마커를 찍음
        MyMarkerView marker = new MyMarkerView(getApplicationContext(),R.layout.markerview);
        marker.setChartView(lineChart);
        lineChart.setMarker(marker);

        //y축 오른쪽을 그림
        YAxis yRAxis = lineChart.getAxisRight();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(true);
        yRAxis.setDrawGridLines(false);

        Description description = new Description();
        description.setText("");


        Log.d("linechart","그려짐");
        lineChart.setDoubleTapToZoomEnabled(true);
        lineChart.setDrawGridBackground(false);
        lineChart.setDescription(description);
       // lineChart.animateXY(500,500);
        Legend legend = lineChart.getLegend(); //레전드 설정 (차트 밑에 색과 라벨을 나타내는 설정)
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);//하단 왼쪽에 설정
        legend.setTextSize(11); // 레전드 컬러 설정
        legend.setFormSize(12);
        lineChart.invalidate();

    }


}
