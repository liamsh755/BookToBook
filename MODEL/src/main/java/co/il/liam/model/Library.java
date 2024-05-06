package co.il.liam.model;

import java.util.ArrayList;
import java.util.Collections;

public class Library extends ArrayList<Book> {
    public void reverseContents() {
        Collections.reverse(this);
    }
}
