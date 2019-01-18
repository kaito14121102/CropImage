package com.example.minh.cropimageviewfull;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //    compile 'com.theartofdev.edmodo:android-image-cropper:2.5.+'
    //https://techkshare.blogspot.com/2018/01/android-image-cropper.html
    private Button mButtonAddImage;
    public static final int REQUEST_GALLERY_CODE = 1;
    public static final int REQUEST_CAMERA_CODE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        onRequestStoragePermisson();
        initWidget();
        initFolder();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_add_to_image:
                customDialog();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_CODE && grantResults.length > 0 && (grantResults[0] + 1) == PackageManager.PERMISSION_GRANTED) {
            cameraOpen();
        } else {
            Log.d("TAGG", requestCode + ": " + permissions.length + ": " + grantResults[0]);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_GALLERY_CODE && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            startActivity(CropActivity.getCropActivityIntent(this, String.valueOf(uri)));
//            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
//                createImageFromBitmap(bitmap);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            startActivity(new Intent(MainActivity.this, CropActivity.class));
        } else if (requestCode == REQUEST_CAMERA_CODE && resultCode == RESULT_OK && data != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            createImageFromBitmap(bitmap);
            startActivity(new Intent(MainActivity.this, CropActivity.class));
        }
    }

    private void initWidget() {
        mButtonAddImage = (Button) findViewById(R.id.button_add_to_image);
        mButtonAddImage.setOnClickListener(this);
    }

    public void customDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_custom);
        final TextView textCamera = dialog.findViewById(R.id.text_camera);
        final TextView textGallery = dialog.findViewById(R.id.text_gallery);
        textGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                galleryOpen();
            }
        });
        textCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA_CODE);
            }
        });
        dialog.show();
    }

    private void galleryOpen() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "select image from gallery"), REQUEST_GALLERY_CODE);
    }

    private void cameraOpen() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA_CODE);
    }

    public void createImageFromBitmap(Bitmap bitmap) {
        String fileName = "cropImage";//no .png or .jpg needed
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            FileOutputStream fo = openFileOutput(fileName, Context.MODE_PRIVATE);
            fo.write(bytes.toByteArray());
            // remember close file output
            fo.close();
        } catch (Exception e) {
            e.printStackTrace();
            fileName = null;
        }
    }

    private void onRequestStoragePermisson() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    10);
        }
    }

    private void initFolder() {
        final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Folder/";
        File newFolder = new File(dir);
        if (!newFolder.exists()) {
            Log.d("TAGG", dir);
            newFolder.mkdir();
        }
    }
}
