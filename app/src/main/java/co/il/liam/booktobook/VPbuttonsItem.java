package co.il.liam.booktobook;

import android.graphics.drawable.Drawable;

public class VPbuttonsItem {

    private String title;
    private int image;

    public VPbuttonsItem(int image , String title) {
        this.title = title;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }
    public int getImage() {
        return image;
    }

}
