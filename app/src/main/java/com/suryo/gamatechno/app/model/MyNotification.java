package com.suryo.gamatechno.app.model;

import android.support.v4.app.NotificationCompat;

/**
 * Created by Abhi on 13 Nov 2017 013.
 */

public class MyNotification {
    private NotificationCompat.Style style;
    private String messageId;
    private Long timestamp;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public NotificationCompat.Style getStyle() {
        return style;
    }

    public void setStyle(NotificationCompat.Style style) {
        this.style = style;
    }
}
