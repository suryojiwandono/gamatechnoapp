package com.suryo.gamatechno.app.others;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {
    public static abstract class Logs {
        public static void i(String text) {
            Log.i(Constants.TAG, text);
        }

        public static void e(String text) {
            Log.e(Constants.TAG, isNullable(text));
        }

        public static void w(String text) {
            Log.w(Constants.TAG, isNullable(text));
        }

        public static void d(String text) {
            Log.d(Constants.TAG, isNullable(text));
        }

        public static void v(String text) {
            Log.v(Constants.TAG, isNullable(text));
        }

        public static void l(String text) {
            text = isNullable(text);
            int maxLogSize = 1000;
            for (int i = 0; i <= text.length() / maxLogSize; i++) {
                int start = i * maxLogSize;
                int end = (i + 1) * maxLogSize;
                end = end > text.length() ? text.length() : end;
                i(text.substring(start, end));
            }
        }
    }

    public static void Toast(Context context, String txt) {
        Toast.makeText(context, txt, Toast.LENGTH_LONG).show();
    }
    public static void Blinked(final View view) {

        final Handler handler = new Handler();
        new Thread(new Runnable() {
            int progressStatus = 0;

            public void run() {
                while (progressStatus < 10) {
                    progressStatus++;
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.post(new Runnable() {
                        public void run() {
                            view.setVisibility((view.getVisibility() == View.VISIBLE) ? View.INVISIBLE : View.VISIBLE);
                        }
                    });
                }
            }
        }).start();
    }

    private static String isNullable(String value) {
        return (value == null) ? "" : value;
    }


    public static boolean isValidEmail(String email) {
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isServiceRunning(Context context, String serviceId) {
        ActivityManager manager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceId.equals(service.service.getClassName()))
                return true;
        }
        return false;
    }
}
