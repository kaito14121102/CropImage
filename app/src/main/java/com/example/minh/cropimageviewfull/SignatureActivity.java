package com.example.minh.cropimageviewfull;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaopo.flying.sticker.DrawableSticker;
import com.xiaopo.flying.sticker.Sticker;
import com.xiaopo.flying.sticker.StickerView;
import com.xiaopo.flying.sticker.TextSticker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import static com.example.minh.cropimageviewfull.MainActivity.REQUEST_GALLERY_CODE;

public class SignatureActivity extends AppCompatActivity {
    private ImageView mImageView;
    private ImageView mImageView1;
    private StickerView mStickerView;
    private Button mButtonSave;
    private BottomNavigationView mBottomNav;
    private int red, green, blue;
    private String font = "";
    private String TAG = "TAG";
    Bitmap mBitmap;
    Integer mColors[] = {Color.BLACK, Color.RED, Color.BLUE, Color.CYAN, Color.DKGRAY, Color.GRAY
            , Color.GREEN, Color.LTGRAY, Color.MAGENTA, Color.YELLOW};
    Random random = new Random();
    //canvas

    Bitmap mBitmapMaster;
    Canvas mCanvasMaster;
    Paint mPaintDraw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);
        initWidget();
        eventBottomNavigation();
        try {
            mBitmap = BitmapFactory.decodeStream(SignatureActivity.this.openFileInput("myImage"));

            mImageView.setImageBitmap(mBitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_GALLERY_CODE && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Drawable d = new BitmapDrawable(getResources(), bitmap);
            mStickerView.addSticker(new DrawableSticker(d));
        }
    }

    public void intCanvasForDrawWatermark() {
        mPaintDraw = new Paint();

        mBitmapMaster = Bitmap.createBitmap(
                mImageView1.getWidth(),
                mImageView1.getHeight(),
                Bitmap.Config.ARGB_8888);
        mCanvasMaster = new Canvas(mBitmapMaster);
        mCanvasMaster.drawBitmap(mBitmapMaster, 0, 0, null);
        mImageView1.setImageBitmap(mBitmapMaster);
    }

    private void eventBottomNavigation() {
        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_signature:
                        dialogCustomSignature();
                        break;
                    case R.id.action_watermark:
                        dialogCustomWatermark();
//                        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
//                        int with = mImageView.getWidth();
//                        int height = mImageView.getHeight();
//                        mPaintDraw.setAlpha(50);
//
//                        for (int i = 0; i <= with / 60; i++) {
//                            for (int j = 0; j <= height / 60; j++) {
//                                mCanvasMaster.drawBitmap(b, i * 60, j * 60, mPaintDraw);
//                            }
//                        }

                        break;
                    case R.id.action_clear:
                        mImageView1.setImageBitmap(mBitmap);
                        mStickerView.removeAllStickers();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    public void drawWatermark(Bitmap bitmap) {
//        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
        int with = mImageView.getWidth();
        int height = mImageView.getHeight();
        mPaintDraw.setAlpha(50);

        for (int i = 0; i <= with / 60; i++) {
            for (int j = 0; j <= height / 60; j++) {
                mCanvasMaster.drawBitmap(bitmap, i * 60, j * 60, mPaintDraw);
            }
        }
    }

    public void drawTextWaterMark(String text) {
        mPaintDraw.setColor(Color.rgb(red, green, blue)); // Text Color
        mPaintDraw.setTextSize(30); // Text Size
        mPaintDraw.setAlpha(50);
        if (!font.equals("")) {
            Typeface tf = Typeface.createFromAsset(getAssets(), font);
            mPaintDraw.setTypeface(tf);
        }
        mCanvasMaster.rotate(30, 500,
                0);
        int with = mImageView.getWidth();
        int height = mImageView.getHeight();
        with = with / 20;
        height = height / 20;
        for (int i = 0; i <= with; i++) {
            for (int j = 0; j <= height; j++) {
                mCanvasMaster.drawText(text, i * (text.length() * 20), j * 50, mPaintDraw);
            }
        }
        red = 0;
        green = 0;
        blue = 0;
        font = "";
    }

    private void initWidget() {
        mImageView = (ImageView) findViewById(R.id.image);
        mImageView1 = (ImageView) findViewById(R.id.image1);
        mBottomNav = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        mStickerView = (StickerView) findViewById(R.id.stiker_view);
        mButtonSave = (Button) findViewById(R.id.button_save);
        eventSticker();
        eventButtonSave();
    }

    private void eventButtonSave() {
        mButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bitmap;
                mStickerView.buildDrawingCache();
                bitmap = mStickerView.getDrawingCache();
                mStickerView.buildDrawingCache(false);
                saveImage(bitmap);
            }
        });
    }

    public void dialogCustomWatermark() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_custom_watermark);
        final TextView textWatermark = dialog.findViewById(R.id.text_watermark);
        final TextView textUsingText = dialog.findViewById(R.id.text_using_text);
        final TextView textIcon = dialog.findViewById(R.id.text_icon);
        textWatermark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelDialog(dialog);
                dialogDrawSignature(2);
            }
        });
        textUsingText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelDialog(dialog);
                dialogUsingText(2);
            }
        });
        textIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelDialog(dialog);
                dialogIcon();
            }
        });
        dialog.show();
    }

    public void dialogIcon() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_icon);
        final ArrayList<Integer> icons = new ArrayList<>();
        GridView gridIcon = dialog.findViewById(R.id.grid_view);
        icons.add(R.drawable.icon);
        icons.add(R.drawable.icon6);
        icons.add(R.drawable.icon7);
        icons.add(R.drawable.icon8);
        icons.add(R.drawable.icon9);
        icons.add(R.drawable.icon10);
        icons.add(R.drawable.icon11);
        icons.add(R.drawable.icon12);
        icons.add(R.drawable.icon13);
        icons.add(R.drawable.icon14);
        icons.add(R.drawable.icon15);
        icons.add(R.drawable.icon16);
        icons.add(R.drawable.icon17);
        icons.add(R.drawable.icon8);
        icons.add(R.drawable.icon9);
        icons.add(R.drawable.icon10);
        icons.add(R.drawable.icon11);
        icons.add(R.drawable.icon12);
        icons.add(R.drawable.icon13);
        icons.add(R.drawable.icon14);
        icons.add(R.drawable.icon15);
        icons.add(R.drawable.icon16);
        IconApdater adapter = new IconApdater(this, R.layout.item_icon, icons);
        gridIcon.setAdapter(adapter);
        gridIcon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                intCanvasForDrawWatermark();
                Bitmap b = BitmapFactory.decodeResource(getResources(), icons.get(i));
                Bitmap resized = Bitmap.createScaledBitmap(b, (int) (b.getWidth() * 0.5)
                        , (int) (b.getHeight() * 0.5), true);
                cancelDialog(dialog);
                drawWatermark(resized);
            }
        });
        dialog.show();
    }


    //////////////////////////////////////////////////////////////////////////SIGNATURE
    public void dialogCustomSignature() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_signature_custom);
        final TextView textSignature = dialog.findViewById(R.id.text_signature);
        final TextView textUsingText = dialog.findViewById(R.id.text_using_text1);
        final TextView textGallery = dialog.findViewById(R.id.text_gallery);
        textSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelDialog(dialog);
                dialogDrawSignature(1);
            }
        });
        textUsingText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelDialog(dialog);
                dialogUsingText(1);
            }
        });
        textGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelDialog(dialog);
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(intent, "select image from gallery"), REQUEST_GALLERY_CODE);
            }
        });
        dialog.show();
    }

    public void dialogDrawSignature(final int id) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_draw_signature);
        final ImageView imageDraw = dialog.findViewById(R.id.image_draw);
        TextView textColor = dialog.findViewById(R.id.text_color);
        TextView textClear = dialog.findViewById(R.id.text_clear);
        TextView textAdd = dialog.findViewById(R.id.text_add);
        final CanvasView canvas;
        canvas = new CanvasView(this, imageDraw);
        textClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canvas.clearCanavas();
            }
        });
        textColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogColor(canvas.getPaint());
            }
        });
        textAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bitmap = canvas.getBitmap();
                if (bitmap != null) {
                    if (id == 1) {
                        Drawable d = new BitmapDrawable(getResources(), bitmap);
                        mStickerView.addSticker(new DrawableSticker(d));
                        cancelDialog(dialog);
                    } else {
                        Bitmap resized = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * 0.2)
                                , (int) (bitmap.getHeight() * 0.2), true);
                        intCanvasForDrawWatermark();
                        drawWatermark(resized);
                        cancelDialog(dialog);
                    }
                }
            }
        });
        dialog.show();
    }

    public void dialogUsingText(final int id) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_using_text);
        final TextView textPreview = dialog.findViewById(R.id.text_preview);
        final EditText editText = dialog.findViewById(R.id.edit_text);
        TextView textAdd = dialog.findViewById(R.id.text_add);
        TextView textColor = dialog.findViewById(R.id.text_color);
        TextView textFont = dialog.findViewById(R.id.text_font);
        final String font = "";
//        Typeface tf = Typeface.createFromAsset(getAssets(),
//                "AdobeNaskh-Medium.otf");
//        textColor.setTypeface(tf);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textPreview.setText(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        textColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                red = 0;
                blue = 0;
                green = 0;
                dialogColor(textPreview);
            }
        });
        textFont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogFont(textPreview);
            }
        });
        textAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.getText().toString().trim().length() != 0) {
                    if (id == 1) {
                        addText(textPreview.getText().toString());
                        cancelDialog(dialog);
                    } else {
                        intCanvasForDrawWatermark();
                        drawTextWaterMark(textPreview.getText().toString());
                        cancelDialog(dialog);
                    }
                } else {
                    Toast.makeText(SignatureActivity.this, "null", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }

    public void dialogColor(final TextView textPreview) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_color);
        SeekBar seekRed = dialog.findViewById(R.id.seek_red);
        SeekBar seekGreen = dialog.findViewById(R.id.seek_green);
        SeekBar seekBlue = dialog.findViewById(R.id.seek_blue);
        TextView textOk = dialog.findViewById(R.id.text_ok);
        ImageView imageResult = dialog.findViewById(R.id.image_result);
        evenSeekBar(seekRed, seekGreen, seekBlue, imageResult);
        textOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textPreview.setTextColor(Color.rgb(red, green, blue));
                cancelDialog(dialog);
            }
        });
        dialog.show();
    }

    public void dialogColor(final Paint paint) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_color);
        SeekBar seekRed = dialog.findViewById(R.id.seek_red);
        SeekBar seekGreen = dialog.findViewById(R.id.seek_green);
        SeekBar seekBlue = dialog.findViewById(R.id.seek_blue);
        TextView textOk = dialog.findViewById(R.id.text_ok);
        ImageView imageResult = dialog.findViewById(R.id.image_result);
        evenSeekBar(seekRed, seekGreen, seekBlue, imageResult);
        textOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paint.setColor(Color.rgb(red, green, blue));
                red = 0;
                blue = 0;
                green = 0;
                cancelDialog(dialog);
            }
        });
        dialog.show();
    }

    public void dialogFont(final TextView textPreview) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_font);
        ListView listView = dialog.findViewById(R.id.list_view);
        final ArrayList<String> fonts = new ArrayList<>();
        fonts.add("COOPBL.TTF");
        fonts.add("INFROMAN.TTF");
        fonts.add("ITCEDSCR.TTF");
        fonts.add("KUNSTLER.TTF");
        FontAdapter adapter = new FontAdapter(this, android.R.layout.simple_list_item_1, fonts);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Typeface tf = Typeface.createFromAsset(getAssets(), fonts.get(i));
                textPreview.setTypeface(tf);
                font = fonts.get(i);
                cancelDialog(dialog);
            }
        });
        dialog.show();
    }

    public void addText(String text) {
        TextSticker sticker = new TextSticker(this);
        sticker.setDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.sticker_transparent_background));
        sticker.setTextColor(Color.rgb(red, green, blue));
        sticker.setText(text);
        if (!font.equals("")) {
            Typeface tf = Typeface.createFromAsset(getAssets(), font);
            sticker.setTypeface(tf);
        }
        sticker.setTextAlign(Layout.Alignment.ALIGN_CENTER);
        sticker.resizeText();
        mStickerView.addSticker(sticker);
        red = 0;
        green = 0;
        blue = 0;
        font = "";
    }

    private void evenSeekBar(SeekBar seekRed, SeekBar seekGreen, SeekBar seekBlue, final ImageView imageResult) {
        seekRed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                red = i;
                imageResult.setBackgroundColor(Color.rgb(red, green, blue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekGreen.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                green = i;
                imageResult.setBackgroundColor(Color.rgb(red, green, blue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBlue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                blue = i;
                imageResult.setBackgroundColor(Color.rgb(red, green, blue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void eventSticker() {
        mStickerView.setOnStickerOperationListener(new StickerView.OnStickerOperationListener() {
            @Override
            public void onStickerAdded(@NonNull Sticker sticker) {
                Log.d(TAG, "onStickerAdded");
            }

            @Override
            public void onStickerClicked(@NonNull Sticker sticker) {
                //stickerView.removeAllSticker();
                if (sticker instanceof TextSticker) {
                    int a = random.nextInt(9);
                    ((TextSticker) sticker).setTextColor(mColors[a]);
                    mStickerView.replace(sticker);
                    mStickerView.invalidate();
                }
                Log.d(TAG, "onStickerClicked");
            }

            @Override
            public void onStickerDeleted(@NonNull Sticker sticker) {
                Log.d(TAG, "onStickerDeleted");
            }

            @Override
            public void onStickerDragFinished(@NonNull Sticker sticker) {
                Log.d(TAG, "onStickerDragFinished");
            }

            @Override
            public void onStickerZoomFinished(@NonNull Sticker sticker) {
                Log.d(TAG, "onStickerZoomFinished");
            }

            @Override
            public void onStickerFlipped(@NonNull Sticker sticker) {
                Log.d(TAG, "onStickerFlipped");
            }

            @Override
            public void onStickerDoubleTapped(@NonNull Sticker sticker) {
                Log.d(TAG, "onDoubleTapped: double tap will be with two click");
                removeSticker();
            }
        });
    }

    public void removeSticker() {
        if (mStickerView.removeCurrentSticker()) {
            Toast.makeText(SignatureActivity.this, "Remove current Sticker successfully!", Toast.LENGTH_SHORT)
                    .show();
        } else {
            Toast.makeText(SignatureActivity.this, "Remove current Sticker failed!", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    public void replaceSticker() {
//        if (mStickerView.replace(sticker)) {
//            Toast.makeText(MainActivity.this, "Replace Sticker successfully!", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(MainActivity.this, "Replace Sticker failed!", Toast.LENGTH_SHORT).show();
//        }
    }

    public void lockSticker() {
        mStickerView.setLocked(!mStickerView.isLocked());
    }

    public void saveImage(Bitmap bitmap) {
        try {
            FileOutputStream fOut = null;
            String strDirectoy = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Folder/";
            Calendar calender = Calendar.getInstance();
            String imageName = "image" + calender.getTimeInMillis();
            File file = new File(strDirectoy, imageName);
            fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
            MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
            fOut.flush();
            fOut.close();
            Toast.makeText(this, "save success", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cancelDialog(Dialog dialog) {
        dialog.cancel();
        dialog.dismiss();
    }
}
