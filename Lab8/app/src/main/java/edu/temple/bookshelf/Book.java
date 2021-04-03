package edu.temple.bookshelf;

public class Book {
    private String title;
    private String author;
    private String coverURL;
    private int id;

    public Book(){
        //Empty method
    };

    public Book( String title, String author, String coverURL, int id) {
        this.title = title;
        this.author = author;
        this.coverURL = coverURL;
        this.id = id;
    }

    public String getTitle() { return title; }

    public String getAuthor() { return author; }

    public String getCoverURL() { return coverURL; }

    public int getId() { return id; }

}
