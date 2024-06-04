package co.il.liam.booktobook;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class LargerPhotoDialog extends Dialog {
    private ImageView ivLargerPhoto;
    private Bitmap image;
    private DisplayMetrics displayMetrics;

    public LargerPhotoDialog(@NonNull Context context, Bitmap image, DisplayMetrics displayMetrics) {
        super(context);
        this.image = image;
        this.displayMetrics = displayMetrics;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.larger_photo_dialog);

        initializeViews();
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            dismiss();
        }
        return super.onTouchEvent(event);
    }

    private void initializeViews() {
        ivLargerPhoto = findViewById(R.id.ivLargerPhoto);

        float height = displayMetrics.heightPixels - 25;
        float width = displayMetrics.widthPixels - 25;

        ivLargerPhoto.getLayoutParams().height = (int) height;
        ivLargerPhoto.getLayoutParams().width = (int) width;

        ivLargerPhoto.setImageBitmap(image);
    }

}
