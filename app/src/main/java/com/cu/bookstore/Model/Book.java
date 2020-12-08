package com.cu.bookstore.Model;

public class Book {
    String id,name,image,url,direct_url,type,writer,history,produce;

    public Book() {
    }

    public Book(String id, String name, String image, String url, String direct_url, String type, String writer, String history, String produce) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.url = url;
        this.direct_url = direct_url;
        this.type = type;
        this.writer = writer;
        this.history = history;
        this.produce = produce;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDirect_url() {
        return direct_url;
    }

    public void setDirect_url(String direct_url) {
        this.direct_url = direct_url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public String getProduce() {
        return produce;
    }

    public void setProduce(String produce) {
        this.produce = produce;
    }
}
