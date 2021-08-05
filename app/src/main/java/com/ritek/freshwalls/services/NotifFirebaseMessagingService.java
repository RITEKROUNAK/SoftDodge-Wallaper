package com.ritek.freshwalls.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ritek.freshwalls.R;
import com.ritek.freshwalls.manager.PrefManager;
import com.ritek.freshwalls.ui.activities.CategoryActivity;
import com.ritek.freshwalls.ui.activities.GifActivity;
import com.ritek.freshwalls.ui.activities.MainActivity;
import com.ritek.freshwalls.ui.activities.UserActivity;
import com.ritek.freshwalls.ui.activities.VideoActivity;
import com.ritek.freshwalls.ui.activities.WallActivity;


import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NotifFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FCM Service";
    Bitmap bitmap;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData().get("type")==null)
            return;



        String type = remoteMessage.getData().get("type");
        String id = remoteMessage.getData().get("id");
        String title = remoteMessage.getData().get("title");
        String image = remoteMessage.getData().get("image");
        String icon = remoteMessage.getData().get("icon");
        String message = remoteMessage.getData().get("message");

        PrefManager prf = new PrefManager(getApplicationContext());
        if (!prf.getString("notifications").equals("false")) {
            Log.v(TAG,remoteMessage.getData().toString());
            if (type.equals("wallpaper")){
                String wallpaper_kind = remoteMessage.getData().get("wallpaper_kind");
                String wallpaper_title = remoteMessage.getData().get("wallpaper_title");
                String wallpaper_description = remoteMessage.getData().get("wallpaper_description");
                String wallpaper_review = remoteMessage.getData().get("wallpaper_review");
                String wallpaper_premium = remoteMessage.getData().get("wallpaper_premium");
                String wallpaper_color = remoteMessage.getData().get("wallpaper_color");
                String wallpaper_size = remoteMessage.getData().get("wallpaper_size");
                String wallpaper_resolution = remoteMessage.getData().get("wallpaper_resolution");
                String wallpaper_comment = remoteMessage.getData().get("wallpaper_comment");
                String wallpaper_comments = remoteMessage.getData().get("wallpaper_comments");
                String wallpaper_downloads = remoteMessage.getData().get("wallpaper_downloads");
                String wallpaper_views = remoteMessage.getData().get("wallpaper_views");
                String wallpaper_shares = remoteMessage.getData().get("wallpaper_shares");
                String wallpaper_sets = remoteMessage.getData().get("wallpaper_sets");
                String wallpaper_trusted = remoteMessage.getData().get("wallpaper_trusted");
                String wallpaper_user = remoteMessage.getData().get("wallpaper_user");
                String wallpaper_userid = remoteMessage.getData().get("wallpaper_userid");
                String wallpaper_userimage = remoteMessage.getData().get("wallpaper_userimage");
                String wallpaper_type = remoteMessage.getData().get("wallpaper_type");
                String wallpaper_extension = remoteMessage.getData().get("wallpaper_extension");

                String wallpaper_thumbnail = remoteMessage.getData().get("wallpaper_thumbnail");
                String wallpaper_image = remoteMessage.getData().get("wallpaper_image");
                String wallpaper_original = remoteMessage.getData().get("wallpaper_original");
                String wallpaper_created = remoteMessage.getData().get("wallpaper_created");
                String wallpaper_tags = remoteMessage.getData().get("wallpaper_tags");

                sendNotification(
                        id,
                        title,
                        image,
                        icon,
                        message,
                        wallpaper_kind,
                        wallpaper_title,
                        wallpaper_description,
                        wallpaper_review,
                        wallpaper_premium,
                        wallpaper_color,
                        wallpaper_size,
                        wallpaper_resolution,
                        wallpaper_comment,
                        wallpaper_comments,
                        wallpaper_downloads,
                        wallpaper_views,
                        wallpaper_shares,
                        wallpaper_sets,
                        wallpaper_trusted,
                        wallpaper_user,
                        wallpaper_userid,
                        wallpaper_userimage,
                        wallpaper_type,
                        wallpaper_extension,
                        wallpaper_thumbnail,
                        wallpaper_image,
                        wallpaper_original,
                        wallpaper_created,
                        wallpaper_tags
                );
            }else if(type.equals("category")){
                String category_title = remoteMessage.getData().get("title_category");
                String category_image = remoteMessage.getData().get("image_category");

                sendNotificationCategory(
                        id,
                        title,
                        image,
                        icon,
                        message,
                        category_title,
                        category_image);
            }else if(type.equals("user")){
                String name_user = remoteMessage.getData().get("name_user");
                String image_user = remoteMessage.getData().get("image_user");

                sendNotificationUser(
                        id,
                        title,
                        image,
                        icon,
                        message,
                        name_user,
                        image_user);
            }  else if (type.equals("link")){
                String link = remoteMessage.getData().get("link");

                sendNotificationUrl(
                        id,
                        title,
                        image,
                        icon,
                        message,
                        link
                );
            }
        }


    }
    private void sendNotificationCategory(
            String id,
            String title,
            String imageUri,
            String iconUrl,
            String message,
            String category_title,
            String category_image
    ) {


        Bitmap image = getBitmapfromUrl(imageUri);
        Bitmap icon = getBitmapfromUrl(iconUrl);
        Intent intent = new Intent(this, CategoryActivity.class);
        intent.setAction(Long.toString(System.currentTimeMillis()));


        intent.putExtra("id", Integer.parseInt(id));
        intent.putExtra("title",category_title);
        intent.putExtra("image",category_image);
        intent.putExtra("from", "notification");



        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.logo);

        int NOTIFICATION_ID = Integer.parseInt(id);

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        String CHANNEL_ID = "my_channel_01";
        CharSequence name = "my_channel";
        String Description = "This is my channel";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(message);

        if (icon!=null){
            builder.setLargeIcon(icon);

        }else{
            builder.setLargeIcon(largeIcon);
        }
        if (image!=null){
            builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(image));
        }


        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(resultPendingIntent);

        notificationManager.notify(NOTIFICATION_ID, builder.build());


    }
    private void sendNotificationUrl(
            String id,
            String title,
            String imageUri,
            String iconUrl,
            String message,
            String url

    ) {


        Bitmap image = getBitmapfromUrl(imageUri);
        Bitmap icon = getBitmapfromUrl(iconUrl);


        Intent notificationIntent = new Intent(Intent.ACTION_VIEW);
        notificationIntent.setData(Uri.parse(url));
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.logo);

        int NOTIFICATION_ID = Integer.parseInt(id);

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        String CHANNEL_ID = "my_channel_01";
        CharSequence name = "my_channel";
        String Description = "This is my channel";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.logo_r)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(message);

        if (icon!=null){
            builder.setLargeIcon(icon);

        }else{
            builder.setLargeIcon(largeIcon);
        }
        if (image!=null){
            builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(image));
        }


        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(notificationIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(resultPendingIntent);

        notificationManager.notify(NOTIFICATION_ID, builder.build());


    }
    private void sendNotification(
            String id,
            String title,
            String imageUri,
            String iconUrl,
            String message,
            String wallpaper_kind,
            String wallpaper_title,
            String wallpaper_description,
            String wallpaper_review,
            String wallpaper_premium,
            String wallpaper_color,
            String wallpaper_size,
            String wallpaper_resolution,
            String wallpaper_comment,
            String wallpaper_comments,
            String wallpaper_downloads,
            String wallpaper_views,
            String wallpaper_shares,
            String wallpaper_sets,
            String wallpaper_trusted,
            String wallpaper_user,
            String wallpaper_userid,
            String wallpaper_userimage,
            String wallpaper_type,
            String wallpaper_extension,
            String wallpaper_thumbnail,
            String wallpaper_image,
            String wallpaper_original,
            String wallpaper_created,
            String wallpaper_tags
    ) {


        Bitmap image = getBitmapfromUrl(imageUri);
        Bitmap icon = getBitmapfromUrl(iconUrl);




        Intent intent ;

        if (wallpaper_kind.equals("video"))
            intent =new Intent(this, VideoActivity.class);
        else if (wallpaper_kind.equals("gif"))
            intent =new Intent(this, GifActivity.class);
        else
            intent =new Intent(this, WallActivity.class);


        intent.setAction(Long.toString(System.currentTimeMillis()));

        intent.putExtra("id",Integer.valueOf(id));
        intent.putExtra("title",wallpaper_title);
        intent.putExtra("description",wallpaper_description);
        intent.putExtra("color",wallpaper_color);
        intent.putExtra("tags",wallpaper_tags);
        intent.putExtra("kind",wallpaper_kind);
        intent.putExtra("from","from");

        if (wallpaper_premium.equals("true"))
            intent.putExtra("premium",true);
        else
            intent.putExtra("premium",false);

        if (wallpaper_review.equals("true"))
            intent.putExtra("review",true);
        else
            intent.putExtra("review",false);

        if (wallpaper_comment.equals("true"))
            intent.putExtra("comment",true);
        else
            intent.putExtra("comment",false);

        intent.putExtra("size",wallpaper_size);
        intent.putExtra("resolution",wallpaper_resolution);
        intent.putExtra("created",wallpaper_created);
        intent.putExtra("sets",Integer.parseInt(wallpaper_sets));
        intent.putExtra("shares",Integer.parseInt(wallpaper_shares));
        intent.putExtra("views",Integer.parseInt(wallpaper_views));
        intent.putExtra("downloads",Integer.parseInt(wallpaper_downloads));
        intent.putExtra("type",wallpaper_type);
        intent.putExtra("extension",wallpaper_extension);


        intent.putExtra("userid",Integer.parseInt(wallpaper_userid));
        intent.putExtra("username",wallpaper_user);
        intent.putExtra("userimage",wallpaper_userimage);
        intent.putExtra("trusted",wallpaper_trusted);

        intent.putExtra("comments",Integer.parseInt(wallpaper_comments));


        intent.putExtra("original",wallpaper_original);
        intent.putExtra("thumbnail",wallpaper_thumbnail);
        intent.putExtra("image",wallpaper_image);

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.logo);

        int NOTIFICATION_ID = Integer.parseInt(id);

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        String CHANNEL_ID = "my_channel_01";
        CharSequence name = "my_channel";
        String Description = "This is my channel";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(message);

        if (icon!=null){
            builder.setLargeIcon(icon);

        }else{
            builder.setLargeIcon(largeIcon);
        }
        if (image!=null){
            builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(image));
        }


        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(resultPendingIntent);

        notificationManager.notify(NOTIFICATION_ID, builder.build());


    }
    private void sendNotificationUser(
            String id,
            String title,
            String imageUri,
            String iconUrl,
            String message,
            String name_user,
            String image_user
    ) {


        Bitmap image = getBitmapfromUrl(imageUri);
        Bitmap icon = getBitmapfromUrl(iconUrl);
        Intent intent = new Intent(this, UserActivity.class);
        intent.setAction(Long.toString(System.currentTimeMillis()));


        intent.putExtra("id", Integer.parseInt(id));
        intent.putExtra("name",name_user);
        intent.putExtra("image",image_user);
        intent.putExtra("from", "notification");




        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.logo);

        int NOTIFICATION_ID = Integer.parseInt(id);

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        String CHANNEL_ID = "my_channel_01";
        CharSequence name = "my_channel";
        String Description = "This is my channel";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(message);

        if (icon!=null){
            builder.setLargeIcon(icon);

        }else{
            builder.setLargeIcon(largeIcon);
        }
        if (image!=null){
            builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(image));
        }


        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(resultPendingIntent);

        notificationManager.notify(NOTIFICATION_ID, builder.build());

    }
    /*
    *To get a Bitmap image from the URL received
    * */
    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

}