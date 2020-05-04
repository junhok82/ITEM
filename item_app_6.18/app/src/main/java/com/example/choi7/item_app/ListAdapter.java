package com.example.choi7.item_app;

import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * 리스트 뷰 어댑터
 */

public class ListAdapter extends BaseAdapter
{
    LayoutInflater inflater = null;
    private ArrayList<ID_item> m_oData = null;
    private int nListCnt = 0;

    public ListAdapter(ArrayList<ID_item> _oData) {
        m_oData = _oData;
        nListCnt = m_oData.size();
    }




    @Override
    public int getCount()
    {
        Log.i("TAG", "getCount");
        return nListCnt;
    }

    @Override
    public Object getItem(int position)
    {
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent)
    {
        final Context context = parent.getContext();
        if (convertView == null)
        {
            if (inflater == null)
            {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }

        TextView oTextDate = (TextView) convertView.findViewById(R.id.textDate);
        oTextDate.setText(m_oData.get(position).strDate);
        oTextDate.setOnClickListener((View.OnClickListener)context);
        oTextDate.setTag(position);


        /*
        oBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"번째 이미지 선택", Toast.LENGTH_SHORT).show();
            }});*/


        convertView.setTag(""+position);
        return convertView;
    }
}