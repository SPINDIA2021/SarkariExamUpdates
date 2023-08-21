package com.gsbussiness.sarkariexamupdates.AppInit;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.gsbussiness.sarkariexamupdates.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;


public class MyNotificationManager {

    public static final int ID_BIG_NOTIFICATION = getNotificationId();
    public static final int ID_SMALL_NOTIFICATION = getNotificationId();

    private Context mCtx;

    public MyNotificationManager(Context mCtx) {
        this.mCtx = mCtx;
    }

    //the method will show a big notification with an image
    //parameters are title for message title, message for message text, url of the big image and an intent that will open
    //when you will tap on the notification
    public void showBigNotification(String title, String message, String url, Intent intent) {
        new sendNotification(mCtx, intent).execute(title, message, url);
    }

    //the method will show a small notification
    //parameters are title for message title, message for message text and an intent that will open
    //when you will tap on the notification
    public void showSmallNotification(String title, String message, Intent intent,Context context) {

        Notification notification;
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addNextIntentWithParentStack(intent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "notification");

            notification = mBuilder.setSmallIcon(R.mipmap.ic_launcher).setTicker(title).setWhen(System.currentTimeMillis())
                    .setAutoCancel(true)
                    .setContentIntent(resultPendingIntent)
                    .setContentTitle(title)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setContentText(message)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(message))
                    .build();
        } else {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "notification");

            notification = mBuilder.setSmallIcon(R.mipmap.ic_launcher).setTicker(title).setWhen(System.currentTimeMillis())
                    .setAutoCancel(true)
                    .setContentTitle(title)

                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setContentText(message)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(message))
                    .build();

        }


        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(ID_SMALL_NOTIFICATION, notification);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel(notificationManager);
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void createChannel(NotificationManager notificationManager) {
        String name = "notification";
        String description = "Notifications for download status";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        NotificationChannel mChannel = new NotificationChannel("notification", name, importance);
        mChannel.setDescription(description);
        mChannel.enableLights(true);
        mChannel.setLightColor(Color.BLUE);
        notificationManager.createNotificationChannel(mChannel);
    }

    private Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);

            Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            return image;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private class sendNotification extends AsyncTask<String, Void, Bitmap> {

        Context ctx;
        String message, title;
        Intent intent;

        public sendNotification(Context context, Intent intent) {
            super();
            this.ctx = context;
            this.intent = intent;
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            InputStream in;
            title = params[0];
            message = params[1];
            try {

                URL url = new URL(params[2]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                in = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(in);
                return myBitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            try {
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);
                stackBuilder.addNextIntentWithParentStack(intent);
                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT);
                NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
                bigPictureStyle.setBigContentTitle(title);
                bigPictureStyle.bigPicture(result);

                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mCtx, "notification");
                Notification notification;
                notification = mBuilder.setSmallIcon(R.mipmap.ic_launcher).setTicker(title).setWhen(System.currentTimeMillis())
                        .setAutoCancel(true)
                        .setContentIntent(resultPendingIntent)
                        .setContentTitle(title)
                        .setStyle(bigPictureStyle)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(mCtx.getResources(), R.mipmap.ic_launcher))
                        .setContentText(message)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .build();

                notification.flags |= Notification.FLAG_AUTO_CANCEL;

                NotificationManager notificationManager = (NotificationManager) mCtx.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(ID_BIG_NOTIFICATION, notification);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    createChannel(notificationManager);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private static int getNotificationId() {
        Random rnd = new Random();
        return 100 + rnd.nextInt(9000);
    }
}