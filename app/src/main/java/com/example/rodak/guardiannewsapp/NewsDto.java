package com.example.rodak.guardiannewsapp;

public class NewsDto {
    private String title;
    private String sectionName;
    private String author;
    private String webPublicationDate;
    private String url;

    public NewsDto(String title, String sectionName, String author, String webPublicationDate, String url) {
        this.title = title;
        this.sectionName = sectionName;
        this.author = author;
        this.webPublicationDate = webPublicationDate;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getSectionName() {
        return sectionName;
    }

    public String getAuthor() {
        return author;
    }

    public String getWebPublicationDate() {
        return webPublicationDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }
}
