package co.il.liam.model;

import android.graphics.Bitmap;
import android.graphics.Color;

public class Book {

    public enum Condition {
        PERFECT,
        SLIGHTLY_USED,
        OLD
    }

    public enum Exchange {
        PERMANENT,
        TEMPORARY,
        FOR_DISPLAY
    }

    private String id;
    private String title;
    private String genre;
    private int numInSeries;
    private Condition condition;
    private Exchange exchange;
    private String author;
    private String description;
    private Bitmap image;
    private Color color;

    public Book() {}

    public Book(String id, String title, String genre, int numInSeries,
                Condition condition, Exchange exchange, String author, String description, Bitmap image, Color color) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.numInSeries = numInSeries;
        this.condition = condition;
        this.exchange = exchange;
        this.author = author;
        this.description = description;
        this.image = image;
        this.color = color;
    }
    public Book(String title, String author, Bitmap image) {
        this.title = title;
        this.author = author;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getNumInSeries() {
        return numInSeries;
    }

    public void setNumInSeries(int numInSeries) {
        this.numInSeries = numInSeries;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public Exchange getExchange() {
        return exchange;
    }

    public void setExchange(Exchange exchange) {
        this.exchange = exchange;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
