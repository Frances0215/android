package com.example.news.ui.home;

import java.io.Serializable;

public class News implements Serializable {
    private static final long serialVersionUID = 123456;
    private String ID;
    private String title;
    private String publisher;
    private String date;
    private String contents;
    private String type;
    private String[] content_f;

    public News(String ID,String title,String publisher,String date,String contents,String type){
        this.ID = ID;
        this.title = title;
        this.publisher = publisher;
        this.date = date;
        this.contents = contents;
        this.type = type;
    }

    public String getID() {
        return ID;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getContents() {
        return contents;
    }

    public String getType() {
        return type;
    }

    public String[] getContent_f() {
        return content_f;
    }

    public void setContent_f(String[] content_f) {
        this.content_f = content_f;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setDate(String data) {
        this.date = date;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
