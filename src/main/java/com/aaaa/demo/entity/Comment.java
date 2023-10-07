package com.aaaa.demo.entity;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Comment {
        
    private Long id;
    private String name;
    private String text;

    private long createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText(){
        return text;
    }

    public void setText(String text){
        this.text=text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedDateTime() {
        return Instant.ofEpochMilli(this.createdAt).atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    @Override
    public String toString() {
        return String.format("User[id=%s,  name=%s, text=%s, createdAt=%s, createdDateTime=%s]", getId(), getName(),getText(),
                getCreatedAt(), getCreatedDateTime());
    }
}
