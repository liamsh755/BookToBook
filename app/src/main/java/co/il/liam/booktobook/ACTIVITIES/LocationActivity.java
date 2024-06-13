package co.il.liam.booktobook.ACTIVITIES;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import co.il.liam.booktobook.R;
import co.il.liam.model.User;

public class LocationActivity extends BaseActivity {
    private TextView tvLocationGoBack;
    private User loggedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        setScreenOrientation();

        initializeViews();
        setListeners();
    }

    @Override
    protected void initializeViews() {
        tvLocationGoBack = findViewById(R.id.tvLocationGoBack);

        Intent userInfoIntent = getIntent();
        loggedUser = (User) userInfoIntent.getSerializableExtra("user");
    }

    @Override
    protected void setListeners() {
        tvLocationGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    @Override
    protected void setScreenOrientation() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}