package com.example.lenovo.downloaddemo;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView(){
        bt=(Button)findViewById(R.id.button);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT>=23){
                    String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
                    int permsRequestCode = 200;
                    ActivityCompat.requestPermissions( MainActivity.this, perms, permsRequestCode);
                }
                else{
                    Intent intent=new Intent(MainActivity.this, MyService.class);
                    startService(intent);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults){

        switch(permsRequestCode){

            case 200:
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                   Intent intent=new Intent(MainActivity.this, MyService.class);
                   startService(intent);
                }

                break;

        }

    }

}
