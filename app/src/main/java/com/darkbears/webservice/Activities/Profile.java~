package com.darkbears.webservice.Activities;

import android.os.Bundle;
import android.support.annotation.StringDef;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.darkbears.webservice.R;

public class Profile extends AppCompatActivity {
TextView details;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        details=(TextView)findViewById(R.id.textView);

        String name=getIntent().getStringExtra("name");
        String userid=getIntent().getStringExtra("userid");
        String email=getIntent().getStringExtra("email");

        details.setText(name + "\n"+userid +"\n"+email);




    }

}
