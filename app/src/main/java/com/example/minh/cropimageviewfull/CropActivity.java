package com.example.minh.cropimageviewfull;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CropActivity extends AppCompatActivity implements View.OnClickListener {
    private static String URI_INTENT = "uri_intent";
    private CropImageView mCropView;
    private Button mButtonRotate, mButtonCrop;
    private Bitmap mBitmap;
    int rotate = 1;

    public static Intent getCropActivityIntent(Context context, String uri) {
        Intent intent = new Intent(context, CropActivity.class);
        intent.putExtra(URI_INTENT, uri);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);
        initWidget();
        if (getIntent() != null) {
            String uri_image = getIntent().getStringExtra(URI_INTENT);
            if (uri_image != null) {
                Uri uri = Uri.parse(uri_image);
                try {
                    mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    mBitmap = BitmapFactory.decodeStream(CropActivity.this.openFileInput("cropImage"));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        showImageOnCropIamge();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_rotate:
                if (mBitmap != null) {
                    mCropView.setRotatedDegrees(rotate * 90);
                    rotate++;
//                    mBitmap = rotateBitmap(mBitmap, 90);
//                    mCropView.setImageBitmap(mBitmap);
                }
                break;
            case R.id.button_next:
                cropImage();
                break;
        }
    }

    private void initWidget() {
        mCropView = (CropImageView) findViewById(R.id.crop_view);
        mButtonRotate = (Button) findViewById(R.id.button_rotate);
        mButtonCrop = (Button) findViewById(R.id.button_next);
        mButtonRotate.setOnClickListener(this);
        mButtonCrop.setOnClickListener(this);
    }

    private void showImageOnCropIamge() {
        mCropView.setImageBitmap(mBitmap);
        mCropView.setAspectRatio(1, 1);
    }

    private Bitmap rotateBitmap(Bitmap mBitmap, int angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
    }

    private void cropImage() {
        mCropView.setOnCropImageCompleteListener(new CropImageView.OnCropImageCompleteListener() {
            @Override
            public void onCropImageComplete(CropImageView view, CropImageView.CropResult result) {
            }
        });
        mCropView.getCroppedImageAsync();
        Bitmap cropped = mCropView.getCroppedImage();
//        mImageView.setImageBitmap(cropped);
        createImageFromBitmap(cropped);
        startActivity(new Intent(CropActivity.this, SignatureActivity.class));
    }

    public void createImageFromBitmap(Bitmap bitmap) {
        String fileName = "myImage";//no .png or .jpg needed
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
}
