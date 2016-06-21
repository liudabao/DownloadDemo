package com.example.lenovo.downloaddemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button start;
    Button pause;
   // MyService.MyBinder binder;
    private MyService mService;
    ServiceConnection connection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connection=new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mService=((MyService.MyBinder)service).getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        Intent intent=new Intent(MainActivity.this, MyService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
        initView();

    }

    private void initView(){
        start=(Button)findViewById(R.id.button);
        pause=(Button)findViewById(R.id.button2);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT>=23){
                    String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
                    int permsRequestCode = 200;
                    ActivityCompat.requestPermissions( MainActivity.this, perms, permsRequestCode);
                }
                else{
                   // Intent intent=new Intent(MainActivity.this, MyService.class);
                    //startService(intent);
                    mService.download();
                }
            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent intent=new Intent(MainActivity.this, MyService.class);
              //  stopService(intent);
                mService.task.flag=false;
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults){

        switch(permsRequestCode){

            case 200:
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                  // Intent intent=new Intent(MainActivity.this, MyService.class);
                   //startService(intent);
                    mService.download();
                }

                break;

        }

    }
    @Override
    protected void onDestroy() {
        unbindService(connection);
        super.onDestroy();
    }
}
