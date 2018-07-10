package com.example.rodak.guardiannewsapp;

public class NewsDto {
    private String title;
    private String sectionName;
    private String author;
    private String webPublicationDate;

    public NewsDto(String title, String sectionName, String author, String webPublicationDate) {
        this.title = title;
        this.sectionName = sectionName;
        this.author = author;
        this.webPublicationDate = webPublicationDate;
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

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setWebPublicationDate(String webPublicationDate) {
        this.webPublicationDate = webPublicationDate;
    }
}
