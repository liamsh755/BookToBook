package co.il.liam.model;

import java.util.Collections;

public class Books extends BaseList<Book, Books>{

    public void reverseContents() {
        Collections.reverse(this);
    }
}
