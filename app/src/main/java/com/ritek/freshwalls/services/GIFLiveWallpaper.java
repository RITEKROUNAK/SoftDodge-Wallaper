package com.ritek.freshwalls.services;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.SurfaceHolder;

import com.ritek.freshwalls.manager.PrefManager;
import com.ritek.freshwalls.ui.activities.GifActivity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class GIFLiveWallpaper extends WallpaperService {

    //###################### Setting ######################
    public String LOCAL_GIF_PATH = "";
    public String LOCAL_GIF_NAME = "";
    private static String LOCAL_GIF = "testgif.gif";


    public static void setToWallPaper(Context context) {

        WallpaperUtil.setToWallPaper(context,
                context.getPackageName()+".services.GIFLiveWallpaper",true);

    }

    public Engine onCreateEngine() {

        return new GIFWallpaperEngine();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private class GIFWallpaperEngine extends WallpaperService.Engine {

        private final int frameDuration = 0;

        private SurfaceHolder holder;
        private Movie movie;
        private boolean visible;
        private Handler handler;
        private int mSurfaceWidth;
        private int mSurfaceHeight;
        private int mMovieWidth;
        private int mMovieHeight;
        private float scaleRatio;
        private float scaleRatioW;
        private float scaleRatioH;
        private float y;
        private float x;
        private volatile boolean mIsSurfaceCreated;

        public GIFWallpaperEngine() {
            handler = new Handler();
        }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            this.holder = surfaceHolder;
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            handler.removeCallbacks(drawGIF);
        }



        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);



            try {
                PrefManager prf= new PrefManager(getApplicationContext());
                LOCAL_GIF_NAME  = prf.getString("LOCAL_GIF_NAME");
                LOCAL_GIF_PATH  = prf.getString("LOCAL_GIF_PATH");
                Log.d("GIF", LOCAL_GIF_PATH+LOCAL_GIF_NAME);

                if(GifActivity.gifName == null){
                    File file = new File(LOCAL_GIF_PATH+LOCAL_GIF_NAME);
                    Log.d("GIF", "gifName  nulll");

                    if(file.exists()){
                        Log.d("GIF", "file exist ");

                        final int readLimit = 16 * 1024;
                        if(file != null){
                            Log.d("GIF", "file not null ");

                            InputStream mInputStream =  new BufferedInputStream(new FileInputStream(file), readLimit);
                            mInputStream.mark(readLimit);
                            Movie video = Movie.decodeStream(mInputStream);
                            movie = video;
                        } else {
                            Movie video = Movie.decodeStream(
                                    getResources().getAssets().open(LOCAL_GIF));
                            movie = video;
                            Log.d("GIF", "file null ");

                        }

                    }else {
                        Log.d("GIF", "file not exist ");

                        Movie video = Movie.decodeStream(
                                getResources().getAssets().open(LOCAL_GIF));
                        movie = video;
                    }
                }
                else {
                    Log.d("GIF", "imageFile exist ");

                    File imageFile =  new File(GifActivity.gifPath, GifActivity.gifName);
                    final int readLimit = 16 * 1024;
                    if(imageFile != null){
                        InputStream mInputStream =  new BufferedInputStream(new FileInputStream(imageFile), readLimit);
                        mInputStream.mark(readLimit);
                        Movie video = Movie.decodeStream(mInputStream);
                        movie = video;
                    } else {
                        Movie video = Movie.decodeStream(
                                getResources().getAssets().open(LOCAL_GIF));
                        movie = video;
                    }
                }
            }catch(IOException e){


                try {
                    Movie video = null;
                    video = Movie.decodeStream(
                            getResources().getAssets().open(LOCAL_GIF));
                    movie = video;
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);

            super.onSurfaceChanged(holder, format, width, height);
            mSurfaceWidth = width;
            mSurfaceHeight = height;
            mMovieWidth = movie.width();
            mMovieHeight = movie.height();
            if((float)mSurfaceWidth/mMovieWidth > (float)mSurfaceHeight/mMovieHeight){
                scaleRatio = (float) mSurfaceWidth/mMovieWidth;
            }
            else {
                scaleRatio = (float) mSurfaceHeight/mMovieHeight;
            }
            scaleRatioW = (float) mSurfaceWidth/mMovieWidth;
            scaleRatioH = (float) mSurfaceHeight/mMovieHeight;
            this.x =        (mSurfaceWidth - (mMovieWidth*scaleRatio))/2;
            this.y =      (mSurfaceHeight - (mMovieHeight*scaleRatio))/2;
            mIsSurfaceCreated = true;
            x= x/scaleRatio;
            y=y/scaleRatio;
        }


        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            mIsSurfaceCreated = false;

        }

        private Runnable drawGIF = new Runnable() {
            public void run() {
                draw();
            }
        };

        private void draw() {
            if (visible) {
                Canvas canvas = holder.lockCanvas();
                canvas.save();
                // Adjust size and position so that
                // the image looks good on your screen
                canvas.scale(scaleRatio,scaleRatio);
                movie.draw(canvas,x,y);
                canvas.restore();
                holder.unlockCanvasAndPost(canvas);
                movie.setTime((int) (System.currentTimeMillis() % movie.duration()));
                handler.removeCallbacks(drawGIF);
                handler.postDelayed(drawGIF, frameDuration);
            }
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            this.visible = visible;
            if (visible) {
                handler.post(drawGIF);
            } else {
                handler.removeCallbacks(drawGIF);
            }
        }

    }


}