package co.il.liam.booktobook.ACTIVITIES;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private TextView tvMainHelp;
    private TextView tvMainAbout;

    private int AMOUNT_OF_BUTTONS;

    private Handler handler;
    private final int ANIMATION_DURATION = 400;
    
    private User loggedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        setScreenOrientation();

        initializeViews();
        setListeners();
        setButtons();
        waitBeforeOpening(ANIMATION_DURATION);

        //checks internet connection
        CheckInternetConnection.check(this);
    }

    //animations
    private void waitBeforeOpening(int milliseconds) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
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
            topAnimator.setDuration(ANIMATION_DURATION);

            ObjectAnimator bottomAnimator = ObjectAnimator.ofFloat(vMainBottomGradient, "y", bottomViewInitialY, topViewTargetY);
            bottomAnimator.setDuration(ANIMATION_DURATION);

            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(topAnimator, bottomAnimator);
            animatorSet.start();

            animatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {

                    fadeIn(tvMainNameDisplay, ANIMATION_DURATION);
                    fadeIn(ivLogout, ANIMATION_DURATION);
                    fadeIn(vpButtons, ANIMATION_DURATION);
                    fadeIn(tvMainHelp, ANIMATION_DURATION);
                    fadeIn(tvMainAbout, ANIMATION_DURATION);
                }
            });

        });
    }


    //just closes the 2 halves and goes to the start screen
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
            topAnimator.setDuration(ANIMATION_DURATION);

            ObjectAnimator bottomAnimator = ObjectAnimator.ofFloat(vMainBottomGradient, "y", bottomViewInitialY, bottomViewTargetY);
            bottomAnimator.setDuration(ANIMATION_DURATION);

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

    //closes the 2 halves animation and goes into a new intent, for when a button is pressed
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
            topAnimator.setDuration(ANIMATION_DURATION);

            ObjectAnimator bottomAnimator = ObjectAnimator.ofFloat(vMainBottomGradient, "y", bottomViewInitialY, bottomViewTargetY);
            bottomAnimator.setDuration(ANIMATION_DURATION);

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
        fadeOut(tvMainHelp, milliseconds);
        fadeOut(tvMainAbout, milliseconds);

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

        AMOUNT_OF_BUTTONS = vpButtonsItems.size();

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
        tvMainHelp = findViewById(R.id.tvMainHelp);
        tvMainAbout = findViewById(R.id.tvMainAbout);

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

                        fadeOut(ivLogout, ANIMATION_DURATION);
                        fadeOut(vpButtons, ANIMATION_DURATION);
                        fadeOut(tvMainNameDisplay, ANIMATION_DURATION);
                        fadeOut(tvMainHelp, ANIMATION_DURATION);
                        fadeOut(tvMainAbout, ANIMATION_DURATION);

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Start the animation after 2 seconds
                                startLeavingAnimation();
                            }
                        }, ANIMATION_DURATION);
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

        tvMainHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainScreenActivity.this);
                builder.setTitle("Help!!!");
                builder.setMessage("You can contact us at any time and we would do our best to respond as quickly as possible.\n" +
                        "Email us at BookToBookCS@gmail.com or click the button below.");
                builder.setCancelable(true);
                builder.setPositiveButton("FAQ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "FAQ", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("Email us", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "BookToBookCS@gmail.com" });
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Contacting support from BookToBook");
                        intent.putExtra(Intent.EXTRA_TEXT, "Hello I need help!\n");
                        intent.setType("message/rfc822");
                        startActivity(intent.createChooser(intent, "Choose an email client:"));
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        tvMainAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "About us", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void setScreenOrientation() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                waitBeforeOpening(ANIMATION_DURATION);
            }
        }
    }


    @Override
    public void onViewPagerButtonClicked(int position) {
        switch (position % AMOUNT_OF_BUTTONS) {
            case 0:
                Intent searchIntent = new Intent(getApplicationContext(), LibraryActivity.class);
                searchIntent.putExtra("user", loggedUser);
                searchIntent.putExtra("job", "search");
                fadeOutViewsAndIntent(searchIntent, ANIMATION_DURATION);

                break;
            case 1:
                Intent locationIntent = new Intent(getApplicationContext(), LocationActivity.class);
                locationIntent.putExtra("user", loggedUser);
                fadeOutViewsAndIntent(locationIntent, ANIMATION_DURATION);

                break;
            case 2:
                Intent discoverIntent = new Intent(getApplicationContext(), DiscoverActivity.class);
                discoverIntent.putExtra("user", loggedUser);
                fadeOutViewsAndIntent(discoverIntent, ANIMATION_DURATION);

                break;
            case 3:
                Intent libraryIntent = new Intent(getApplicationContext(), LibraryActivity.class);
                libraryIntent.putExtra("user", loggedUser);
                libraryIntent.putExtra("job", "library");
                fadeOutViewsAndIntent(libraryIntent, ANIMATION_DURATION);

                break;
            case 4:
                Intent addIntent = new Intent(getApplicationContext(), AddActivity.class);
                addIntent.putExtra("user", loggedUser);
                fadeOutViewsAndIntent(addIntent, ANIMATION_DURATION);

                break;
            case 5:
                Intent chatIntent = new Intent(getApplicationContext(), ChatListActivity.class);
                chatIntent.putExtra("user", loggedUser);
                fadeOutViewsAndIntent(chatIntent, ANIMATION_DURATION);

                break;
            default:
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainScreenActivity.this);
        builder.setTitle("Logging out?");
        builder.setMessage("Are you sure you want to leave?");
        builder.setCancelable(false);
        builder.setPositiveButton("Log out", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                fadeOut(ivLogout, ANIMATION_DURATION);
                fadeOut(vpButtons, ANIMATION_DURATION);
                fadeOut(tvMainNameDisplay, ANIMATION_DURATION);
                fadeOut(tvMainHelp, ANIMATION_DURATION);
                fadeOut(tvMainAbout, ANIMATION_DURATION);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Start the animation after 2 seconds
                        startLeavingAnimation();
                    }
                }, ANIMATION_DURATION);
            }
        });
        builder.setNegativeButton("Stay logged in", null);

        AlertDialog dialogLeave = builder.create();

        new AlertDialog.Builder(this)
                .setTitle("Go back Confirmation")
                .setMessage("Are you sure you want to go back?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogLeave.show();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}