package co.il.liam.booktobook;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import android.os.Handler;

public class LoadingDialog extends Dialog implements DialogInterface.OnDismissListener {
    private ImageView ivLoadingBooksAnim;
    private TextView tvLoadingTitle;
    private TextView tvLoadingDescription;

    private Handler handler;
    private int curIndex = 0;
    private final int ANIMATION_LENGTH = 250;

    private final int[] allBookImages = {
            R.drawable.loading_books_0_colored,
            R.drawable.loading_books_1_colored,
            R.drawable.loading_books_2_colored,
            R.drawable.loading_books_3_colored,
            R.drawable.loading_books_4_colored,
            R.drawable.loading_books_5_colored,
            R.drawable.loading_books_6_colored,
            R.drawable.loading_books_7_colored
    };

    public LoadingDialog(@NonNull Context context, String title, String description) {
        super(context);
        setContentView(R.layout.loading_dialog);

        initializeViews(title, description);
        startAnimation();
    }

    private void startAnimation() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ivLoadingBooksAnim.setImageResource(allBookImages[curIndex]);
                curIndex++;
                if (curIndex == allBookImages.length) {
                    curIndex = 0; // Loop back to the start
                }
                handler.postDelayed(this, ANIMATION_LENGTH);
            }
        }, ANIMATION_LENGTH);
    }

    private void initializeViews(String title, String description) {
        ivLoadingBooksAnim = findViewById(R.id.ivLoadingBooksAnim);
        tvLoadingTitle = findViewById(R.id.tvLoadingTitle);
        tvLoadingDescription = findViewById(R.id.tvLoadingDescription);
        handler = new Handler();

        tvLoadingTitle.setText(title);
        tvLoadingDescription.setText(description);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        handler.removeCallbacksAndMessages(null);
    }
}
