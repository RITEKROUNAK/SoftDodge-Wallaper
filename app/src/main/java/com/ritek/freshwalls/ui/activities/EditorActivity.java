package com.ritek.freshwalls.ui.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatSeekBar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jackandphantom.blurimage.BlurImage;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.ritek.freshwalls.R;
import com.theartofdev.edmodo.cropper.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import jp.co.cyberagent.android.gpuimage.GPUImageView;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageBoxBlurFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageBrightnessFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageContrastFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageGaussianBlurFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSaturationFilter;
import uk.co.senab.photoview.PhotoView;

public class EditorActivity extends AppCompatActivity {

    private String original;
    private PhotoView photo_view_edit_activity;
    private AppCompatSeekBar seek_bar_editor_activity_blur;
    private AppCompatSeekBar seek_bar_editor_activity_contrast;
    private AppCompatSeekBar seek_bar_editor_activity_brightness;
    private AppCompatSeekBar seek_bar_editor_activity_saturation;

    private TextView text_view_progress_editor_activity_blur;
    private TextView text_view_progress_editor_activity_contrast;
    private TextView text_view_progress_editor_activity_brightness;
    private TextView text_view_progress_editor_activity_saturation;


    private Bitmap TempBitmap = null;
    private Bitmap OriginalBitMap = null;

    private float blurvalue = 0;
    private float contrastvalue = 0;
    private float brightnessValue = 0;
    private float saturationvalue = 0;

    private RelativeLayout relative_layout_editor_activity_contrast;
    private RelativeLayout relative_layout_editor_activity_brightness;
    private RelativeLayout relative_layout_editor_activity_blur;
    private RelativeLayout relative_layout_editor_activity_saturation;

    private ImageView image_view_editor_activity_blur;
    private LinearLayout linear_layout_editor_activity_blur;
    private TextView text_view_editor_activity_blur;

    private ImageView image_view_editor_activity_contrast;
    private LinearLayout linear_layout_editor_activity_contrast;
    private TextView text_view_editor_activity_contrast;

    private ImageView image_view_editor_activity_saturation;
    private LinearLayout linear_layout_editor_activity_saturation;
    private TextView text_view_editor_activity_saturation;

    private ImageView image_view_editor_activity_brightness;
    private LinearLayout linear_layout_editor_activity_brightness;
    private TextView text_view_editor_activity_brightness;
    private Bitmap bitmapEdited;
    private RelativeLayout relative_layout_editor_activity_close;
    private LinearLayout linear_layout_editor_activity_crop;
    private ImageView image_view_editor_activity_check;
    private GPUImageView gpu_image_view_editor_activity;
    private GPUImageBoxBlurFilter gpuImageFilterGroup;
    private GPUImageSaturationFilter gpuImageSaturationFilter;
    private GPUImageContrastFilter gpuImageContrastFilter;
    private GPUImageBrightnessFilter gpuImageBrightnessFilter;
    private GPUImageGaussianBlurFilter gpuImageBoxBlurFilter;
    private LinearLayout linear_layout_editor_activity_done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        Bundle bundle = getIntent().getExtras() ;
        this.original =  bundle.getString("original");
        initGPU();
        initUI();
        initImage();
        initEvents();
    }

    private void initGPU() {
        gpuImageFilterGroup = new GPUImageBoxBlurFilter();
        gpuImageSaturationFilter= new GPUImageSaturationFilter();
        gpuImageContrastFilter= new GPUImageContrastFilter();
        gpuImageBrightnessFilter= new GPUImageBrightnessFilter();
        gpuImageBoxBlurFilter= new GPUImageGaussianBlurFilter();
        gpuImageFilterGroup.addFilter(gpuImageSaturationFilter);
        gpuImageFilterGroup.addFilter(gpuImageContrastFilter);
        gpuImageFilterGroup.addFilter(gpuImageBrightnessFilter);
        gpuImageFilterGroup.addFilter(gpuImageBoxBlurFilter);
    }

    private void initEvents() {
        this.linear_layout_editor_activity_crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectTrayImageAsRec(1);
            }
        });
        linear_layout_editor_activity_blur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSeleced(0);

            }
        });
        linear_layout_editor_activity_contrast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSeleced(2);

            }
        });
        linear_layout_editor_activity_brightness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSeleced(1);

            }
        });
        linear_layout_editor_activity_saturation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSeleced(3);

            }
        });
        relative_layout_editor_activity_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSeleced(19);
            }
        });
        this.linear_layout_editor_activity_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                done();
            }
        });
        this.seek_bar_editor_activity_blur.setOnSeekBarChangeListener(new AppCompatSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    if (TempBitmap!=null){
                        blurvalue =  progress;
                        text_view_progress_editor_activity_blur.setText(progress+" %");
                        //new ApplyEdit().execute();
                        if (blurvalue==0){
                            bitmapEdited = TempBitmap;
                            photo_view_edit_activity.setImageBitmap(bitmapEdited);
                        }else{
                            bitmapEdited = BlurImage.with(getApplicationContext()).load(TempBitmap).intensity(blurvalue).Async(true).getImageBlur();
                            BlurImage.with(getApplicationContext()).load(TempBitmap).intensity(blurvalue).Async(true).into(photo_view_edit_activity);
                        }

                    }
                  //  blurvalue =   (float) ((float) progress)/((float)10);
                   // text_view_progress_editor_activity_blur.setText(progress+" %");
                    //gpuImageBoxBlurFilter.setBlurSize(progress);
                    // gpu_image_view_editor_activity.setFilter(gpuImageFilterGroup);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        this.seek_bar_editor_activity_saturation.setOnSeekBarChangeListener(new AppCompatSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    /*if (TempBitmap!=null){
                        new ApplyEdit().execute();
                    }*/
                    saturationvalue =   (float) ((float) progress)/((float)10);
                    text_view_progress_editor_activity_saturation.setText(progress+" °");
                    gpuImageSaturationFilter.setSaturation(saturationvalue);
                    gpu_image_view_editor_activity.setFilter(gpuImageFilterGroup);

                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        this.seek_bar_editor_activity_contrast.setOnSeekBarChangeListener(new AppCompatSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                   /* if (TempBitmap!=null){
                        contrastvalue =  progress*10;
                        text_view_progress_editor_activity_contrast.setText(contrastvalue+" %");
                        new ApplyEdit().execute();
                    }*/
                    contrastvalue =  (float) ((float) progress)/((float)10);
                    text_view_progress_editor_activity_contrast.setText(progress+" %");
                    gpuImageContrastFilter.setContrast(contrastvalue);
                    gpu_image_view_editor_activity.setFilter(gpuImageFilterGroup);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        this.seek_bar_editor_activity_brightness.setOnSeekBarChangeListener(new AppCompatSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                   /* if (TempBitmap!=null){
                        brightnessValue = progress*10;
                        float p =  ((float)brightnessValue/255)*100;
                        int pourcent =(int) p;
                        if (pourcent>100){
                            pourcent=100;
                        }
                        if (pourcent<-100){
                            pourcent=-100;
                        }
                        text_view_progress_editor_activity_brightness.setText(pourcent+" %");
                        new ApplyEdit().execute();
                    }*/
                    brightnessValue = (float) ((float) progress)/((float)100);
                    text_view_progress_editor_activity_brightness.setText(progress+" %");
                    gpuImageBrightnessFilter.setBrightness(brightnessValue);
                    gpu_image_view_editor_activity.setFilter(gpuImageFilterGroup);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }
    private void initImage() {
        Picasso.with(this).load(original).into(photo_view_edit_activity);
        final Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                gpu_image_view_editor_activity.setImage(bitmap);
                TempBitmap = bitmap;
                OriginalBitMap= bitmap;
                bitmapEdited=bitmap;
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        Picasso.with(getApplicationContext()).load(original).error(R.drawable.placeholder).placeholder(R.drawable.placeholder).into(target);
        photo_view_edit_activity.setTag(target);
    }
    private void initUI() {
        this.gpu_image_view_editor_activity =  findViewById(R.id.gpu_image_view_editor_activity);
        this.image_view_editor_activity_check =  findViewById(R.id.image_view_editor_activity_check);
        this.photo_view_edit_activity =  findViewById(R.id.photo_view_edit_activity);
        this.seek_bar_editor_activity_brightness =  findViewById(R.id.seek_bar_editor_activity_brightness);
        this.seek_bar_editor_activity_blur =  findViewById(R.id.seek_bar_editor_activity_blur);
        this.seek_bar_editor_activity_contrast =  findViewById(R.id.seek_bar_editor_activity_contrast);
        this.seek_bar_editor_activity_saturation =  findViewById(R.id.seek_bar_editor_activity_saturation);
        this.relative_layout_editor_activity_contrast =  findViewById(R.id.relative_layout_editor_activity_contrast);
        this.relative_layout_editor_activity_brightness =  findViewById(R.id.relative_layout_editor_activity_brightness);
        this.relative_layout_editor_activity_blur =  findViewById(R.id.relative_layout_editor_activity_blur);
        this.relative_layout_editor_activity_saturation =  findViewById(R.id.relative_layout_editor_activity_saturation);
        this.text_view_editor_activity_blur =  findViewById(R.id.text_view_editor_activity_blur);
        this.image_view_editor_activity_blur =  findViewById(R.id.image_view_editor_activity_blur);
        this.linear_layout_editor_activity_blur =  findViewById(R.id.linear_layout_editor_activity_blur);
        this.text_view_editor_activity_contrast =  findViewById(R.id.text_view_editor_activity_contrast);
        this.image_view_editor_activity_contrast =  findViewById(R.id.image_view_editor_activity_contrast);
        this.linear_layout_editor_activity_contrast =  findViewById(R.id.linear_layout_editor_activity_contrast);
        this.linear_layout_editor_activity_saturation =  findViewById(R.id.linear_layout_editor_activity_saturation);

        this.text_view_editor_activity_brightness =  findViewById(R.id.text_view_editor_activity_brightness);
        this.image_view_editor_activity_brightness =  findViewById(R.id.image_view_editor_activity_brightness);
        this.linear_layout_editor_activity_brightness =  findViewById(R.id.linear_layout_editor_activity_brightness);
        this.linear_layout_editor_activity_saturation =  findViewById(R.id.linear_layout_editor_activity_saturation);

        this.text_view_progress_editor_activity_brightness =  findViewById(R.id.text_view_progress_editor_activity_brightness);
        this.text_view_progress_editor_activity_blur=  findViewById(R.id.text_view_progress_editor_activity_blur);
        this.text_view_progress_editor_activity_contrast =  findViewById(R.id.text_view_progress_editor_activity_contrast);

        this.text_view_editor_activity_saturation =  findViewById(R.id.text_view_editor_activity_saturation);
        this.text_view_progress_editor_activity_saturation =  findViewById(R.id.text_view_progress_editor_activity_saturation);
        this.image_view_editor_activity_saturation =  findViewById(R.id.image_view_editor_activity_saturation);
        this.relative_layout_editor_activity_close =  findViewById(R.id.relative_layout_editor_activity_close);
        this.linear_layout_editor_activity_crop =  findViewById(R.id.linear_layout_editor_activity_crop);
        this.linear_layout_editor_activity_done =  findViewById(R.id.linear_layout_editor_activity_done);

    }
    public void setSeleced(int i){
        image_view_editor_activity_blur.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.primary_text), android.graphics.PorterDuff.Mode.SRC_IN);
        text_view_editor_activity_blur.setTextColor(getResources().getColor(R.color.primary_text));

        image_view_editor_activity_contrast.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.primary_text), android.graphics.PorterDuff.Mode.SRC_IN);
        text_view_editor_activity_contrast.setTextColor(getResources().getColor(R.color.primary_text));

        image_view_editor_activity_brightness.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.primary_text), android.graphics.PorterDuff.Mode.SRC_IN);
        text_view_editor_activity_brightness.setTextColor(getResources().getColor(R.color.primary_text));

        image_view_editor_activity_saturation.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.primary_text), android.graphics.PorterDuff.Mode.SRC_IN);
        text_view_editor_activity_saturation.setTextColor(getResources().getColor(R.color.primary_text));

        relative_layout_editor_activity_brightness.setVisibility(View.GONE);
        relative_layout_editor_activity_contrast.setVisibility(View.GONE);
        relative_layout_editor_activity_blur.setVisibility(View.GONE);
        relative_layout_editor_activity_saturation.setVisibility(View.GONE);
        relative_layout_editor_activity_close.setVisibility(View.GONE);

        switch (i){
            case 0:
                image_view_editor_activity_blur.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN);
                text_view_editor_activity_blur.setTextColor(getResources().getColor(R.color.colorAccent));
                relative_layout_editor_activity_blur.setVisibility(View.VISIBLE);
                relative_layout_editor_activity_close.setVisibility(View.VISIBLE);
                break;
            case 1:
                image_view_editor_activity_brightness.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN);
                text_view_editor_activity_brightness.setTextColor(getResources().getColor(R.color.colorAccent));
                relative_layout_editor_activity_brightness.setVisibility(View.VISIBLE);
                relative_layout_editor_activity_close.setVisibility(View.VISIBLE);

                break;
            case 2:
                image_view_editor_activity_contrast.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN);
                text_view_editor_activity_contrast.setTextColor(getResources().getColor(R.color.colorAccent));
                relative_layout_editor_activity_contrast.setVisibility(View.VISIBLE);
                relative_layout_editor_activity_close.setVisibility(View.VISIBLE);

                break;
            case 3:
                image_view_editor_activity_saturation.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN);
                text_view_editor_activity_saturation.setTextColor(getResources().getColor(R.color.colorAccent));
                relative_layout_editor_activity_saturation.setVisibility(View.VISIBLE);
                relative_layout_editor_activity_close.setVisibility(View.VISIBLE);
                break;
        }

    }
    public void done(){

            Bitmap bitmap = bitmapEdited;

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            Intent intent=new Intent();
            intent.putExtra("result",byteArray);
            setResult(RESULT_OK, intent);
            finish();

    }
    public void SelectTrayImageAsRec(int requestId) {
        if (ContextCompat.checkSelfPermission(EditorActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(EditorActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        } else {
            String path = Environment.getExternalStorageDirectory().toString();
            OutputStream fOut = null;
            Integer counter = 0;
            File file = new File(path, "FitnessGirl.jpg"); // the File to save , append increasing numeric counter to prevent files from getting overwritten.
            try {
                fOut = new FileOutputStream(file);


                Bitmap pictureBitmap =OriginalBitMap; // obtaining the Bitmap
                pictureBitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
                fOut.flush(); // Not really required
                fOut.close(); // do not forget to close the stream
                MediaStore.Images.Media.insertImage(getContentResolver(),file.getAbsolutePath(),file.getName(),file.getName());

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Uri imageUri = FileProvider.getUriForFile(EditorActivity.this, EditorActivity.this.getApplicationContext().getPackageName() + ".provider", file);
            CropImage.ActivityBuilder cropImge = CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setActivityTitle("Crop Your Wallpaper")
                    .setGuidelinesColor(R.color.black)
                    .setAllowFlipping(true)
                    .setScaleType(CropImageView.ScaleType.FIT_CENTER)
                    .setCropShape(CropImageView.CropShape.RECTANGLE)
                    .setActivityMenuIconColor(R.color.black)
                    .setCropMenuCropButtonIcon(R.drawable.ic_check);
            Intent intent = cropImge.getIntent(EditorActivity.this);
            startActivityForResult(intent, requestId);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                TempBitmap = bitmap;
//                new ApplyEdit().execute("");
  //              photo_view_edit_activity.setImageBitmap(TempBitmap);
                if (blurvalue==0){
                    bitmapEdited = TempBitmap;
                    photo_view_edit_activity.setImageBitmap(bitmapEdited);
                }else{
                    bitmapEdited = BlurImage.with(getApplicationContext()).load(TempBitmap).intensity(blurvalue).Async(true).getImageBlur();
                    BlurImage.with(getApplicationContext()).load(TempBitmap).intensity(blurvalue).Async(true).into(photo_view_edit_activity);
                }


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
    private class ApplyEdit extends AsyncTask<String, Bitmap, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                 /*   bitmapEdited = TempBitmap;
                    if (contrastvalue!=0)
                        bitmapEdited =adjustedContrast(bitmapEdited,contrastvalue);
                    if (blurvalue!=0)
                        bitmapEdited =fastblur(bitmapEdited,1,blurvalue);
                    if (brightnessValue!=0)
                        bitmapEdited = doBrightness(bitmapEdited,brightnessValue);
                    if (saturationvalue!=0)
                        bitmapEdited = hue(bitmapEdited,saturationvalue);*/
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "Executed";
        }
        @Override
        protected void onPostExecute(String result) {
            if (contrastvalue==0   && brightnessValue ==0 && blurvalue == 0  && saturationvalue==0){
                photo_view_edit_activity.setImageBitmap(TempBitmap);
            }else{
                photo_view_edit_activity.setImageBitmap(bitmapEdited);
            }
        }
        @Override
        protected void onPreExecute() { }
        @Override
        protected void onProgressUpdate(Bitmap... values) { }
    }
    public Bitmap fastblur(Bitmap sentBitmap, float scale, int radius) {
        try {
            int width = Math.round(sentBitmap.getWidth() * scale);
            int height = Math.round(sentBitmap.getHeight() * scale);
            sentBitmap = Bitmap.createScaledBitmap(sentBitmap, width, height, false);

            Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

            if (radius < 1) {
                return (null);
            }

            int w = bitmap.getWidth();
            int h = bitmap.getHeight();

            int[] pix = new int[w * h];
            Log.e("pix", w + " " + h + " " + pix.length);
            bitmap.getPixels(pix, 0, w, 0, 0, w, h);

            int wm = w - 1;
            int hm = h - 1;
            int wh = w * h;
            int div = radius + radius + 1;

            int r[] = new int[wh];
            int g[] = new int[wh];
            int b[] = new int[wh];
            int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
            int vmin[] = new int[Math.max(w, h)];

            int divsum = (div + 1) >> 1;
            divsum *= divsum;
            int dv[] = new int[256 * divsum];
            for (i = 0; i < 256 * divsum; i++) {
                dv[i] = (i / divsum);
            }

            yw = yi = 0;

            int[][] stack = new int[div][3];
            int stackpointer;
            int stackstart;
            int[] sir;
            int rbs;
            int r1 = radius + 1;
            int routsum, goutsum, boutsum;
            int rinsum, ginsum, binsum;

            for (y = 0; y < h; y++) {
                rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
                for (i = -radius; i <= radius; i++) {
                    p = pix[yi + Math.min(wm, Math.max(i, 0))];
                    sir = stack[i + radius];
                    sir[0] = (p & 0xff0000) >> 16;
                    sir[1] = (p & 0x00ff00) >> 8;
                    sir[2] = (p & 0x0000ff);
                    rbs = r1 - Math.abs(i);
                    rsum += sir[0] * rbs;
                    gsum += sir[1] * rbs;
                    bsum += sir[2] * rbs;
                    if (i > 0) {
                        rinsum += sir[0];
                        ginsum += sir[1];
                        binsum += sir[2];
                    } else {
                        routsum += sir[0];
                        goutsum += sir[1];
                        boutsum += sir[2];
                    }
                }
                stackpointer = radius;

                for (x = 0; x < w; x++) {

                    r[yi] = dv[rsum];
                    g[yi] = dv[gsum];
                    b[yi] = dv[bsum];

                    rsum -= routsum;
                    gsum -= goutsum;
                    bsum -= boutsum;

                    stackstart = stackpointer - radius + div;
                    sir = stack[stackstart % div];

                    routsum -= sir[0];
                    goutsum -= sir[1];
                    boutsum -= sir[2];

                    if (y == 0) {
                        vmin[x] = Math.min(x + radius + 1, wm);
                    }
                    p = pix[yw + vmin[x]];

                    sir[0] = (p & 0xff0000) >> 16;
                    sir[1] = (p & 0x00ff00) >> 8;
                    sir[2] = (p & 0x0000ff);

                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];

                    rsum += rinsum;
                    gsum += ginsum;
                    bsum += binsum;

                    stackpointer = (stackpointer + 1) % div;
                    sir = stack[(stackpointer) % div];

                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];

                    rinsum -= sir[0];
                    ginsum -= sir[1];
                    binsum -= sir[2];

                    yi++;
                }
                yw += w;
            }
            for (x = 0; x < w; x++) {
                rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
                yp = -radius * w;
                for (i = -radius; i <= radius; i++) {
                    yi = Math.max(0, yp) + x;

                    sir = stack[i + radius];

                    sir[0] = r[yi];
                    sir[1] = g[yi];
                    sir[2] = b[yi];

                    rbs = r1 - Math.abs(i);

                    rsum += r[yi] * rbs;
                    gsum += g[yi] * rbs;
                    bsum += b[yi] * rbs;

                    if (i > 0) {
                        rinsum += sir[0];
                        ginsum += sir[1];
                        binsum += sir[2];
                    } else {
                        routsum += sir[0];
                        goutsum += sir[1];
                        boutsum += sir[2];
                    }

                    if (i < hm) {
                        yp += w;
                    }
                }
                yi = x;
                stackpointer = radius;
                for (y = 0; y < h; y++) {
                    // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                    pix[yi] = ( 0xff000000 & pix[yi] ) | ( dv[rsum] << 16 ) | ( dv[gsum] << 8 ) | dv[bsum];

                    rsum -= routsum;
                    gsum -= goutsum;
                    bsum -= boutsum;

                    stackstart = stackpointer - radius + div;
                    sir = stack[stackstart % div];

                    routsum -= sir[0];
                    goutsum -= sir[1];
                    boutsum -= sir[2];

                    if (x == 0) {
                        vmin[y] = Math.min(y + r1, hm) * w;
                    }
                    p = x + vmin[y];

                    sir[0] = r[p];
                    sir[1] = g[p];
                    sir[2] = b[p];

                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];

                    rsum += rinsum;
                    gsum += ginsum;
                    bsum += binsum;

                    stackpointer = (stackpointer + 1) % div;
                    sir = stack[stackpointer];

                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];

                    rinsum -= sir[0];
                    ginsum -= sir[1];
                    binsum -= sir[2];

                    yi += w;
                }
            }

            Log.e("pix", w + " " + h + " " + pix.length);
            bitmap.setPixels(pix, 0, w, 0, 0, w, h);
            return (bitmap);

        }catch (OutOfMemoryError outOfMemoryError){
            Bitmap bitmap=((BitmapDrawable) photo_view_edit_activity.getDrawable()).getBitmap();
            return fastblur(bitmap,scale,radius);
        }
    }
    private Bitmap adjustedContrast(Bitmap src, double value)
    {
        try {
            // image size
            int width = src.getWidth();
            int height = src.getHeight();
            // create output bitmap

            // create a mutable empty bitmap
            Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());

            // create a canvas so that we can draw the bmOut Bitmap from source bitmap
            Canvas c = new Canvas();
            c.setBitmap(bmOut);

            // draw bitmap to bmOut from src bitmap so we can modify it
            c.drawBitmap(src, 0, 0, new Paint(Color.BLACK));


            // color information
            int A, R, G, B;
            int pixel;
            // get contrast value
            double contrast = Math.pow((100 + value) / 100, 2);

            // scan through all pixels
            for(int x = 0; x < width; ++x) {
                for(int y = 0; y < height; ++y) {
                    // get pixel color
                    pixel = src.getPixel(x, y);
                    A = Color.alpha(pixel);
                    // apply filter contrast for every channel R, G, B
                    R = Color.red(pixel);
                    R = (int)(((((R / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                    if(R < 0) { R = 0; }
                    else if(R > 255) { R = 255; }

                    G = Color.green(pixel);
                    G = (int)(((((G / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                    if(G < 0) { G = 0; }
                    else if(G > 255) { G = 255; }

                    B = Color.blue(pixel);
                    B = (int)(((((B / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                    if(B < 0) { B = 0; }
                    else if(B > 255) { B = 255; }

                    // set new pixel color to output bitmap
                    bmOut.setPixel(x, y, Color.argb(A, R, G, B));
                }
            }
            return bmOut;
        }catch (OutOfMemoryError outOfMemoryError){
            Bitmap bitmap=((BitmapDrawable) photo_view_edit_activity.getDrawable()).getBitmap();
            return adjustedContrast(bitmap,value);
        }
    }

    public  Bitmap doBrightness(Bitmap src, int value)
    {
        try{
            // image size
            int width = src.getWidth();
            int height = src.getHeight();
            // create output bitmap
            Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
            // color information
            int A, R, G, B;
            int pixel;

            // scan through all pixels
            for (int x = 0; x < width; ++x)
            {
                for (int y = 0; y < height; ++y)
                {
                    // get pixel color
                    pixel = src.getPixel(x, y);
                    A = Color.alpha(pixel);
                    R = Color.red(pixel);
                    G = Color.green(pixel);
                    B = Color.blue(pixel);

                    // increase/decrease each channel
                    R += value;
                    if (R > 255)
                    {
                        R = 255;
                    } else if (R < 0)
                    {
                        R = 0;
                    }

                    G += value;
                    if (G > 255)
                    {
                        G = 255;
                    } else if (G < 0)
                    {
                        G = 0;
                    }

                    B += value;
                    if (B > 255)
                    {
                        B = 255;
                    } else if (B < 0)
                    {
                        B = 0;
                    }

                    // apply new pixel color to output bitmap
                    bmOut.setPixel(x, y, Color.argb(A, R, G, B));
                }
            }

            // return final image
            return bmOut;
        }catch (OutOfMemoryError outOfMemoryError){
            Bitmap bitmap=((BitmapDrawable) photo_view_edit_activity.getDrawable()).getBitmap();
            return doBrightness(bitmap,value);
        }
    }
    public  Bitmap hue(Bitmap bitmaps, float hue) {
        try{
            Bitmap newsBitmap = bitmaps.copy(bitmaps.getConfig(), true);
            final int width = newsBitmap.getWidth();
            final int height = newsBitmap.getHeight();
            float [] hsv = new float[3];

            for(int y = 0; y < height; y++){
                for(int x = 0; x < width; x++){
                    int pixel = newsBitmap.getPixel(x,y);
                    Color.colorToHSV(pixel,hsv);
                    hsv[0] = hue;
                    newsBitmap.setPixel(x,y,Color.HSVToColor(Color.alpha(pixel),hsv));
                }
            }

            return newsBitmap;
        }catch (OutOfMemoryError outOfMemoryError){
            Bitmap bitmapns=((BitmapDrawable) photo_view_edit_activity.getDrawable()).getBitmap();
            return hue(bitmapns,hue);
        }
    }
}
