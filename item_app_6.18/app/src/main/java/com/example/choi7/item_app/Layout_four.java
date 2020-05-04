package com.example.choi7.item_app;
/*
 wifi 간접 접속 클래스
 */
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

/*----scan app imports----*/
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.net.DhcpInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static android.view.View.GONE;

public class Layout_four extends AppCompatActivity implements View.OnClickListener  {
    List<ScanResult> scanResult;
    WifiManager wifi;
    int scanCount;
    ScanResult result;
    ListView m_oListView = null;
    // private ListAdapter listadapter;
    //listadapter=new ListAdapter(this, mOnClickListener);
    static String family_ssid;
    static String password;
    String family_wifi;

    ArrayList<ID_item> oData = new ArrayList<>();
    String list[]=new String[50];
    ID_item oItem = new ID_item();

    String text;

    ScanResult scResult;

    Button on;
    Button off;
    Button set;
    Button send;

    Button info01_previous, info02_previous, info03_previous;
    Button info05_previous, info06_previous, info07_previous, info08_previous;
    Button info01_next, info02_next, info03_next;
    Button info05_next, info06_next, info07_next, info08_next;

    ImageView ivt4_1;
    ImageView ivt4_2;
    ImageView ivt4_3;

    LinearLayout layout_four_linear1;

    LinearLayout gone_main, scan, info01, info02, info03, info04;
    LinearLayout info05, info06, info07, info08;

    TextView textDate;

    private View header;
    private LayoutInflater inflater;


    View oParentView;
    TextView oTextTitle;
    TextView skip;
    EditText edtPassword;
    View dialogView;

    private ClientThread mClientThread;

    Boolean scanFlag = false, connselFlag = true;
    Boolean scanoffFlag = false;

    Typeface typeface = ResourcesCompat.getFont(this,R.font.the_oegyeinseolmyeongseo);

    String ip ; // IP
    int port = 8000; // PORT번호
    /*
    @Override
    protected void onDestroy() {
        super.onDestroy();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_four);

        on = (Button) findViewById(R.id.switch_on);
        off = (Button) findViewById(R.id.switch_off);
        on.setOnClickListener(this);
        off.setOnClickListener(this);

        wifi = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        skip=(TextView)findViewById(R.id.skip);
        skip.setTypeface(typeface);

        gone_main = (LinearLayout) findViewById(R.id.gone_main);
        info01 = (LinearLayout) findViewById(R.id.info01);
        info02 = (LinearLayout) findViewById(R.id.info02);
        info03 = (LinearLayout) findViewById(R.id.info03);

        info05 = (LinearLayout) findViewById(R.id.info05);
        info06 = (LinearLayout) findViewById(R.id.info06);
        info07 = (LinearLayout) findViewById(R.id.info07);
        info08 = (LinearLayout) findViewById(R.id.info08);
        scan = (LinearLayout) findViewById(R.id.scan);
        //list_view = (LinearLayout) findViewById(R.id.list_view);

        //info01_previous = (Button) findViewById(R.id.info01_previous);
        info02_previous = (Button) findViewById(R.id.info02_previous);
        info03_previous = (Button) findViewById(R.id.info03_previous);

        info05_previous = (Button) findViewById(R.id.info05_previous);
        info06_previous = (Button) findViewById(R.id.info06_previous);
        info07_previous = (Button) findViewById(R.id.info07_previous);
        info08_previous = (Button) findViewById(R.id.info08_previous);

        info01_next = (Button) findViewById(R.id.info01_next);
        info02_next = (Button) findViewById(R.id.info02_next);
        info03_next = (Button) findViewById(R.id.info03_next);

        info05_next = (Button) findViewById(R.id.info05_next);
        info06_next = (Button) findViewById(R.id.info06_next);
        info07_next = (Button) findViewById(R.id.info07_next);
        info08_next = (Button) findViewById(R.id.info08_next);

        LinearLayout scale_lin1 = (LinearLayout) findViewById(R.id.scale_lin2);

        layout_four_linear1=(LinearLayout)findViewById(R.id.gone_lin4_1);
        ivt4_1 = (ImageView) findViewById(R.id.ivt4_1);
        ivt4_2 = (ImageView) findViewById(R.id.ivt4_2);
        ivt4_3 = (ImageView) findViewById(R.id.ivt4_3);

        final TableLayout gone_tb4 = (TableLayout) findViewById(R.id.gone_tb4);

       // final ImageView gone_iv2 = (ImageView) findViewById(R.id.ivt2_2);


        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.listview_item, null);

        textDate = (TextView)view.findViewById(R.id.textDate);

        Animation animation1 = AnimationUtils.loadAnimation(Layout_four.this, R.anim.scale_anim);
        animation1.setFillAfter(true);
        scale_lin1.startAnimation(animation1);
        scale_lin1.startAnimation(animation1);


        Handler delayHandler = new Handler();
        delayHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation animation = new AlphaAnimation(0, 1);
                animation.setDuration(780);
                Animation animation2 = AnimationUtils.loadAnimation(Layout_four.this, R.anim.scale_anim2);
                animation2.setFillAfter(true);

                gone_tb4.setVisibility(View.VISIBLE);



                layout_four_linear1.setVisibility(View.VISIBLE);
                layout_four_linear1.setAnimation(animation);
                layout_four_linear1.setVisibility(View.VISIBLE);
                layout_four_linear1.setAnimation(animation2);
            }
        }, 750);            // 딜레이

        View.OnClickListener click_listener_iv4 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {

                    case R.id.ivt4_1:
                        Intent im_it4_1 = new Intent(Layout_four.this,Layout_one.class);
                        startActivity(im_it4_1);
                        finish();
                        break;
                    case R.id.ivt4_2:
                        Intent im_it4_2 = new Intent(Layout_four.this,Layout_two.class);
                        startActivity(im_it4_2);
                        finish();
                        break;
                    case R.id.ivt4_3:
                        Intent im_it4_3 = new Intent(Layout_four.this,Layout_three.class);
                        startActivity(im_it4_3);
                        finish();
                        break;
                }
            }
        };          // [메뉴]다른 메뉴로 넘어가기
        ivt4_1.setOnClickListener(click_listener_iv4);
        ivt4_2.setOnClickListener(click_listener_iv4);
        ivt4_3.setOnClickListener(click_listener_iv4);

        View.OnClickListener skip_linten = new View.OnClickListener()
        {
            public void onClick(View v)
            {
                scan.setVisibility(View.VISIBLE);
                info01.setVisibility(GONE);
                info02.setVisibility(GONE);
                info03.setVisibility(GONE);
                info05.setVisibility(GONE);
                info06.setVisibility(GONE);
                info07.setVisibility(GONE);
                info08.setVisibility(GONE);
                skip.setVisibility(GONE);
            }
        };
        skip.setOnClickListener( skip_linten);
/*
        //접속한 와이파이의 ip주소를 얻어옴
        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        DhcpInfo dhcpInfo = wm.getDhcpInfo() ;
        int serverIP = dhcpInfo.gateway;
        String ipAddress = String.format(
                "%d.%d.%d.%d",
                (serverIP & 0xff),
                (serverIP >> 8 & 0xff),
                (serverIP >> 16 & 0xff),
                (serverIP >> 24 & 0xff));
        scane_info.setText("접속중인 ip : " + ipAddress);


        mClientThread = new ClientThread(ipAddress, mMainHandler);
        mClientThread.start();
*/

        /*
        // 현재 접속되어 있는 AP 정보 추출
        WifiInfo wifiInfo = wifi.getConnectionInfo();
        scane_info.setText("접속중인 AP : " + wifiInfo.getSSID());
        */
        WifiInfo wifiInfo2 = wifi.getConnectionInfo();





        /*<---------- button series ---------->*/

        //button previous series
        View.OnClickListener listen_previous02 = new View.OnClickListener()
        {
            public void onClick(View v)
            {
                info02.setVisibility(GONE);
                info01.setVisibility(View.VISIBLE);
                skip.setVisibility(View.VISIBLE);
            }
        };
        info02_previous.setOnClickListener(listen_previous02);

        View.OnClickListener listen_previous03 = new View.OnClickListener()
        {
            public void onClick(View v)
            {
                info03.setVisibility(GONE);
                info02.setVisibility(View.VISIBLE);
                skip.setVisibility(View.VISIBLE);
            }
        };
        info03_previous.setOnClickListener(listen_previous03);


        View.OnClickListener listen_previous05 = new View.OnClickListener()
        {
            public void onClick(View v)
            {
                info05.setVisibility(GONE);
                info03.setVisibility(View.VISIBLE);
                skip.setVisibility(View.VISIBLE);
            }
        };
        info05_previous.setOnClickListener(listen_previous05);

        View.OnClickListener listen_previous06 = new View.OnClickListener()
        {
            public void onClick(View v)
            {
                info06.setVisibility(GONE);
                info05.setVisibility(View.VISIBLE);
                skip.setVisibility(View.VISIBLE);
            }
        };
        info06_previous.setOnClickListener(listen_previous06);

        View.OnClickListener listen_previous07 = new View.OnClickListener()
        {
            public void onClick(View v)
            {
                info07.setVisibility(GONE);
                info06.setVisibility(View.VISIBLE);
                skip.setVisibility(View.VISIBLE);
            }
        };
        info07_previous.setOnClickListener(listen_previous07);

        View.OnClickListener listen_previous08 = new View.OnClickListener()
        {
            public void onClick(View v)
            {
                info08.setVisibility(GONE);
                info07.setVisibility(View.VISIBLE);
                skip.setVisibility(View.VISIBLE);
            }
        };
        info08_previous.setOnClickListener(listen_previous08);

        //button next series
        View.OnClickListener listen_next01 = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                info01.setVisibility(GONE);
                info02.setVisibility(View.VISIBLE);
                skip.setVisibility(View.VISIBLE);
            }
        };
        info01_next.setOnClickListener(listen_next01);

        View.OnClickListener listen_next02 = new View.OnClickListener()
        {
            public void onClick(View v)
            {
                info02.setVisibility(GONE);
                info03.setVisibility(View.VISIBLE);
                skip.setVisibility(View.VISIBLE);
            }
        };
        info02_next.setOnClickListener(listen_next02);

        View.OnClickListener listen_next03 = new View.OnClickListener()
        {
            public void onClick(View v)
            {
                info03.setVisibility(GONE);
                info05.setVisibility(View.VISIBLE);
                skip.setVisibility(View.VISIBLE);
            }
        };
        info03_next.setOnClickListener(listen_next03);


        View.OnClickListener listen_next05 = new View.OnClickListener()
        {
            public void onClick(View v)
            {
                info05.setVisibility(GONE);
                info06.setVisibility(View.VISIBLE);
                skip.setVisibility(View.VISIBLE);
            }
        };
        info05_next.setOnClickListener(listen_next05);

        View.OnClickListener listen_next06 = new View.OnClickListener()
        {
            public void onClick(View v)
            {
                info06.setVisibility(GONE);
                info07.setVisibility(View.VISIBLE);
                skip.setVisibility(View.VISIBLE);
            }
        };
        info06_next.setOnClickListener(listen_next06);

        View.OnClickListener listen_next07 = new View.OnClickListener()
        {
            public void onClick(View v)
            {
                info07.setVisibility(GONE);
                info08.setVisibility(View.VISIBLE);
                skip.setVisibility(View.VISIBLE);
            }
        };
        info07_next.setOnClickListener(listen_next07);

        View.OnClickListener listen_next08 = new View.OnClickListener()
        {
            public void onClick(View v)
            {
                info08.setVisibility(GONE);
                scan.setVisibility(View.VISIBLE);
                skip.setVisibility(GONE);
                //gone_main.setVisibility(View.VISIBLE);
            }
        };
        info08_next.setOnClickListener(listen_next08);

    }

    BroadcastReceiver mWifiScanReceiver;
    //버튼 클릭시
    public void onClick(View v) {
        //on
        if (v.getId() == R.id.switch_on) {
            Log.d(TAG, "ScanStart()");
            scanoffFlag = true;
            initWIFIScan(); // start WIFIScan

        }
        //off
        if (v.getId() == R.id.switch_off) {
            if(scanoffFlag) {
                Log.d(TAG, "ScanStop()");
                oData.clear();
                m_oListView = (ListView)findViewById(R.id.listView);
                ListAdapter oAdapter = new ListAdapter(oData);
                m_oListView.setAdapter(oAdapter);
                scanoffFlag = false;
                unregisterReceiver(mWifiScanReceiver);
            }
        }
        if(v.getId() == R.id.textDate){
            if(connselFlag){    //구 connect_button
                Log.d(TAG, "Clicked : textData_01 - connect_button");
                connselFlag = false;
                View oParentView = (View)v.getParent();
                TextView oTextTitle = (TextView) oParentView.findViewById(R.id.textDate);
                String ssid=oTextTitle.getText().toString();  //선택된 wifi의 ssid를 가져옴
                switch_connect(ssid,scResult);
            }
            else{   //구 select_button
                //접속한 와이파이의 ip주소를 얻어옴
                Log.d(TAG, "Clicked : textData_02 - select_button");
                connselFlag = true;
                WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                DhcpInfo dhcpInfo = wm.getDhcpInfo() ;
                int serverIP = dhcpInfo.gateway;
                String ipAddress = String.format(
                        "%d.%d.%d.%d",
                        (serverIP & 0xff),
                        (serverIP >> 8 & 0xff),
                        (serverIP >> 16 & 0xff),
                        (serverIP >> 24 & 0xff));

                Log.d("연결",ipAddress);

                mClientThread = new ClientThread(ipAddress, mMainHandler);
                mClientThread.start();
                View oParentView = (View)v.getParent();
                TextView oTextTitle = (TextView) oParentView.findViewById(R.id.textDate);
                show();
                String pre_ssid=oTextTitle.getText().toString();  //선택된 wifi의 ssid를 가져옴
                family_ssid=pre_ssid;
            }
        }
    }



    //비밀번호 입력창 (다이얼로그)
    public void show()
    {
        final EditText edittext = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("WiFi 입력창");
        builder.setMessage("WiFi의 비밀번호를 입력하시오.");
        builder.setView(edittext);
        builder.setPositiveButton("입력",                       //입력을 누르게 되었을때 데이터가 전송됨
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        password=edittext.getText().toString();   //입력한 비밀번호를 가져옴
                        family_wifi=family_ssid+"@"+password+"#";   //하나의 문자열에 ssid와 password를 저장함, 개행문자로 구분
                        if(password!=null){
                            if (SendThread.mHandler != null) {
                                Message msg = Message.obtain();
                                msg.what = 1;
                                msg.obj = family_wifi;
                                SendThread.mHandler.sendMessage(msg);     //데이터 전송
                            }
                        }
                        dialog.dismiss();
                        //Toast.makeText(getApplicationContext(),edittext.getText().toString() ,Toast.LENGTH_LONG).show();
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




    //wifi 초기화
    public void initWIFIScan() {
        //주변의 wifi신호를 검색함, 주기적으로 wifi신호를 받아온다
        mWifiScanReceiver= new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final String action = intent.getAction();
                if (action.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                    Log.d(TAG, "onReceive - WifiManager.SCAN_RESULTS_AVAILABLE_ACTION");
                    scanResult = wifi.getScanResults();
                    String wifilists[] = new String[scanResult.size()];
                    boolean compdata = false;
                    int compflag = 0;
                    int levelSensitivity = -80;//previous : -73
                    for (int i = 0; i < scanResult.size(); i++) {
                        ScanResult result = scanResult.get(i);
                        wifilists[i] = new String(result.SSID.toString());
                        for(int j=0;j<i;j++){
                            compdata = wifilists[j].equals(result.SSID.toString());
                            if(compdata == true){
                                compflag ++;
                                compdata = false;
                            }
                        }
                        if((compflag == 0) && (result.level > levelSensitivity)) {
                            ID_item oItem = new ID_item();
                            oItem.strDate =result.SSID;
                            oData.add(oItem);

                        }
                        else {
                            compflag = 0;
                            compdata = false;
                        }

                    }
                    m_oListView = (ListView)findViewById(R.id.listView);
                    ListAdapter oAdapter = new ListAdapter(oData);
                    m_oListView.setAdapter(oAdapter);
                }
                else if (action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
                    Log.d(TAG,
                            "onReceive - WifiManager.NETWORK_STATE_CHANGED_ACTION");
                    sendBroadcast(new Intent("wifi.ON_NETWORK_STATE_CHANGED"));
                }

            }
        };
        // scanCount = 0;
        text = "";
        final IntentFilter filter = new IntentFilter(
                WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        registerReceiver( mWifiScanReceiver , filter);  //receiver를 등록한다
        wifi.startScan();  //스캔을 시작함
        Log.d(TAG, "initWIFIScan()");
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };


    public void switch_set(){
        Intent intentConfirm = new Intent();
        intentConfirm.setAction("android.settings.WIFI_SETTINGS");
        startActivity(intentConfirm);
    }

    public void switch_connect(String networkSSID, ScanResult scanResult){
        //https://code.i-harness.com/ko-kr/q/868e72
        try {
            String networkPass = null;
            WifiConfiguration conf = new WifiConfiguration();
            conf.SSID = "\"" + networkSSID + "\"";   // Please note the quotes. String should contain ssid in quotes
            conf.status = WifiConfiguration.Status.DISABLED;
            conf.priority = 40;

            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            conf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            conf.allowedAuthAlgorithms.clear();
            conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);

            //WifiManager wifiManager = (WifiManager) WiFiApplicationCore.getAppContext().getSystemService(Context.WIFI_SERVICE);
            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
            int netId = wifiManager.addNetwork(conf);
            //wifiManager.removeNetwork(netId);
            //wifiManager.disableNetwork(netId);

            wifiManager.disconnect();
            wifiManager.enableNetwork(netId, true);
            wifiManager.updateNetwork(conf);


            if(wifiManager.reconnect()==true){
                Toast.makeText(getApplicationContext(),networkSSID+"에 접속하였습니다.",Toast.LENGTH_SHORT).show();
            };

        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Handler mMainHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: // 화면에 데이터 출력
                    break;
            }
        }
    };

}


class ClientThread extends Thread {

    private String mServAddr;
    private Handler mMainHandler;

    public ClientThread(String servAddr, Handler mainHandler) {
        mServAddr = servAddr;
        mMainHandler = mainHandler;
    }

    @Override
    public void run() {
        Socket sock = null;
        try {
            sock = new Socket(mServAddr, 8000);
            doPrintln(">> 서버와 연결 성공!");

            SendThread sendThread = new SendThread(this, sock.getOutputStream());
            RecvThread recvThread = new RecvThread(this, sock.getInputStream());
            sendThread.start();
            recvThread.start();
            sendThread.join();
            recvThread.join();

        } catch (Exception e) {
            doPrintln(e.getMessage());
        } finally {
            try {
                if (sock != null) {
                    sock.close();
                    doPrintln(">> 서버와 연결 종료!");
                }
            } catch (IOException e) {
                doPrintln(e.getMessage());
            }
        }
    }

    public void doPrintln(String str) {
        Message msg = Message.obtain();
        msg.what = 1;
        msg.obj = str + "\n";
        mMainHandler.sendMessage(msg);
    }
}

class SendThread extends Thread {

    private ClientThread mClientThread;
    private OutputStream mOutStream;
    public static Handler mHandler;

    public SendThread(ClientThread clientThread, OutputStream outStream) {
        mClientThread = clientThread;
        mOutStream = outStream;
    }

    @Override
    public void run() {
        Looper.prepare();
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1: // 데이터 송신 메시지
                        try {
                            String s = (String) msg.obj;
                            mOutStream.write(s.getBytes());
                            mClientThread.doPrintln("[보낸 데이터] " + s);
                        } catch (IOException e) {
                            mClientThread.doPrintln(e.getMessage());
                        }
                        break;
                    case 2: // 스레드 종료 메시지
                        getLooper().quit();
                        break;
                }
            }
        };
        Looper.loop();
    }
}

class RecvThread extends Thread {

    private ClientThread mClientThread;
    private InputStream mInStream;

    public RecvThread(ClientThread clientThread, InputStream inStream) {
        mClientThread = clientThread;
        mInStream = inStream;
    }

    @Override
    public void run() {
        byte[] buf = new byte[1024];
        while (true) {
            try {
                int nbytes = mInStream.read(buf);
                if (nbytes > 0) {
                    String s = new String(buf, 0, nbytes);
                    mClientThread.doPrintln("[받은 데이터] " + s);
                } else {
                    mClientThread.doPrintln(">> 서버가 연결 끊음!");
                    if (SendThread.mHandler != null) {
                        Message msg = Message.obtain();
                        msg.what = 2;
                        SendThread.mHandler.sendMessage(msg);
                    }
                    break;
                }
            } catch (IOException e) {
                mClientThread.doPrintln(e.getMessage());
            }
        }
    }
}