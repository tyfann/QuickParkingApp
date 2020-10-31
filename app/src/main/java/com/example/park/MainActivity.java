package com.example.park;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.park.fragment.Mainfragment;
import com.example.park.fragment.Mapfragment;
import com.example.park.fragment.Myfragment;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    protected LinearLayout mMenuMain;
    protected LinearLayout mMenuMap;
    protected LinearLayout mMenuMy;
    public String queryable;
  //  protected int temp;

    //定义了三个fragment
    protected Mainfragment  mMainFragment=new Mainfragment();
    protected Mapfragment mMapFragment=new Mapfragment();
    protected Myfragment mMyFragment=new Myfragment();
    public static String Park1 = "ParkingMap1";
    public static String Park2 = "ParkingMap2";
    public String scan_time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //指定其布局文件
        setContentView(R.layout.activity_main);
        initView();
        int fragid;
        LinearLayout main1 = (LinearLayout)findViewById(R.id.menu_main);
        LinearLayout map1 = (LinearLayout)findViewById(R.id.menu_map);
        LinearLayout my1 = (LinearLayout)findViewById(R.id.menu_my);
        //RelativeLayout mymap1 = (RelativeLayout) findViewById(R.id.mymap);


        Mapfragment mMapFragment1 = new Mapfragment();
        //定义一个maptrace用于接收ScanResultActivity里的mapstr
        //定义一个maptrace1用于接收ScanResultActivity里的mapstr1
        String maptrace,maptrace1;
        if(getIntent() != null){
            //停车路线
            maptrace = getIntent().getStringExtra(ScanResultActivity.MAPTRACE);
            //找车路线
            maptrace1 = getIntent().getStringExtra(ScanResultActivity.MAPTRACE1);
            queryable = getIntent().getStringExtra(ScanResultActivity.MAPQUERY);
            scan_time  =getIntent().getStringExtra(ScanResultActivity.MAPTIME);

       //     fragid = R.id.menu_map;
           // temp = -123;
        }
        else {
            maptrace = "maptrace(停车)传数据失败了！";
            maptrace1 = "maptrace1(找车)传数据失败了！";
        }

        this.getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.continer_content,mMainFragment)
                .add(R.id.continer_content,mMapFragment = mMapFragment1.newInstance(maptrace,maptrace1))
                .hide(mMapFragment)
                .add(R.id.continer_content,mMyFragment)
                .hide(mMyFragment)
                .commit();

        main1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .show(mMainFragment)
                        .hide(mMapFragment)
                        .hide(mMyFragment)
                        .commit();
            }
        });

        map1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .hide(mMainFragment)
                        .show(mMapFragment)
                        .hide(mMyFragment)
                        .commit();
            }
        });

        my1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .hide(mMainFragment)
                        .hide(mMapFragment)
                        .show(mMyFragment)
                        .commit();
            }
        });

       /* mymap1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .hide(mMainFragment)
                        .show(mMapFragment)
                        .hide(mMyFragment)
                        .commit();
            }
        });*/



        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
/*
        this.mMainFragment.btn_Scanner=findViewById(R.id.btn_qrocde);
        if (mMainFragment.btn_Scanner==null) {
            Toast.makeText(this,"扫码功能失败",Toast.LENGTH_SHORT).show();

        }else{
            mMainFragment.btn_Scanner.setOnClickListener(MainActivity.this);
        }

 */
        //事务添加 默认：显示首页 其他页面：隐藏
        //提交
    }

    //初始化视图
    public void initView(){
        mMenuMain=(LinearLayout)this.findViewById(R.id.menu_main);
        mMenuMap=(LinearLayout)this.findViewById(R.id.menu_map);
        mMenuMy=(LinearLayout)this.findViewById(R.id.menu_my);


        mMenuMain.setOnClickListener(this);
        mMenuMap.setOnClickListener(this);
        mMenuMy.setOnClickListener(this);

    }

    private void showFragment(){

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
       // fragid = v.getId();
       /* switch (fragid){
            case R.id.menu_main://首页
                this.getSupportFragmentManager()
                        .beginTransaction()
                        .show(mMainFragment)
                        .hide(mMapFragment)
                        .hide(mMyFragment)
                        .commit();
                //mMenuMain.setBackgroundColor(0000);
                break;
            case R.id.menu_map://地图
                this.getSupportFragmentManager()
                        .beginTransaction()
                        .hide(mMainFragment)
                        .show(mMapFragment)
                        .hide(mMyFragment)
                        .commit();

                // mMenuMain.setBackgroundColor();
                // mMenuMain.setBackgroundColor(0000);
                break;
            case R.id.menu_my://我的
                this.getSupportFragmentManager()
                        .beginTransaction()
                        .hide(mMainFragment)
                        .hide(mMapFragment)
                        .show(mMyFragment)
                        .commit();
                break;
            default:
        }*/
    }
    private void scanCode() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setCaptureActivity(CaptureAct.class);
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        intentIntegrator.setPrompt("Scanning Code");
        intentIntegrator.initiateScan();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);

        if (result!=null) {
            if (result.getContents()!=null) {
                if(result.getContents().equals(Park1)){

                    Intent intent = new Intent(this, ScanResultActivity.class);
                    startActivity(intent);


                } else if(result.getContents().equals(Park2)){
                    Intent intent = new Intent(this,QuitParkActivity.class);
                    intent.putExtra("querycode",queryable);
                    intent.putExtra("time",scan_time);
                    startActivity(intent);


                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(result.getContents());
                    builder.setTitle("Scanning Result");
                    builder.setPositiveButton("Scan Again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            scanCode();
                        }
                    }).setNegativeButton("Finish", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

            }else {
                Toast.makeText(this,"No Result!",Toast.LENGTH_LONG).show();
            }
        }else {
            super.onActivityResult(requestCode,resultCode,data);
        }
    }


}

