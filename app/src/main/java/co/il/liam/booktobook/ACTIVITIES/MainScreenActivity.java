package co.il.liam.booktobook.ACTIVITIES;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.utils.widget.MotionLabel;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

import co.il.liam.booktobook.ADAPTERS.VPbuttonsAdapter;
import co.il.liam.booktobook.R;
import co.il.liam.booktobook.VPbuttonsItem;
import co.il.liam.model.User;

public class MainScreenActivity extends BaseActivity implements VPbuttonsAdapter.ViewPagerButtonClickListener {
    private TextView tvMainNameDisplay;
    private ViewPager2 vpButtons;
    private ImageView ivLogout;
    private User loggedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        initializeViews();
        setListeners();
        setButtons();
    }

    private void setButtons() {
        List<VPbuttonsItem> vpButtonsItems = new ArrayList<>();

        vpButtonsItems.add(new VPbuttonsItem(R.drawable.button_search, "Search for books"));
        vpButtonsItems.add(new VPbuttonsItem(R.drawable.button_location, "Nearby books"));
        vpButtonsItems.add(new VPbuttonsItem(R.drawable.button_discover, "Books for you"));
        vpButtonsItems.add(new VPbuttonsItem(R.drawable.button_library, "My library"));
        vpButtonsItems.add(new VPbuttonsItem(R.drawable.button_add, "Add book"));

        vpButtons.setAdapter(new VPbuttonsAdapter(vpButtonsItems, vpButtons, MainScreenActivity.this, this));

        vpButtons.setClipToPadding(false);
        vpButtons.setClipChildren(false);
        vpButtons.setOffscreenPageLimit(3);
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
                else if (position == vpButtonsItems.size() - 1) {
                    vpButtons.setCurrentItem(1, false);
                }
            }
        });
    }

    @Override
    protected void initializeViews() {
        tvMainNameDisplay = findViewById(R.id.tvMainHello);
        vpButtons = findViewById(R.id.vpMainButtons);
        ivLogout = findViewById(R.id.ivLogOut);

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
                setResult(Activity.RESULT_OK);
                finish();
            }
        });
    }


    @Override
    public void onViewPagerButtonClicked(int position) {
        switch (position) {
            case 0:
                int search = 1;
                break;
            case 1:
                int location = 1;
                break;
            case 2:
                int discover = 1;
                break;
            case 3:
                Intent libraryIntent = new Intent(getApplicationContext(), LibraryActivity.class);
                libraryIntent.putExtra("user", loggedUser);
                startActivity(libraryIntent);
                break;
            case 4:
                int add = 1;
                break;
            default:
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}