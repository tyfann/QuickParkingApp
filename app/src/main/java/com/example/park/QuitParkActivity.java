package com.example.park;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.park.findroad.Map;
import com.example.park.findroad.New;
import com.example.park.fragment.InputMap;
import com.example.park.fragment.Mapfragment;
import com.example.park.fragment.Parking;
import com.example.park.util.DateUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;


public class QuitParkActivity extends AppCompatActivity{


    public String queryId;
    TextView tv_scanResult;
    List<InputMap> inputMaps;
    public String scan_time;
    public String last_time;
    char selection;
    protected Handler mHandler1=new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanresult);
        Intent intent = this.getIntent();
        queryId = intent.getStringExtra("querycode");
        last_time = intent.getStringExtra("time");

        Bmob.initialize(QuitParkActivity.this,"8803b2c419834daa0f57d8c26aea6c28");
        tv_scanResult = findViewById(R.id.tv_scanresult);
        ScanForResult();
        try {
            Thread.currentThread().sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mHandler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(QuitParkActivity.this, MainActivity.class);
                //进行跳转操作
                startActivity(intent);
            }
        },1000);
    }

    private void ScanForResult(){
        scan_time= DateUtil.getNowTime();
        Time time = new Time();
        time.setIntime(scan_time);
        time.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e==null){
                    Toast.makeText(QuitParkActivity.this,"出车库成功，上次进入时间为："+last_time+"此次出车库时间为:"+scan_time,Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(QuitParkActivity.this,"上传失败！",Toast.LENGTH_SHORT).show();
                }
            }
        });
        BmobQuery<Parking> query = new BmobQuery<Parking>();
        query.addWhereLessThan("Values",5);
        query.setLimit(500);
        query.order("createdAt");
        query.setCachePolicy(BmobQuery.CachePolicy.IGNORE_CACHE);
        query.findObjects(new FindListener<Parking>() {
            @SuppressLint("WrongConstant")
            @Override
            public void done(List<Parking> object, BmobException e) {
                if(e==null){
                    BmobQuery<Parking> bmobQuery = new BmobQuery<Parking>();
                    bmobQuery.getObject(queryId, new QueryListener<Parking>() {
                        @Override
                        public void done(Parking object,BmobException e) {
                            if(e==null){
                                Parking p1 = new Parking();
                                p1.setValues(2);
                                p1.setCo_X(object.getCo_X());
                                p1.setCo_Y(object.getCo_Y());

                                p1.update(queryId, new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e==null){
                                            Toast.makeText(QuitParkActivity.this,"成功离开车库!",Toast.LENGTH_SHORT).show();

                                        }else {
                                            Toast.makeText(QuitParkActivity.this,"离开车库失败!"+e.getMessage(),Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                //Log.v("ScanResultActivity:",Integer.toString(object.getValues()));
                            }else{
                                //Log.v("ScanResultActivity:",e.getMessage());
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(QuitParkActivity.this,"缓存失败"+e.getMessage(),Toast.LENGTH_LONG).show();
                    System.out.println("缓存失败！！！");
                }
            }
        });

    }

}