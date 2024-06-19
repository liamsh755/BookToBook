package co.il.liam.model;

import android.graphics.Bitmap;

import java.util.List;

public class BookRec {
    private String title;
    private List<String> authors;
    private int pageCount;
    private String description;
    private Bitmap photo;


    public BookRec() {}

    public BookRec(String title, List<String> authors, int pageCount, String description, Bitmap photo) {
        this.title = title;
        this.authors = authors;
        this.pageCount = pageCount;
        this.description = description;
        this.photo = photo;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }
}
