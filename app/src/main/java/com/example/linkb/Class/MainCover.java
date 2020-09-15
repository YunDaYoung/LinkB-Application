package com.example.linkb.Class;

public class MainCover {
    String id;
    String mobileImage;
    String coverLink;

    public MainCover() {}
    public MainCover(String id, String mobileImage, String coverLink) {
        this.id = id;
        this.mobileImage = mobileImage;
        this.coverLink = coverLink;
    }

    public String getId(){
        return id;
    }

    public String getMobileImage(){
        return mobileImage;
    }

    public String getCoverLink(){
        return coverLink;
    }
}
