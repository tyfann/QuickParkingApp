package com.example.park.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.park.Loading;
import com.example.park.LoginActivity;
import com.example.park.MainActivity;
import com.example.park.MapActivity2;
import com.example.park.R;

public class Myfragment extends Fragment{
    protected Button mBtnLogin;
    TextView testview1;
    int myback = R.id.menu_map;

   /* protected Mainfragment  mMainFragment=new Mainfragment();
    protected Mapfragment mMapFragment=new Mapfragment();
    protected Myfragment mMyFragment=new Myfragment();*/

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mBtnLogin = getView().findViewById(R.id.btn_login);
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //登录
                Intent login = new Intent(getActivity(), LoginActivity.class);
                startActivity(login);
            }
        });

        LinearLayout feedback = (LinearLayout) getView().findViewById(R.id.feedback);
        LinearLayout order = (LinearLayout) getView().findViewById(R.id.order);
        LinearLayout favourite = (LinearLayout) getView().findViewById(R.id.favourite);

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Order1 = new Intent(getActivity(), Loading.class);
                startActivity(Order1);
            }
        });

        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Favourite1 = new Intent(getActivity(), Loading.class);
                startActivity(Favourite1);
            }
        });

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent feedback1 = new Intent(getActivity(), Loading.class);
                startActivity(feedback1);
            }
        });

        /*RelativeLayout mymap1 = (RelativeLayout) getView().findViewById(R.id.mymap);
        mymap1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myback = R.id.menu_map;
            }
        });*/
    }

}

