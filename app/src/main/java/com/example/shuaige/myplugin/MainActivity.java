package com.example.shuaige.myplugin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("_zs_","= = = = =>>>start<<<= = = = =");
        Test test = new Test();
        Log.i("_zs_","= = = = =>>>end<<<= = = = =");
    }
}
