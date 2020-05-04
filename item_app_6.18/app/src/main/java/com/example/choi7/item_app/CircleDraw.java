package com.example.choi7.item_app;
/**
 원 그래프 그려주는 클래스 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import static android.graphics.Color.rgb;
import static com.example.choi7.item_app.Layout_load.total_use_watt;
import static com.example.choi7.item_app.Layout_three.wattsetting;

public class CircleDraw extends View {



    public CircleDraw(Context context) {
        super(context);
    }
    public CircleDraw(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    float angle ;
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

       float width=this.getWidth();    //그래프의 위치를 정해주기 위해 너비를 구한다.
        float height=this.getHeight();  //그래프의 위치를 정해주기 위해 높이를 구한다.

        float display_use_watt;


        float left=width/2/2;           //왼쪽좌표를 너비의 1/4로 해주었다.
        float top=height/2/2;           //위쪽의 좌표를 높이의 1/4로 해주었다.

        //drawArc를 이용하면 오른쪽이 0도가 된다. 난 가장 윗부분을 0도로 보았기 때문에 -90도를 해주었다.

        final float START_POINT = -90f;

        float goal_total_watt=0;
        float total_watt=0;
        float ANGLE_PER_SCORE;
        String circle_total_use_watt=" ";
        //이 그래프에서 100을 최대값을로 정해주어 360도를 100으로 나누었다.
        // 만약 5등분을 하고싶다면 360/5를 하면 된다.
        goal_total_watt=Float.parseFloat(wattsetting);
        if(total_use_watt!=null){
        total_watt=Float.parseFloat(total_use_watt);
        circle_total_use_watt=total_use_watt;
        }
        if(goal_total_watt!=0) {
            ANGLE_PER_SCORE = goal_total_watt / 360;
            angle = total_watt/ANGLE_PER_SCORE;
        }
        else{
            ANGLE_PER_SCORE=0;
            angle=0;
        }
        //사용한 누적전력량에 따른 그래프의 각도 (누적 전력량 * 1Watt 당 각도)


        //사각형 객체 RectF를 생성, 원형 그래프의 크기를 사각형이라 보고 좌,상,우,하 좌표 설정, 좌상이 기준이 된다.
        // RectF rectF = new RectF(225,160,505,440);
        RectF rectF = new RectF(left+10,top,left+width/2-10,top+height/2);
        //페인트 객체 생성
        Paint p = new Paint();
        //페인트 객체 설정
        p.setAntiAlias(true);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(100);  //페인트의 넓이
        p.setAlpha(0x00); //투명도 설정
        p.setColor(rgb(255,255,204));
        //원형 그래프의 바탕 그래프 부분 설정
        canvas.drawArc(rectF, -90 ,-360 , false, p);
        //페인트 객체 설정
        p.setColor(rgb(255,255,11));
        //그래프 형태를 둥근형으로 설정
        p.setStrokeCap(Paint.Cap.ROUND);
        //원형 그래프의 노란 부분 설정
        canvas.drawArc(rectF, START_POINT, angle, false, p);

        //페인트 객체를 다시 설정한 다음 캔버스에 글씨를 쓴다.
        p.reset();
        p.setColor(rgb(71,87,109));


        //캔버스에 그릴 글씨의 길이에 맞게 글씨 크기 및 위치를 조정
        p.setTextSize(width/16);
        //canvas.drawText("누적 전력량",width/4+width/11+width/128, height/2-height/16, p);

        p.setColor(rgb(255,0,0));
        p.setTextSize(width/15);
        canvas.drawText(circle_total_use_watt, width/4+width/9+width/30, height/2+height/16, p);

        p.setColor(rgb(71,87,109));
        p.setTextSize(width/15);
        canvas.drawText("KW",width/2+width/16+width/64,height/2+height/16, p);


    }

}