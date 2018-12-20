package com.ty.android.mypermission;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.skyward.android.permission.OnPermissionListener;
import com.skyward.android.permission.RequestPermission;

/**
 * @author skyward
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestPermission.request(MainActivity.this, new OnPermissionListener() {
                    @Override
                    public void onPermissionSuccess() {
                        Toast.makeText(MainActivity.this, "取得权限", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionFailure(String[] permission) {
                        Toast.makeText(MainActivity.this, "禁止权限", Toast.LENGTH_SHORT).show();
                    }
                }, Manifest.permission.WRITE_EXTERNAL_STORAGE);


            }
        });

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RequestPermission.request(MainActivity.this, new OnPermissionListener() {
                    @Override
                    public void onPermissionSuccess() {
                        Toast.makeText(MainActivity.this,"取得权限",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionFailure(String[] permission) {
                        Toast.makeText(MainActivity.this,"禁止权限",Toast.LENGTH_SHORT).show();
                    }
                }, Manifest.permission.CALL_PHONE,Manifest.permission.CAMERA);
            }
        });


    }


}
