package co.il.liam.booktobook.ACTIVITIES;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import co.il.liam.booktobook.R;

public class LocationActivity extends BaseActivity {
    private TextView tvLocationGoBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        initializeViews();
        setListeners();
    }

    @Override
    protected void initializeViews() {
        tvLocationGoBack = findViewById(R.id.tvLocationGoBack);
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
}