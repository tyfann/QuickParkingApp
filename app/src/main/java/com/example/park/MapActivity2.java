package com.example.park;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MapActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String maptrace = null;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map3);

        TextView textView = (TextView) findViewById(R.id.textView2);
        textView.setText(getString(R.string.map));
    }
}