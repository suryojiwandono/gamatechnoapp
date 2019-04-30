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
import android.text.Html;

import com.suryo.gamatechno.app.Main;
import com.suryo.gamatechno.app.R;
import com.suryo.gamatechno.app.behaviour.NotificationReceiver;
import com.suryo.gamatechno.app.behaviour.Response;
import com.suryo.gamatechno.app.behaviour.async.AsyncImage;
import com.suryo.gamatechno.app.db.RoomHelper;
import com.suryo.gamatechno.app.model.MyNotification;
import com.suryo.gamatechno.app.model.Room;
import com.suryo.gamatechno.app.page.PageRoom;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by Abhi on 13 Nov 2017 013.
 */

public class NotificationUtils {
    private static final String CHANNEL_ID = "com.suryo.gamatechno.notification.channel";
    private static final String GROUP_KEY = "com.suryo.gamatechno.notification.group";
    public static final String REPLY_KEY = "com.suryo.gamatechno.notification.reply";
    private Context context;

    NotificationUtils(Context context) {
        this.context = context;
    }

    void displayNotification(List<MyNotification> myNotification, String contentText, boolean isSound) {
        final int bigIcon = R.drawable.icon_app;
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
                Intent intent = new Intent(context, PageRoom.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("userId", myNotification.get(i).getMessageId());
                intent.putExtra("fgNotif", idNotif);

                PendingIntent detailsPendingIntent = PendingIntent.getActivity(
                        context, reqDetail, intent, PendingIntent.FLAG_UPDATE_CURRENT
                );

                /*------------------ reply ----------------- */
                Intent replyIntent = new Intent(context, NotificationReceiver.class);
                replyIntent.putExtra("userId", myNotification.get(i).getMessageId());
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

                notificationBuilder.addAction(replyAction);

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
                .setTicker("New Chat")
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

    public static void showNotification(final Context context) {
        String separator = ": ";
        String photo = "📷 Photo";
        RoomHelper roomHelper = new RoomHelper();
        final List<Room> data = roomHelper.getNotification();
        if (data.size() > 0) {
            LinkedHashMap<String, List<Room>> hashMap = getHashMap(data, getIdGroup(data));

            final String contentTitle = context.getResources().getString(R.string.app_name);
            final String contentText = (data.size() > 1) ? data.size() + " chats" : data.size() + " chat";

            if (hashMap.size() == 1) {
                String[] temp = data.get(0).fromUserPhoto.split(Constants.http);
                if (data.size() == 1 && temp.length == 3) {
                    final Room detail = data.get(0);
                    final String message = detail.fromUserName + separator + (detail.fromMessage.equals("") ? photo : detail.fromMessage);
                    AsyncImage asyncImage = new AsyncImage(context);
                    asyncImage.setOnAsyncAction(new Response.OnReceiverAction() {
                        @Override
                        public void onRefresh(Object object) {
                            if (object != null) {
                                Bitmap bitmapImg = (Bitmap) object;
                                NotificationCompat.BigPictureStyle bigPicture = new NotificationCompat.BigPictureStyle().bigPicture(bitmapImg);
                                bigPicture.setSummaryText(Html.fromHtml(message).toString());
                                bigPicture.setBigContentTitle(contentTitle);

                                List<MyNotification> myNotifications = new ArrayList<>();
                                MyNotification myNotif = new MyNotification();
                                myNotif.setMessageId(detail.messageId);
                                myNotif.setStyle(bigPicture);
                                myNotif.setTimestamp(MyDateFormat.dateToLong(data.get(data.size() - 1).timestamp));
                                myNotifications.add(myNotif);
                                showNotification(context, myNotifications, contentText);
                            }
                        }
                    });
                    asyncImage.execute(detail.fromUserPhoto);
                } else {
                    int size = data.size();
                    final String totalMessage = size > 1 ? " (" + size + " chats)" : "";

                    NotificationCompat.MessagingStyle messagingStyle = new NotificationCompat.MessagingStyle(contentTitle.concat(totalMessage));
                    messagingStyle.setConversationTitle(contentTitle.concat(totalMessage));

                    for (Room myDetail : data) {
                        String note = myDetail.fromMessage.equals("") ? photo : myDetail.fromMessage;
                        messagingStyle.addMessage(note, MyDateFormat.dateToLong(myDetail.timestamp), myDetail.fromUserName);
                    }
                    final Room detail = data.get(0);

                    List<MyNotification> myNotifications = new ArrayList<>();
                    MyNotification myNotif = new MyNotification();
                    myNotif.setMessageId(detail.messageId);
                    myNotif.setStyle(messagingStyle);
                    myNotif.setTimestamp(MyDateFormat.dateToLong(data.get(data.size() - 1).timestamp));
                    myNotifications.add(myNotif);
                    showNotification(context, myNotifications, contentText);
                }
            } else if (hashMap.size() > 1) {
                List<MyNotification> myNotifications = new ArrayList<>();
                // >= API 24
                for (String key : hashMap.keySet()) {
                    List<Room> rooms = hashMap.get(key);

                    int size = rooms.size();
                    String totalMessage = size > 1 ? " (" + size + " chats)" : "";

                    NotificationCompat.MessagingStyle messagingStyle = new NotificationCompat.MessagingStyle(contentTitle.concat(totalMessage));
                    messagingStyle.setConversationTitle(contentTitle.concat(totalMessage));

                    for (Room detail : rooms) {
                        String note = detail.fromMessage.equals("") ? photo : detail.fromMessage;
                        messagingStyle.addMessage(note, MyDateFormat.dateToLong(detail.timestamp), detail.fromUserName);
                    }

                    MyNotification myNotif = new MyNotification();
                    myNotif.setMessageId(key);
                    myNotif.setStyle(messagingStyle);
                    myNotif.setTimestamp(MyDateFormat.dateToLong(rooms.get(rooms.size() - 1).timestamp));
                    myNotifications.add(myNotif);
                }

                // < API 24
                NotificationCompat.MessagingStyle messagingStyle = new NotificationCompat.MessagingStyle(contentTitle);
                messagingStyle.setConversationTitle(contentTitle);
//                Collections.reverse(data);
                for (Room detail : data) {
                    String note = detail.fromMessage.equals("") ? photo : detail.fromMessage;
                    messagingStyle.addMessage(note, MyDateFormat.dateToLong(detail.timestamp), detail.fromUserName);
                }

                MyNotification myNotif = new MyNotification();
                myNotif.setMessageId(data.get(0).messageId);
                myNotif.setStyle(messagingStyle);
                myNotif.setTimestamp(MyDateFormat.dateToLong(data.get(0).timestamp));
                myNotifications.add(myNotif);
                showNotification(context, myNotifications, contentText);
            }
        }
    }

    private static LinkedHashMap<String, List<Room>> getHashMap(List<Room> data, List<String> dataId) {
        LinkedHashMap<String, List<Room>> hashMap = new LinkedHashMap<>();
        for (String ids : dataId) {
            List<Room> idIns = new ArrayList<>();
            for (Room rooms : data) {
                if (rooms.messageId.equals(ids)) {
                    idIns.add(rooms);
                }
            }
            hashMap.put(ids, idIns);
        }
        return hashMap;
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.icon_app : R.drawable.icon_app;
    }

    private static void show(Context context, Room room, Bitmap bitmapImg) {
        NotificationCompat.Style style = null;
        if (bitmapImg != null) {
            NotificationCompat.BigPictureStyle bigPicture = new NotificationCompat.BigPictureStyle().bigPicture(bitmapImg);
            bigPicture.setSummaryText(Html.fromHtml(room.fromMessage).toString());
            bigPicture.setBigContentTitle(room.fromUserEmail);
            style = bigPicture;
        } else {
            NotificationCompat.MessagingStyle messagingStyle = new NotificationCompat.MessagingStyle(room.fromMessage);
            messagingStyle.setConversationTitle(room.fromMessage);
            style = messagingStyle;
        }
        List<MyNotification> myNotifications = new ArrayList<>();
        MyNotification myNotif = new MyNotification();
        myNotif.setMessageId(room.fromUserId);
        myNotif.setStyle(style);
        myNotif.setTimestamp(MyDateFormat.dateToLong(room.timestamp));
        myNotifications.add(myNotif);
        showNotification(context, myNotifications, room.fromMessage);
    }

    private static List<String> getIdGroup(List<Room> data) {
        List<String> dataId = new ArrayList<>();
        for (Room rooms : data) {
            if (!dataId.contains(rooms.messageId)) {
                dataId.add(rooms.messageId);
            }
        }
        return dataId;
    }

    public static void closeNotification(Context context, int id) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(id);
        closeNotification(context);
//        showNotification(context,);
    }

    private static void showNotification(Context context, List<MyNotification> myNotifications, String contentText) {
        NotificationUtils notifUtils = new NotificationUtils(context);
        notifUtils.displayNotification(myNotifications, contentText, true);
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
