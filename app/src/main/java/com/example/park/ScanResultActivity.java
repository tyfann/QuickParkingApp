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


public class ScanResultActivity extends AppCompatActivity{
    public static final String MAPTRACE1 = "maptrace1";
    public static final String MAPTRACE = "maptrace";
    public static final String MAPQUERY = "queryable";
    public static final String MAPTIME = "time";

    public String mapstr = null;       //停车，找车位
    public String mapstr1 = null;      //找车
    public String queryId;

    TextView tv_scanResult;
    List<InputMap> inputMaps;
    public String scan_time;
    char selection;
    protected Handler mHandler1=new Handler();
    New anew=new New(ScanResultActivity.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanresult);
        Bmob.initialize(ScanResultActivity.this,"8803b2c419834daa0f57d8c26aea6c28");
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
                Intent intent = new Intent(ScanResultActivity.this, MainActivity.class);
                Log.v("ScanResultActivity","mapstr"+mapstr);
                if(mapstr == null){
                    mapstr = "查找车位失败！（mapstr是空指针！）";
                }
                if(mapstr1 == null){
                    mapstr1 = "找车操作失败！（mapstr1是空指针！）";
                }
                //用intent将这两个数据传递出去
                intent.putExtra(MAPTRACE,mapstr);
                intent.putExtra(MAPTRACE1,mapstr1);
                intent.putExtra(MAPQUERY,queryId);
                intent.putExtra(MAPTIME,scan_time);
                //打印在日志上以备查看
                Log.v("ScanResultActivity","mapstr"+mapstr);
                //进行跳转操作
                startActivity(intent);
            }
        },2000);
    }

    private void ScanForResult(){
        scan_time= DateUtil.getNowTime();
        Time time = new Time();
        time.setIntime(scan_time);
        time.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e==null){
                    Toast.makeText(ScanResultActivity.this,"进入时间记录已上传！",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ScanResultActivity.this,"上传失败！",Toast.LENGTH_SHORT).show();
                }
            }
        });
        HashMap<String,String> IdMap = new HashMap<>();
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
                    //Toast.makeText(ScanResultActivity.this,"缓存成功，共"+object.size()+"条数据",Toast.LENGTH_SHORT).show();
                    //List list=new ArrayList();
                    String s="";
                    inputMaps = new ArrayList<>();
                    InputMap inputMap1 = null;
                    for(int i=0;i<object.size();i++){
                        //text_print.setText("X坐标："+object.get(i).getCo_X()+"Y坐标："+object.get(i).getCo_Y());
                        //list.add("X坐标："+object.get(i).getCo_X()+"Y坐标："+object.get(i).getCo_Y());
                        // if(object.get(i).getEmptyIF()==Boolean.TRUE)
                        Integer x = object.get(i).getCo_X();
                        Integer y = object.get(i).getCo_Y();
                        inputMap1 = new InputMap(x,y,object.get(i).getValues());
                        String inode = Integer.toString(x).concat("+").concat(Integer.toString(y));
                        IdMap.put(inode,object.get(i).getObjectId());
                        //Log.v("ScanResultActivity",object.toString());
                        inputMaps.add(inputMap1);
                        //s=s.concat("X坐标："+x+" Y坐标："+y+object.get(i).getValues()+"\n");
                    }
                    selection='A';
                    mapstr = anew.PrintParkingPath(inputMaps,selection);
                    String queryable = Integer.toString(anew.des[0]).concat("+").concat(Integer.toString(anew.des[1]));
                    anew.mapstringchange();
                    mapstr1 = anew.findparking(inputMaps);
                    Log.v("ScanResultActivity","mapstr1:"+mapstr1);

                    queryId = IdMap.get(queryable);
                    //Log.v("ScanResultActivity:",queryId);


                    BmobQuery<Parking> bmobQuery = new BmobQuery<Parking>();
                    bmobQuery.getObject(queryId, new QueryListener<Parking>() {
                        @Override
                        public void done(Parking object,BmobException e) {
                            if(e==null){
                                Parking p1 = new Parking();
                                p1.setValues(4);
                                p1.setCo_X(object.getCo_X());
                                p1.setCo_Y(object.getCo_Y());

                                p1.update(queryId, new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e==null){
                                            Toast.makeText(ScanResultActivity.this,"更新车库成功!",Toast.LENGTH_SHORT).show();

                                        }else {
                                            Toast.makeText(ScanResultActivity.this,"更新车库失败!"+e.getMessage(),Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                Log.v("ScanResultActivity:",Integer.toString(object.getValues()));
                            }else{
                                Log.v("ScanResultActivity:",e.getMessage());
                            }
                        }
                    });





                    //打印停车路径
                 //   anew.PrintParkingPath(inputMaps,selection);
                    //打印找车路径
                    Log.v("ScanResultActivity","queryId:"+queryId);

                }
                else{
                    Toast.makeText(ScanResultActivity.this,"缓存失败"+e.getMessage(),Toast.LENGTH_SHORT).show();
                    System.out.println("缓存失败！！！");
                }
            }
        });

    }

}