package com.example.linkb;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class RecommendEventItem {
    private String imageResource;
    private String title;
    private String day;


    public RecommendEventItem() {
    }

    public RecommendEventItem(String imageResource, String title, String day) {
        this.imageResource = imageResource;
        this.title = title;
        this.day = day;
    }
    public String getImageResource() {
        return imageResource;
    }
    public void setImageResource(String imageResource) {
        this.imageResource = imageResource;
    }
    public String getTitle() {
        return title;
    }
    public String getDay() {
        return day;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setDay(String day) {
        this.day = day;
    }
}