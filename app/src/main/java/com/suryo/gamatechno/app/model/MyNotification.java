package com.suryo.gamatechno.app.model;

import android.support.v4.app.NotificationCompat;

/**
 * Created by Abhi on 13 Nov 2017 013.
 */

public class MyNotification {
    private NotificationCompat.Style style;
    private String userId;
    private Long timestamp;
    private Boolean isViews;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Boolean getViews() {
        return isViews;
    }

    public void setViews(Boolean views) {
        isViews = views;
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
