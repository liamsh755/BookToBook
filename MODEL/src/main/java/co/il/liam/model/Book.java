package co.il.liam.model;

import android.graphics.Bitmap;
import android.graphics.Color;

public class Book {

    public enum Condition {
        PERFECT,
        USED,
        OLD
    }

    public enum Exchange {
        PERMANENT,
        TEMPORARY,
        FOR_DISPLAY
    }

    public enum Height {
        TALL,
        MEDIUM,
        SHORT
    }

    public enum Width {
        THICK,
        THIN
    }

    public enum Decoration {
        ONE_LINE,
        TWO_LINES,
        THREE_LINES,
        THICK_LINE
    }

    public enum Font{
        COMIC_SANS,
        CLASSIC,
        CURSIVE,
        GOTHIC,
        FUN
    }
    private String title;
    private String genre;
    private Condition condition;
    private Exchange exchange;
    private String author;
    private String description;
    private Bitmap image;

    private int mainColor;
    private int secColor;
    private Height height;
    private Width width;
    private Decoration decorations;
    private Font font;

    public Book() {}

    public Book(String title, String author, String description, String genre,                           //book details
                Condition condition, Exchange exchange,  Bitmap image,                                   //trade details
                int mainColor, int secColor, Height height, Width width, Decoration decorations, Font font) {    //layout details
        this.title = title;
        this.genre = genre;
        this.author = author;
        this.description = description;
        this.condition = condition;
        this.exchange = exchange;
        this.image = image;
        this.mainColor = mainColor;
        this.secColor = secColor;
        this.height = height;
        this.width = width;
        this.decorations = decorations;
        this.font = font;
    }

    public Book(String title, String author, Bitmap image,int mainColor, int secColor, Height height, Width width, Decoration decorations, Font font) {
        this.title = title;
        this.author = author;
        this.image = image;
        this.mainColor = mainColor;
        this.secColor = secColor;
        this.height = height;
        this.width = width;
        this.decorations = decorations;
        this.font = font;
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

    public int getMainColor() {
        return mainColor;
    }

    public void setMainColor(int mainColor) {
        this.mainColor = mainColor;
    }

    public int getSecColor() {
        return secColor;
    }

    public void setSecColor(int secColor) {
        this.secColor = secColor;
    }

    public Height getHeight() {
        return height;
    }

    public void setHeight(Height height) {
        this.height = height;
    }

    public Width getWidth() {
        return width;
    }

    public void setWidth(Width width) {
        this.width = width;
    }

    public Decoration getDecorations() {
        return decorations;
    }

    public void setDecorations(Decoration decorations) {
        this.decorations = decorations;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }
}
