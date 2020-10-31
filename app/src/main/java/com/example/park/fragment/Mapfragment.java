package com.example.park.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import com.example.park.MainActivity;
import com.example.park.R;
import com.example.park.findroad.New;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.UpdateListener;

public class Mapfragment extends Fragment {

    public static final String BUNDLE_MAPSTRING = "bundle_mapstring";
    public static final String BUNDLE_MAPSTRING1 = "bundle_mapstring1";
    private String mapstring,mapstring1;
   // private String mapstring1;

    //接收传值
    public static Mapfragment newInstance(String mapstring,String mapstring1){
        Mapfragment mapfragment = new Mapfragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_MAPSTRING,mapstring);
        bundle.putString(BUNDLE_MAPSTRING1,mapstring1);
        mapfragment.setArguments(bundle);
        return mapfragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //数据库相关
        Bmob.initialize(getActivity(),"8803b2c419834daa0f57d8c26aea6c28");
        View square = inflater.inflate(R.layout.fragment_map,container,false);
        Context context=getActivity();
        New str = new New(context);

        if(getArguments() != null){
            //从string.xml中取出来
            mapstring = getArguments().getString(BUNDLE_MAPSTRING);//停车
            mapstring1 = getArguments().getString(BUNDLE_MAPSTRING1);//取车
        }
        else {
            mapstring="扫码失败！(停车失败)";
            mapstring1="失败！(找车失败)";

        }
        //设置了string
        Button btn1 = (Button)square.findViewById(R.id.Parkway);
        Button btn2 = (Button) square.findViewById(R.id.Findingway);

        TextView textView = (TextView) square.findViewById(R.id.maping);
        //停车
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText(getString(R.string.park,mapstring));
            }
        });
        //找车
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText(getString(R.string.park,mapstring1));
            }
        });

        //String sinfo = getResources().getString(R.string.park);
       // String sfinal = String.format(sinfo,mapstring);
        /*click的butten的id不同
        if(){
            //停车

        }
        else{
            //找车
            textView.setText(getString(R.string.park,mapstring1));
        }
         */

        textView.setText(getString(R.string.park,mapstring));
        textView.setMovementMethod(ScrollingMovementMethod.getInstance());

        //初始化车位信息，只需执行一次（你们别执行
        //initParking();
        //从云端获取车位数据信息
        //getParking();
        //修改云端某条数据（停完车后
        //updataParking();
        return square;
    }

    @Nullable
    @Override
    public View getView() {
        return super.getView();
    }

    public void onSaveInstanceState(@NonNull Bundle outState) {
        //super.onSaveInstanceState(outState);
    }

    private void initParking() {
        List<BmobObject> parkings=new ArrayList<>();
        for(int i=0;i<100;i++){
            Parking parking=new Parking();
            parking.setCo_X(i);
            parking.setCo_Y(i);
            parking.setValues(i);
            parkings.add(parking);
        }
        new BmobBatch().insertBatch(parkings).doBatch(new QueryListListener<BatchResult>() {
            @Override
            public void done(List<BatchResult> results, BmobException e) {
                if(e==null){
                    for(int i=0;i<results.size();i++){
                        BatchResult result=results.get(i);
                        BmobException ex=result.getError();
                        if(ex==null){
                            // Toast.makeText(SecondActivity.this,"No."+i+"数据批量添加成功",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getActivity(),"No."+i+"数据批量添加失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
                    Toast.makeText(getActivity(),"数据批量添加失败",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getParking() {
        BmobQuery<Parking> query = new BmobQuery<Parking>();
        query.addWhereEqualTo("EmptyIF", Boolean.TRUE);
        query.order("createdAt");
        query.setCachePolicy(BmobQuery.CachePolicy.IGNORE_CACHE);
        query.findObjects(new FindListener<Parking>() {
            @Override
            public void done(List<Parking> object, BmobException e) {
                if (e == null) {
                    Toast.makeText(getActivity(), "缓存成功，共" + object.size() + "条数据", Toast.LENGTH_SHORT).show();
                    //List list=new ArrayList();
                    String s = "";
                    for (int i = 0; i < object.size(); i++) {
                        //text_print.setText("X坐标："+object.get(i).getCo_X()+"Y坐标："+object.get(i).getCo_Y());
                        //list.add("X坐标："+object.get(i).getCo_X()+"Y坐标："+object.get(i).getCo_Y());
                        // if(object.get(i).getEmptyIF()==Boolean.TRUE)
                        s = s.concat(object.get(i).getObjectId()+" X坐标：" + object.get(i).getCo_X() + " Y坐标：" + object.get(i).getCo_Y() + " 空\n");
                        //id用来后续在云端修改数据，读下来的数据你可以选择缓存在一个数组里。
                    }
                    Log.d("MapActivity","坐标信息"+s);
                }
                else {
                    Toast.makeText(getActivity(), "缓存失败", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    private void updataParking() {
        Parking p = new Parking();
        //objectId改成需要修改的数据的objectId，下面只是举了个例子
        p.update("ba84a596fa", new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast.makeText(getActivity(), "更新成功", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("BMOB", e.toString());
                    Toast.makeText(getActivity(), "更新失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}

