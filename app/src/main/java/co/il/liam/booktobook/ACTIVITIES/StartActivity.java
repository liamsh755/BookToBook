package co.il.liam.booktobook.ACTIVITIES;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;

import co.il.liam.booktobook.ADAPTERS.VPadapter;
import co.il.liam.booktobook.NoInternetDialog;
import co.il.liam.booktobook.R;
import co.il.liam.booktobook.VPitem;
import co.il.liam.booktobook.CheckInternetConnection;
import co.il.liam.model.User;

public class StartActivity extends BaseActivity {

    private Button btnStartLogin;
    private Button btnStartRegister;
    private ViewPager2 vpInfo;
    private ImageView ivDots;

    private SharedPreferences preferences;
    static final int LOG_OUT_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        initializeViews();
        setListeners();
        setRemember();
        setInfo();
        //checks internet connection
        CheckInternetConnection.check(this);
    }

    private void setRemember() {
        preferences = getSharedPreferences("remember", MODE_PRIVATE);
        boolean checkbox = preferences.getBoolean("checkbox", false);

        if (checkbox) {
            User foundUser = new User();
            foundUser.setUsername(preferences.getString("uUsername", "ERROR"));
            foundUser.setEmail(preferences.getString("uEmail", ""));
            foundUser.setPassword(preferences.getString("uPassword", ""));
            foundUser.setIdFs(preferences.getString("uIdFs", ""));
            foundUser.setState(preferences.getString("uState", ""));
            foundUser.setCity(preferences.getString("uCity", ""));

            Intent intentEnter = new Intent(getApplicationContext(), MainScreenActivity.class);
            intentEnter.putExtra("user", foundUser);
            startActivityForResult(intentEnter, LOG_OUT_REQUEST_CODE);
        }
    }

    private void setInfo() {
        ArrayList<VPitem> vPitemArrayList;
        int[] images = {R.drawable.vpimg_showoff_new, R.drawable.vpimg_swap_new, R.drawable.vpimg_discover_new};
        String[] headers = {"Show off", "Swap books", "Discover more"};
        String[] descriptions = {
                getString(R.string.showoff_desc),
                getString(R.string.swap_desc),
                getString(R.string.discover_desc)
        };

        vPitemArrayList = new ArrayList<>();

        for (int i = 0; i < images.length; i++) {
            VPitem vPitem = new VPitem(images[i], headers[i], descriptions[i]);
            vPitemArrayList.add(vPitem);
        }

        VPadapter vPadapter = new VPadapter(vPitemArrayList);
        vpInfo.setAdapter(vPadapter);
        vpInfo.setClipToPadding(false);
        vpInfo.setClipChildren(false);
        vpInfo.setOffscreenPageLimit(2);
        vpInfo.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);
    }

    protected void setListeners() {
        btnStartLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLogin = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intentLogin);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        btnStartRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRegister = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intentRegister);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        vpInfo.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                pageScrolled(position, positionOffset);
            }
        });
    }

    private void pageScrolled(int position, float positionOffset) {
        int[] dots = {R.drawable.vp_dots_left, R.drawable.vp_dots_mid, R.drawable.vp_dots_right
                , R.drawable.vp_dots_left_between, R.drawable.vp_dots_right_between};

        int[] animation = {R.drawable.vp_dots_animation_1, R.drawable.vp_dots_animation_2
                ,R.drawable.vp_dots_animation_3, R.drawable.vp_dots_animation_4};

        if (positionOffset < 0.4) {
            ivDots.setImageResource(dots[position]);
        }
        else if (positionOffset < 0.47) {
            int pos = (2 * position);
            ivDots.setImageResource(animation[pos]);
        }
        else if (positionOffset < 0.53) {
            ivDots.setImageResource(dots[position + 3]);
        }
        else if (positionOffset < 0.6) {
            int pos = (position + 1) * 2;
            ivDots.setImageResource(animation[pos - 1]);
        }
        else {
            ivDots.setImageResource(dots[position + 1]);
        }

    }

    protected void initializeViews() {
        btnStartLogin = findViewById(R.id.btnStartLogin);
        btnStartRegister = findViewById(R.id.btnStartRegister);
        vpInfo = findViewById(R.id.vpInfo);
        ivDots = findViewById(R.id.ivDots);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LOG_OUT_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("checkbox", false);
                editor.putString("uUsername", "");
                editor.putString("uEmail", "");
                editor.putString("uPassword", "");
                editor.apply();
            }
        }
    }
}