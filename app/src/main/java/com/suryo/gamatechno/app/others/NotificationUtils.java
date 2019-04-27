package com.suryo.gamatechno.app.others;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.RemoteInput;

import com.suryo.gamatechno.app.Main;
import com.suryo.gamatechno.app.R;
import com.suryo.gamatechno.app.behaviour.NotificationReceiver;
import com.suryo.gamatechno.app.model.MyNotification;
import com.suryo.gamatechno.app.page.PageMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Objects;

/**
 * Created by Abhi on 13 Nov 2017 013.
 */

public class NotificationUtils {
    private static final String CHANNEL_ID = "com.wilmar.fieldinspection.notification.channel";
    private static final String GROUP_KEY = "com.wilmar.fieldinspection.notification.group";
    public static final String REPLY_KEY = "com.wilmar.fieldinspection.notification.reply";
    private Context context;

    NotificationUtils(Context context) {
        this.context = context;
    }

    void displayNotification(List<MyNotification> myNotification, String contentText, boolean isSound) {
        final int bigIcon = R.drawable.ic_launcher_background;
        final int smallIcon = getNotificationIcon();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, context.getResources().getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH);
            Objects.requireNonNull(notificationManager).createNotificationChannel(mChannel);
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            int idNotif = Constants.NotifId.FG_NOTIF + 1;
            int reqDetail = Constants.ReqPendingIntent.NOTIF_SUB,
                    reqReply = Constants.ReqPendingIntent.NOTIF_REPLY,
                    reqMarkRead = Constants.ReqPendingIntent.NOTIF_MARK;

            for (int i = 0; i < myNotification.size() - 1; i++) {
                /*------------------ details ----------------- */
                Intent intent = new Intent(context, PageMessage.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("userId", myNotification.get(i).getUserId());
                intent.putExtra("fgNotif", idNotif);

                PendingIntent detailsPendingIntent = PendingIntent.getActivity(
                        context, reqDetail, intent, PendingIntent.FLAG_UPDATE_CURRENT
                );

                /*------------------ reply ----------------- */
                Intent replyIntent = new Intent(context, NotificationReceiver.class);
                replyIntent.putExtra("userId", myNotification.get(i).getUserId());
                replyIntent.putExtra("fgNotif", idNotif);
                replyIntent.putExtra("isReply", true);

                PendingIntent replyPendingIntent = PendingIntent.getBroadcast(
                        context, reqReply, replyIntent, PendingIntent.FLAG_CANCEL_CURRENT
                );

                NotificationCompat.Action replyAction = new NotificationCompat.Action.Builder(
                        R.drawable.bg_button_send, "REPLY", replyPendingIntent)
                        .addRemoteInput(new RemoteInput.Builder(REPLY_KEY)
                                .setLabel("Enter Comment")
                                .build())
                        .setAllowGeneratedReplies(true)
                        .build();

                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setWhen(myNotification.get(i).getTimestamp())
                        .setContentIntent(detailsPendingIntent)
                        .setSmallIcon(smallIcon)
                        .setStyle(myNotification.get(i).getStyle())
                        .setGroupSummary(false)
                        .setGroup(GROUP_KEY);

                if (myNotification.get(i).getViews()) notificationBuilder.addAction(replyAction);

                notificationManager.notify(idNotif, notificationBuilder.build());

                idNotif++;
                reqReply++;
                reqMarkRead++;
            }
        }

        /*------------------ All ----------------- */
        Intent resultIntent = new Intent(context, Main.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, Constants.ReqPendingIntent.NOTIF, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID);

        if (isSound) {
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            mBuilder.setSound(alarmSound);
        }

        Notification notification = mBuilder
                .setWhen(myNotification.get(myNotification.size() - 1).getTimestamp())
                .setTicker("New Inspection")
                .setAutoCancel(true)
                .setContentText(contentText)
                .setSubText(contentText)
                .setContentIntent(resultPendingIntent)
                .setStyle(myNotification.get(myNotification.size() - 1).getStyle())
                .setSmallIcon(smallIcon)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), bigIcon))
                .setGroup(GROUP_KEY)
                .setGroupSummary(true)
                .build();

        notificationManager.notify(Constants.NotifId.FG_NOTIF, notification);
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.ic_launcher_background : R.drawable.ic_launcher_background;
    }

    public static void closeNotification(Context context, int id) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(id);
        closeNotification(context);
//        showNotification(context, true);
    }

    public static void closeNotification(Context context) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(Constants.NotifId.FG_NOTIF);
    }

    private Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void playNotificationSound() {
        try {
            Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + context.getPackageName() + "/raw/notification");
            Ringtone r = RingtoneManager.getRingtone(context, alarmSound);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
