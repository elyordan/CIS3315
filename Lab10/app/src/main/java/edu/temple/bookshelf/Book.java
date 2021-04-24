package edu.temple.bookshelf;

public class Book {
    private String title;
    private String author;
    private String coverURL;
    private int id;
    private int duration;

    public Book(){
        //Empty method
    };

    public Book( String title, String author, String coverURL, int id, int duration) {
        this.title = title;
        this.author = author;
        this.coverURL = coverURL;
        this.id = id;
        this.duration = duration;
    }

    public String getTitle() { return title; }

    public String getAuthor() { return author; }

    public String getCoverURL() { return coverURL; }

    public int getId() { return id; }

    public int getDuration() { return duration; }

}
