package co.il.liam.booktobook.ACTIVITIES;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.Guideline;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

import co.il.liam.booktobook.ADAPTERS.VPbuttonsAdapter;
import co.il.liam.booktobook.R;
import co.il.liam.booktobook.VPbuttonsItem;
import co.il.liam.booktobook.CheckInternetConnection;
import co.il.liam.model.User;

public class MainScreenActivity extends BaseActivity implements VPbuttonsAdapter.ViewPagerButtonClickListener {
    private TextView tvMainNameDisplay;
    private ViewPager2 vpButtons;
    private ImageView ivLogout;
    private View vMainTopGradient;
    private View vMainBottomGradient;
    private Guideline glMain15p;
    private Guideline glMain50p;
    private Guideline glMain85p;

    private Handler handler;
    
    private User loggedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        initializeViews();
        setListeners();
        setButtons();
        waitBeforeOpening(600);

        //checks internet connection
        CheckInternetConnection.check(this);
    }

    //animations
    private void waitBeforeOpening(int milliseconds) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start the animation after 2 seconds
                startOpeningAnimation();
            }
        }, milliseconds);
    }

    private void startOpeningAnimation() {
        vMainTopGradient.post(() -> {
            float topViewInitialY = vMainTopGradient.getY();
            float topViewTargetY = glMain15p.getY() - vMainTopGradient.getHeight();

            float bottomViewInitialY = vMainBottomGradient.getY();
            float bottomViewTargetY = glMain85p.getY();

            ObjectAnimator topAnimator = ObjectAnimator.ofFloat(vMainTopGradient, "y", topViewInitialY, bottomViewTargetY);
            topAnimator.setDuration(1000);

            ObjectAnimator bottomAnimator = ObjectAnimator.ofFloat(vMainBottomGradient, "y", bottomViewInitialY, topViewTargetY);
            bottomAnimator.setDuration(1000);

            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(topAnimator, bottomAnimator);
            animatorSet.start();

            animatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    int milliseconds = 1000;

                    fadeIn(tvMainNameDisplay, milliseconds);
                    fadeIn(ivLogout, milliseconds);
                    fadeIn(vpButtons, milliseconds);
                }
            });

        });
    }
    private void startLeavingAnimation() {
        vMainTopGradient.post(() -> {
            // Get the positions of the guidelines
            float guideline50pY = glMain50p.getY();

            // Calculate the target positions for the top and bottom views
            float topViewInitialY = vMainTopGradient.getY();
            float topViewTargetY = guideline50pY;

            float bottomViewInitialY = vMainBottomGradient.getY();
            float bottomViewTargetY = guideline50pY - vMainBottomGradient.getHeight();

            // Create the animators
            ObjectAnimator topAnimator = ObjectAnimator.ofFloat(vMainTopGradient, "y", topViewInitialY, topViewTargetY);
            topAnimator.setDuration(1000);

            ObjectAnimator bottomAnimator = ObjectAnimator.ofFloat(vMainBottomGradient, "y", bottomViewInitialY, bottomViewTargetY);
            bottomAnimator.setDuration(1000);

            // Play the animations together
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(topAnimator, bottomAnimator);
            animatorSet.start();

            // Add listener to handle end of animation
            animatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    setResult(Activity.RESULT_OK);
                    finish();
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            });
        });
    }

    private void startClosingAnimation(Intent intent) {
        vMainTopGradient.post(() -> {
            // Get the positions of the guidelines
            float guideline50pY = glMain50p.getY();

            // Calculate the target positions for the top and bottom views
            float topViewInitialY = vMainTopGradient.getY();
            float topViewTargetY = guideline50pY;

            float bottomViewInitialY = vMainBottomGradient.getY();
            float bottomViewTargetY = guideline50pY - vMainBottomGradient.getHeight();

            // Create the animators
            ObjectAnimator topAnimator = ObjectAnimator.ofFloat(vMainTopGradient, "y", topViewInitialY, topViewTargetY);
            topAnimator.setDuration(1000);

            ObjectAnimator bottomAnimator = ObjectAnimator.ofFloat(vMainBottomGradient, "y", bottomViewInitialY, bottomViewTargetY);
            bottomAnimator.setDuration(1000);

            // Play the animations together
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(topAnimator, bottomAnimator);
            animatorSet.start();

            // Add listener to handle end of animation
            animatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    startActivityForResult(intent, 1);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    Log.d("qqq", "intent done");
                }
            });
        });
    }
    private void fadeOutViewsAndIntent(Intent intent, int milliseconds) {
        fadeOut(ivLogout, milliseconds);
        fadeOut(vpButtons, milliseconds);
        fadeOut(tvMainNameDisplay, milliseconds);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startClosingAnimation(intent);
            }
        }, milliseconds);
    }

    private void fadeIn(View view, int Milliseconds) {
        view.setVisibility(View.VISIBLE);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        fadeIn.setDuration(Milliseconds);
        fadeIn.start();
    }
    private void fadeOut(View view, int Milliseconds) {
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
        fadeOut.setDuration(Milliseconds);
        fadeOut.start();
        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
            }
        });

    }



    private void setButtons() {
        List<VPbuttonsItem> vpButtonsItems = new ArrayList<>();

        vpButtonsItems.add(new VPbuttonsItem(R.drawable.button_search, "Search for books"));
        vpButtonsItems.add(new VPbuttonsItem(R.drawable.button_location, "Nearby books"));
        vpButtonsItems.add(new VPbuttonsItem(R.drawable.button_discover, "Books for you"));
        vpButtonsItems.add(new VPbuttonsItem(R.drawable.button_library, "My library"));
        vpButtonsItems.add(new VPbuttonsItem(R.drawable.button_add, "Add book"));
        vpButtonsItems.add(new VPbuttonsItem(R.drawable.button_chat, "My chats"));

        vpButtons.setAdapter(new VPbuttonsAdapter(vpButtonsItems, vpButtons, MainScreenActivity.this, this));

        vpButtons.setClipToPadding(false);
        vpButtons.setClipChildren(false);
        vpButtons.setOffscreenPageLimit(3);
        vpButtons.setCurrentItem(vpButtonsItems.size() - 3, false);
        vpButtons.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.6f + r * 0.4f);
                page.setScaleX(0.6f + r * 0.4f);
            }
        });

        vpButtons.setPageTransformer(compositePageTransformer);

        vpButtons.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                if (position == 0) {
                    vpButtons.setCurrentItem(vpButtonsItems.size() - 2, false);

                }
                else if (position == vpButtonsItems.size()) {
                    vpButtons.setCurrentItem(350, false);
                }
            }
        });
    }

    @Override
    protected void initializeViews() {
        tvMainNameDisplay = findViewById(R.id.tvMainHello);
        vpButtons = findViewById(R.id.vpMainButtons);
        ivLogout = findViewById(R.id.ivLogOut);
        vMainTopGradient = findViewById(R.id.vMainBottomGradient);
        vMainBottomGradient = findViewById(R.id.vMainTopGradient);
        glMain15p = findViewById(R.id.glMain15p);
        glMain50p = findViewById(R.id.glMain50p);
        glMain85p = findViewById(R.id.glMain85p);

        handler = new Handler();

        setNameDisplay();
    }

    private void setNameDisplay() {
        Intent  userInfoIntent = getIntent();
        loggedUser = (User) userInfoIntent.getSerializableExtra("user");

        assert loggedUser != null;
        String message = "Hello " + loggedUser.getUsername();
        tvMainNameDisplay.setText(message);
    }

    @Override
    protected void setListeners() {
        ivLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainScreenActivity.this);
                builder.setTitle("Logging out?");
                builder.setMessage("Are you sure you want to leave?");
                builder.setCancelable(false);
                builder.setPositiveButton("Log out", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        int milliseconds = 1000;

                        fadeOut(ivLogout, milliseconds);
                        fadeOut(vpButtons, milliseconds);
                        fadeOut(tvMainNameDisplay, milliseconds);

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Start the animation after 2 seconds
                                startLeavingAnimation();
                            }
                        }, milliseconds);
                    }
                });
                builder.setNegativeButton("Stay logged in", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                waitBeforeOpening(600);
            }
        }
    }


    @Override
    public void onViewPagerButtonClicked(int position) {
        switch (position % 6) {
            case 0:
                Intent searchIntent = new Intent(getApplicationContext(), SearchActivity.class);
                searchIntent.putExtra("user", loggedUser);
                fadeOutViewsAndIntent(searchIntent, 1000);

                break;
            case 1:
                Intent locationIntent = new Intent(getApplicationContext(), LocationActivity.class);
                locationIntent.putExtra("user", loggedUser);
                fadeOutViewsAndIntent(locationIntent, 1000);

                break;
            case 2:
                Intent discoverIntent = new Intent(getApplicationContext(), DiscoverActivity.class);
                discoverIntent.putExtra("user", loggedUser);
                fadeOutViewsAndIntent(discoverIntent, 1000);

                break;
            case 3:
                Intent libraryIntent = new Intent(getApplicationContext(), LibraryActivity.class);
                libraryIntent.putExtra("user", loggedUser);
                fadeOutViewsAndIntent(libraryIntent, 500);

                break;
            case 4:
                Intent addIntent = new Intent(getApplicationContext(), AddActivity.class);
                addIntent.putExtra("user", loggedUser);
                fadeOutViewsAndIntent(addIntent, 1000);

                break;
            case 5:
                Intent chatIntent = new Intent(getApplicationContext(), ChatListActivity.class);
                chatIntent.putExtra("user", loggedUser);
                fadeOutViewsAndIntent(chatIntent, 1000);

                break;
            default:
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}