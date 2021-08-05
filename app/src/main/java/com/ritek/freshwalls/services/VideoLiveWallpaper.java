package com.ritek.freshwalls.services;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;

import com.ritek.freshwalls.manager.PrefManager;
import com.ritek.freshwalls.ui.activities.VideoActivity;

import java.io.File;
import java.io.IOException;

public class VideoLiveWallpaper extends WallpaperService {

    //###################### Setting ######################
    public String LOCAL_VIDEO_PATH = "";
    public String LOCAL_VIDEO_NAME = "";
    public String LOCAL_VIDEO = "testvideo.mp4";

    public Engine onCreateEngine() {
        return new VideoWallpaperEngine();
    }


    public static void setToWallPaper(Context context) {
        WallpaperUtil.setToWallPaper(context,
                context.getPackageName()+".services.VideoLiveWallpaper",true);
    }

    class VideoWallpaperEngine extends WallpaperService.Engine {

        private MediaPlayer mMediaPlayer;

        private int mSurfaceWidth;
        private int mSurfaceHeight;
        private int mMovieWidth;
        private int mMovieHeight;
        private float scaleRatio;
        private Surface mSurface;
        private int SETSIZE = 1;


        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();

        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            //否则进入 Home 还 Play
            if (visible) {
                mMediaPlayer.start();
            } else {
                mMediaPlayer.pause();
            }
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setSurface(holder.getSurface());
            try {
                PrefManager prf= new PrefManager(getApplicationContext());
                LOCAL_VIDEO_NAME  = prf.getString("LOCAL_VIDEO_NAME");
                LOCAL_VIDEO_PATH  = prf.getString("LOCAL_VIDEO_PATH");
                if(VideoActivity.videoName == null){
                    File file = new File(LOCAL_VIDEO_PATH+LOCAL_VIDEO_NAME);
                    if(file.exists()){
                        String filePath = LOCAL_VIDEO_PATH+LOCAL_VIDEO_NAME;
                        mMediaPlayer.setDataSource(filePath);
                    }else{
                        AssetManager assetMg = getApplicationContext().getAssets();
                        AssetFileDescriptor fileDescriptor = assetMg.openFd(LOCAL_VIDEO);
                        mMediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(),
                                fileDescriptor.getStartOffset(), fileDescriptor.getLength());
                    }
                } else {
                    String filePath = VideoActivity.videoPath+VideoActivity.videoName;
                    mMediaPlayer.setDataSource(filePath);
                }

                //循环
                mMediaPlayer.setLooping(true);
                mMediaPlayer.setVolume(0, 0);
                mMediaPlayer.prepare();
                mMediaPlayer.start();

            } catch (IOException e) {
                e.printStackTrace();
            }

            /**
             * 播放器异常事件
             */
            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {

                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    // TODO Auto-generated method stub
                    mMediaPlayer.release();
                    return false;
                }
            });


            /**
             * 播放器準備事件
             */
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer mp) {
                    // TODO Auto-generated method stub
                    try {
                        mp.start();
                        uiHandler.sendEmptyMessageDelayed(SETSIZE, 1000);

                    } catch (Exception e) {
                        // TODO: handle exception
                        Log.e("start mediaplayer", e.toString());
                    }

                }
            });

        }


        private Handler uiHandler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if(msg.what == SETSIZE){
                    mMovieHeight = mMediaPlayer.getVideoHeight();
                    mMovieWidth = mMediaPlayer.getVideoWidth();
                }
            };
        };

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);

            mSurfaceWidth = width;
            mSurfaceHeight = height;
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);

        }
    }

}