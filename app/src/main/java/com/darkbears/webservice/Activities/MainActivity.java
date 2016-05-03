package com.darkbears.webservice.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.darkbears.webservice.R;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.RegexpValidator;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends ActionBarActivity {
MaterialEditText name,email,phone,password;
Button  signup;
TextView login;
RelativeLayout background;
String sname,semail,sphone,spassword;
ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();

        initonclicklistner();

    }

    private void initonclicklistner() {

        login.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    login.setTextColor(Color.RED);


                }else if (event.getAction()==MotionEvent.ACTION_UP){
                    login.setTextColor(Color.WHITE);
                    Intent in = new Intent(getApplicationContext(),Login.class);
                    startActivity(in);
                    finish();
                }
                return true;
            }
        });


        signup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    signup.setBackgroundResource(R.drawable.greenbt);
                    sname=name.getText().toString().trim();
                    semail=email.getText().toString().trim();
                    sphone=phone.getText().toString().trim();
                    spassword=password.getText().toString().trim();

                  }else {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        signup.setBackgroundResource(R.drawable.smallbt);

                        if(semail.length()==0||sname.length()==0||sphone.length()==0||spassword.length()==0){

                            if(sname.length()==0)
                                name.setError("Please Enter Your Name");
                        }
                        if (spassword.length() < 4) {
                            password.setError("Please Enter More Than 3 Characters");
                        }
                            if(semail.length()==0)
                            email.setError("Please Enter Email Address");
                            else  if (!isValidEmail(semail)) {

                                email.setError("Email Address Is Not Valid");
                            }

                            if(sphone.length()==0)
                            phone.setError("Please Enter Mobile Number");

                            else if (sphone.length() < 10) {
                                phone.setError("Invalid Mobile Number");



                        }else {

                            if (!isValidEmail(semail)) {

                                email.setError("Email Address Is Not Valid");
                            }  else if (sphone.length() < 10) {
                                phone.setError("Invalid Mobile Number");
                            } else if (spassword.length() < 4) {
                                password.setError("Please Enter More Than 3 Characters");
                            } else {
                                InputMethodManager imm = (InputMethodManager)getSystemService(Service.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(password.getWindowToken(), 0);
                                AsyncforSignup async = new AsyncforSignup();
                                async.execute();
                            }
                        }
                    }
                }

                return true;
            }
        });
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }


    private void initialize() {

        name=(MaterialEditText)findViewById(R.id.editText);
        email=(MaterialEditText)findViewById(R.id.editText2);
        phone=(MaterialEditText)findViewById(R.id.editText3);
        password=(MaterialEditText)findViewById(R.id.editText4);
        signup=(Button)findViewById(R.id.button);
        login=(TextView)findViewById(R.id.textView2);
        background=(RelativeLayout)findViewById(R.id.background);


    }

     public class AsyncforSignup extends AsyncTask<Void,Void,Void>
    {

        String api_response,api_responsemsg;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please Wait...");
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {

            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new FormBody.Builder()
                    .add("email",semail )
                    .add("name",sname)
                    .add("phone_no",sphone)
                    .add("password",spassword)
                    .add("user_type","customer")
                    .build();
            Request request = new Request.Builder()
                    .url("http://dbwsweb.com/projectdemo/conwash_test/web_apis/Ws_controller/signup")
                    .post(formBody)
                    .build();

            try {
               Response response = client.newCall(request).execute();

               api_response=response.body().string();
               api_responsemsg=response.message();
                Log.e("clickingbody",api_response);

                // Do something with the response.
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            pDialog.dismiss();


            try {
                JSONObject jobj=new JSONObject(api_response);

                String message=jobj.getString("message");
                int status =jobj.getInt("status");

                if(status==1) {
                    AlertDialog.Builder alertdialog = new AlertDialog.Builder(MainActivity.this);
                    alertdialog.setMessage(message).setTitle("CONGRATULATIONS!!!")
                     .setIcon(R.drawable.tick).setCancelable(false)
                            .show();
                    Myhandler.sendEmptyMessageDelayed(1, 2000);
                }else {
                    email.setError("Email Adress Already Exist");
                }



            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }

    Handler Myhandler=new Handler(){

        @Override
        public void handleMessage(Message msg) {

                Intent in = new Intent(getApplicationContext(), Login.class);
                in.putExtra("emailaddress",semail);
             System.out.println("Hello");
                startActivity(in);
                finish();


        }

    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
