package com.darkbears.webservice.Activities;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.darkbears.webservice.R;

import java.util.zip.Inflater;


public class Profile extends AppCompatActivity {
TextView details;
ImageView dp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initialize();
        initonclick();

        String name=getIntent().getStringExtra("name");
        String userid=getIntent().getStringExtra("userid");
        String email=getIntent().getStringExtra("email");

        details.setText("UserID: "+userid + "\n"+"Name: "+name +"\n"+"Email: "+email);

    }

    private void initialize() {
        details=(TextView)findViewById(R.id.textView);
        dp=(ImageView)findViewById(R.id.imageView);
    }

    private void initonclick() {

        dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDialog();
            }
        });

    }

    private void startDialog() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(Profile.this,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builderSingle.setIcon(R.drawable.upload);
        LayoutInflater inflator = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflator.inflate(R.layout.dialoge, null, false);
        ImageView gal=(ImageView)view.findViewById(R.id.imageView3);
        ImageView cam=(ImageView)view.findViewById(R.id.imageView4);
        builderSingle.setView(view);
        builderSingle.setTitle("Choose Your Option");

         gal.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Toast.makeText(Profile.this, "gallary", Toast.LENGTH_SHORT).show();
             }
         });

        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Profile.this, "Camera", Toast.LENGTH_SHORT).show();
            }
        });

        builderSingle.show();

    }


}
