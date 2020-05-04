package com.example.choi7.item_app;
/**
 * asynctask 클래스
 */
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BaseDataSet;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.Socket;
import java.security.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static android.content.ContentValues.TAG;

import static com.example.choi7.item_app.Layout_three.lay3_rel1;
//import static com.example.choi7.item_app.Layout_three.pred_image;
import static com.example.choi7.item_app.Layout_three.pred_result_layout;
import static com.example.choi7.item_app.Layout_three.predict_count;

/**
 * Created by choi7 on 2019-05-29.
 */

public class noti_asynctask extends AsyncTask<String, Void, String> {
    private int port = 8000;
    private final String ip = "15.164.14.98";

    NotificationManager notificationManager;
    private NotificationCompat.Builder mNotifyBuilder;
    PendingIntent intent;
    static Socket clientSocket;
    private BufferedReader socketIn;
    private PrintWriter socketOut;
    static Bitmap bitmap;
    String channelId = "channel";
    String channelName = "Channel Name";
    private Context mContext;
    int importance = NotificationManager.IMPORTANCE_HIGH;
    File storage;
    static File tempFile;
    ArrayList <String> tmpList;    //예측 데이터 날짜
    ArrayList <String> tmpList2 ;  //예측 데이터 전력
    ArrayList <String> tmpList3 ;  //실제 데이터 날짜
    ArrayList <String> tmpList4 ;  //실제 데이터 전력

    ArrayList <String> tmpList5 ;  //예측 daytime
    ArrayList <String> tmpList6 ;  //실제 daytime

    String [] daytime;
    static LineChart lineChart=Layout_three.lineChart;
    int arraylist_count=0;
    int arraylist_count2=0;
    static ArrayList<Entry> entries = new ArrayList<>();  //실제 데이터를 담는 배열
    static ArrayList<Entry> entries2=new ArrayList<>();   //예측 데이터를 담는 배열
    Thread mThread;
    // 생성자
    public noti_asynctask(Context context){
        this.mContext = context;  //알림이 출력될 context를 받아옴
        this. mNotifyBuilder = new NotificationCompat.Builder(context,channelId)  //알림 설정
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentTitle("분석결과") // 제목 설정
                .setContentText("분석이 완료되었습니다.") // 내용 설정
                .setTicker("분석완료") // 상태바에 표시될 한줄 출력
                .setAutoCancel(true)
                .setContentIntent(intent);
        this.notificationManager =
                (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        // 스마트폰의 버전이 오레오이상인지 아닌지 판단함
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }
        //알림 클릭시 해당 인텐트로 넘어감
        intent = PendingIntent.getActivity(context, 0,
                new Intent(context, Layout_three.class), PendingIntent.FLAG_UPDATE_CURRENT);
        //내부저장소 캐시 경로를 받아옵니다.
        storage =context.getCacheDir();

        return;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    // 백그라운드에서 통신을 진행함
    @Override
    protected String doInBackground(String... arg0) {

        mThread=new Thread(){
            @Override
            public void run(){
                try{


                    clientSocket = new Socket(ip, port);


                    OutputStream os = clientSocket.getOutputStream();
                    //문자기반 출력스트림
                    OutputStreamWriter writer = new OutputStreamWriter(os);
                    //버퍼처리된 문자기반 출력 스트림
                    BufferedWriter bufferWriter = new BufferedWriter(writer);
                    bufferWriter.write("predict");
                    bufferWriter.flush();

                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH", Locale.KOREA);


                    tmpList = new ArrayList<String>();
                    tmpList2 = new ArrayList<String>();
                    tmpList3= new ArrayList<String>();
                    tmpList4=new ArrayList<String>();
                    tmpList5=new ArrayList<String>();
                    tmpList6=new ArrayList<String>();

                    int recv_count=0;

                    Log.d("recv","recvstart");
                    BufferedReader bis2 = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    String i;

                    while((i= bis2.readLine())!=null){
                        Log.d("i값",i);
                        if(i.equals("done sendingdate,actual")){
                            recv_count++;
                        }
                        else if(i.equals("done sending") || i.equals("done sendin"))
                            break;

                        String [] array=i.split(",");
                        //예측 데이터 판별
                        if( recv_count==0 &&!array[0].equals("date") && !array[1].equals("prediction") ){
                            Log.d("예측 데이터 넣기 ", "실행됨");
                            String[] array2 = array[0].split("-");

                            String array3 = array2[2].substring(0, array2[2].lastIndexOf(":") - 3);
                            String day= array2[1];
                            String date=array3.substring(0,2);

                            String time=array3.substring(3,5);
                            String year=array2[0];
                            Log.d("time",time);

                            String graph_daytime=year+"-"+day+"-"+date;
                            Log.d("날짜",graph_daytime);

                            try {
                                Date FirstDate = format.parse(array[0]);
                                tmpList3.add(String.valueOf(FirstDate.getTime()));
                                Log.d("예측 데이터 계산시간",String.valueOf(FirstDate.getTime()));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            tmpList4.add(array[1]);
                            array = null;
                            arraylist_count2++;

                            Log.d("예측 데이터 날짜", String.valueOf(tmpList3));
                            Log.d("예측 데이터 전력", String.valueOf(tmpList4));
                        }
                        //실제 데이터 판별
                        else if( recv_count==1 &&!array[0].equals("done sendingdate") && !array[1].equals("actual")){

                            String[] array2 = array[0].split("-");

                            String array3 = array2[2].substring(0, array2[2].lastIndexOf(":") - 3);
                            String day= array2[1];
                            String date=array3.substring(0,2);
                            String time=array3.substring(3,5);
                            String year=array2[0];

                            String graph_daytime=year+"-"+day+"-"+date;
                            Log.d("날짜",graph_daytime);

                            try {
                                Date FirstDate = format.parse(array[0]);
                                tmpList.add(String.valueOf(FirstDate.getTime()));
                                Log.d("실제 데이터 계산시간",String.valueOf(FirstDate.getTime()));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            tmpList2.add(array[1]);
                            array = null;
                            arraylist_count++;

                            Log.d("실제데이터 날짜", String.valueOf(tmpList));
                            Log.d("실제데이터 전력", String.valueOf(tmpList2));
                        }
                    }

                }
                catch (MalformedURLException e){
                    e.printStackTrace();
                }catch (IOException e){
                    e.printStackTrace();
                }

                for (int j = 0; j < arraylist_count; j++) {
                    entries.add(new Entry(Long.parseLong(tmpList.get(j)), Integer.parseInt(tmpList2.get(j))));
                    for(int check=0;check<arraylist_count2;check++)
                        if( Long.parseLong(tmpList.get(j)) == Long.parseLong(tmpList3.get(check)) )
                            entries2.add(new Entry(Long.parseLong(tmpList.get(j)), (int)Float.parseFloat(tmpList4.get(check))));
                    Log.d("entries",String.valueOf(entries.get(j)));
                }

            }

        };
        if (isCancelled()==true)
            return null;

        mThread.run();

        return null;
    }

    protected void onCancelled() {

        super.onCancelled();
    }

    //백그라운드 완료시 실행
    @Override
    protected void onPostExecute(String result) {
        notificationManager.notify(1, mNotifyBuilder.build());
        lay3_rel1.setVisibility(View.GONE);
        pred_result_layout.setVisibility(View.VISIBLE);
        setChart();
        predict_count=2;
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    //초기화
    void initChart(){
        lineChart.clear();
        entries.clear();
        entries2.clear();
    }

    //그래프 그리기
    void setChart(){

        lineChart.clear();

        Typeface typeface = ResourcesCompat.getFont(mContext,R.font.the_oegyeinseolmyeongseo);
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
        Drawable drawable = ContextCompat.getDrawable(mContext,R.drawable.prediction_chart_fill);
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
        Drawable drawable2 = ContextCompat.getDrawable(mContext,R.drawable.actual_chart_fill);
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
                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd HH시  ",Locale.KOREA);

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
        MyMarkerView marker = new MyMarkerView(mContext,R.layout.markerview);
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
        lineChart.animateXY(500,500);
        Legend legend = lineChart.getLegend(); //레전드 설정 (차트 밑에 색과 라벨을 나타내는 설정)
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);//하단 왼쪽에 설정
        legend.setTextSize(11); // 레전드 컬러 설정
        legend.setFormSize(12);
        lineChart.invalidate();

    }

}
