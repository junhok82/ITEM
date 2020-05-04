package com.example.choi7.item_app;

/**
 * 폰트설정
 */

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class font extends AppCompatTextView {
    public font(Context context) {
        super(context,null);
        applyTypeface(context);
    }

    public font(Context context,AttributeSet attrs){
        super(context, attrs, android.R.attr.textViewStyle);
        applyTypeface(context);
    }

    public font(Context context,AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        applyTypeface(context);
    }

    private void applyTypeface(Context context){
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),"the_oegyeinseolmyeongseo.ttf");
        setTypeface(typeface);
    }
}
