package com.example.linkb.Class;

public class DetailEvent {
    String eventId;
    String eventName;
    String eventHost;
    String eventStartDate;
    String eventEndDate;
    String eventIntroduce;
    String eventMap;
    String eventThumbImage;
    String eventImage;
    String companyId;
    String companyName;
    String companyIntroduce;
    String companyAddress;
    String memberCount;

    public DetailEvent() {}
    public DetailEvent(
            String eventId, String eventName, String eventHost, String eventStartDate, String eventEndDate,
            String eventIntroduce, String eventMap, String eventImage, String companyId, String companyName, String memberCount){
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventHost = eventHost;
        this.eventStartDate = eventStartDate;
        this.eventEndDate = eventEndDate;
        this.eventIntroduce = eventIntroduce;
        this.eventMap = eventMap;
        this.eventImage = eventImage;
        this.companyId = companyId;
        this.companyName = companyName;
        this.memberCount = memberCount;
    }

    public String getEventId(){ return eventId; }

    public String getEventName(){ return eventName; }

    public String getEventHost(){ return eventHost; }

    public String getEventStartDate(){ return eventStartDate; }

    public String getEventEndDate(){ return eventEndDate; }

    public String getEventIntroduce(){ return eventIntroduce; }

    public String getEventMap(){ return eventMap; }

    public String getEventImage(){ return eventImage; }

    public String getCompanyId(){ return companyId; }

    public String getCompanyName(){ return companyName; }

    public String getMemberCount(){ return memberCount; }
}
