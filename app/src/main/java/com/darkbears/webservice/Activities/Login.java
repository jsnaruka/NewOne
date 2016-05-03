package com.darkbears.webservice.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.Toast;

import com.darkbears.webservice.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONObject;

import java.io.IOException;

import javax.security.auth.login.LoginException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Login extends ActionBarActivity {
    MaterialEditText email,password;
    Button login,signupnow;
    String semail,spassword,getemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialize();
        initonclicklistner();
        getemail=getIntent().getStringExtra("emailaddress");
        email.setText(getemail);
    }

    private void initonclicklistner() {

        login.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    login.setBackgroundResource(R.drawable.greenbt);
                    semail=email.getText().toString();
                    spassword=password.getText().toString();

                }else if (event.getAction()==MotionEvent.ACTION_UP){
                    login.setBackgroundResource(R.drawable.smallbt);
                    InputMethodManager imm = (InputMethodManager)getSystemService(Service.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(password.getWindowToken(), 0);
                    if(semail.length()==0||password.length()==0||!isValidEmail(semail)){
                        if(password.length()==0)
                            password.setError("Please Enter Your Password");
                        if(semail.length()==0)
                            email.setError("Please Enter Email Address");
                        else  if (!isValidEmail(semail)) {

                            email.setError("Email Address Is Not Valid");
                        }
                    }else {

                        Asyncforlogin async = new Asyncforlogin();
                        async.execute();
                    }
                }
                return true;
            }
        });

        signupnow.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    signupnow.setBackgroundResource(R.drawable.greenbt);

                }else if (event.getAction()==MotionEvent.ACTION_UP){
                    signupnow.setBackgroundResource(R.drawable.smallbt);
                    Intent in = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(in);
                    finish();
                }
                return true;
            }
        });
    }

    private void initialize() {
        email=(MaterialEditText)findViewById(R.id.editText);
        password=(MaterialEditText)findViewById(R.id.editText2);
        login=(Button)findViewById(R.id.button);
        signupnow=(Button)findViewById(R.id.button2);

    }


    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
    public class Asyncforlogin extends AsyncTask<Void,Void,String>
    {

        String api_response,message,name,email1,userid;
        int status;
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage("Please Wait...");
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new FormBody.Builder()
                    .add("email", semail)
                    .add("password",spassword)
                    .build();
            Request request = new Request.Builder()
                    .url("http://dbwsweb.com/projectdemo/conwash_test/web_apis/Ws_controller/login")
                    .post(formBody)
                    .build();

            try {
                Response response = client.newCall(request).execute();

                api_response=response.body().string();
                Log.e("clickingbody", api_response);

                // Do something with the response.
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                pDialog.dismiss();
                JSONObject jobj=new JSONObject(api_response);
                Log.e("response",api_response);

                message=jobj.getString("message");
                status=jobj.getInt("status");

                JSONObject responce_data=jobj.optJSONObject("responsedata");
               name=responce_data.getString("name");
               userid=responce_data.getString("userid");
               email1=responce_data.getString("email");


            }

            catch (Exception e) {
                e.printStackTrace();
            }
           /* new AlertDialog.Builder(Login.this)
                    .setMessage(message)
                    .show();
*/
            if(status==1){
                Intent in = new Intent(getApplicationContext(),Profile.class);
                in.putExtra("name",name);
                in.putExtra("userid",userid);
                in.putExtra("email",email1);
                startActivity(in);
                Toast.makeText(getApplicationContext(), "Welcome  "+name, Toast.LENGTH_SHORT).show();
                finish();
            }else if (status==0){
                email.setError("Either email address or password is incorrect");
                password.setError("Either email address or password is incorrect");
            }

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
