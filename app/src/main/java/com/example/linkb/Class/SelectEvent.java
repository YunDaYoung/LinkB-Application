package com.example.linkb.Class;

public class SelectEvent {
    public String id;
    public String name;
    public String host;
    public String startDate;
    public String endDate;
    public String eventLocation;
    public String eventImage;

    public SelectEvent() {}

    public SelectEvent(String id, String name, String host, String startDate, String endDate, String eventLocation, String eventImage) {
        this.id = id;
        this.name = name;
        this.host = host;
        this.startDate = startDate;
        this.endDate = endDate;
        this.eventLocation = eventLocation;
        this.eventImage = eventImage;
    }

    public String getName(){
        return name;
    }

    public String getHost(){
        return host;
    }

    public String getStartDate(){
        return startDate;
    }

    public String getEndDate(){
        return endDate;
    }

    public String getEventLocation(){
        return eventLocation;
    }

    public String getEventImage(){
        return eventImage;
    }
}
