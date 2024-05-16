package co.il.liam.model;

import java.util.ArrayList;
import java.util.Collections;

public class Library {

    private String email;
    private ArrayList<Book> books;

    public Library() {}

    public Library(String email, ArrayList<Book> books) {
        this.email = email;
        this.books = books;
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public void setBooks(ArrayList<Book> books) {
        this.books = books;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void add(Book book) {
        books.add(book);
    }

    public void reverseContents() {
        Collections.reverse(books);
    }
}
