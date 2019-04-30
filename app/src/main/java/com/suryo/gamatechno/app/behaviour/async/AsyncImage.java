package com.suryo.gamatechno.app.behaviour.async;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.suryo.gamatechno.app.behaviour.Response;
import com.suryo.gamatechno.app.others.Utility;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AsyncImage extends AsyncTask {

    private Context context;
    private Response.OnReceiverAction onAsyncAction;

    public AsyncImage(Context context) {
        this.context = context;
    }

    public void setOnAsyncAction(Response.OnReceiverAction onAsyncAction) {
        this.onAsyncAction = onAsyncAction;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        return getBitmapFromURL(objects[0].toString());
    }

    @Override
    protected void onPostExecute(Object o) {
        if (onAsyncAction != null) onAsyncAction.onRefresh(o);
    }


    private static Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            Utility.Logs.e(e.getMessage());
            return null;
        }
    }

}
