package com.example.linkb;

public class Main_SampleData {
    private int image;
    private String context;

    public Main_SampleData(int image, String context){
        this.image = image;
        this.context = context;
    }

    public int getTitle(){
        return this.image;
    }

    public String getContext(){
        return this.context;
    }
}
