package com.darkbears.webservice.Activities;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.darkbears.webservice.R;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.zip.Inflater;


public class Profile extends AppCompatActivity {
    TextView details;
    ImageView dp;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initialize();
        initonclick();

        String name = getIntent().getStringExtra("name");
        String userid = getIntent().getStringExtra("userid");
        String email = getIntent().getStringExtra("email");

        details.setText("UserID: " + userid + "\n" + "Name: " + name + "\n" + "Email: " + email);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void initialize() {
        details = (TextView) findViewById(R.id.textView);
        dp = (ImageView) findViewById(R.id.imageView);
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
        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(Profile.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builderSingle.setIcon(R.drawable.upload);
        LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflator.inflate(R.layout.dialoge, null, false);
        ImageView gal = (ImageView) view.findViewById(R.id.imageView3);
        ImageView cam = (ImageView) view.findViewById(R.id.imageView4);
        builderSingle.setView(view);
        builderSingle.setTitle("Choose Your Option");
        final AlertDialog dialog = builderSingle.create();
        dialog.show();

        gal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 100);
                dialog.cancel();

            }


        });

        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent photoPickerIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    photoPickerIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getTempFile(Profile.this)) );
                    startActivityForResult(photoPickerIntent, 101);
                } catch (Exception e) {
                    String errorMessage = "SD full!!!";
                   Toast toast = Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT);
                   toast.show();
                }


                dialog.cancel();

            }
        });


    }


    private File getTempFile(Context context){
        //it will return /sdcard/image.tmp
        final File path = new File( Environment.getExternalStorageDirectory(), context.getPackageName() );
        if(!path.exists()){
            path.mkdir();
        }
        return new File(path, "image.tmp");
    }


    private Intent getCropIntent(Intent intent) {
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);
        return intent;

    }

    private void getCropIntentforcamera(Uri picUri) {
        try {
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(picUri, "image/*");
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 200);
            intent.putExtra("outputY", 200);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, 102);
        } catch (ActivityNotFoundException anfe) {
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            Bitmap yourSelectedImage = null;
            Uri selectedImage=null;

           // System.out.println("uri"+selectedImage);
           // Uri myuri=Uri.parse("content://media/external/images/media/191028");
            if (requestCode == 101) {
                final File file = getTempFile(this);
                getCropIntentforcamera(Uri.fromFile(file));
                //saveimage(yourSelectedImage,0);
            }else{
              selectedImage = data.getData();
            }
            if (requestCode == 100) {
                getCropIntentforcamera(data.getData());
                // saveimage(yourSelectedImage,1);
            }

            try {

                if (selectedImage != null) {
                    InputStream imageStream = getContentResolver()
                            .openInputStream(selectedImage);

                    //yourSelectedImage = BitmapFactory
                    //.decodeStream(imageStream);
                    yourSelectedImage = BitmapFactory.decodeFile(selectedImage.getPath());

                }
                if (yourSelectedImage == null) {
                    Bundle extra = data.getExtras();
                    if (extra != null) {
                        yourSelectedImage = (Bitmap) extra.get("data");
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        yourSelectedImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    }
                }
                if (requestCode == 102) {
                    dp.setImageBitmap(yourSelectedImage);
                }

            } catch (Exception e) {
                // TODO: handle exception
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }



}
