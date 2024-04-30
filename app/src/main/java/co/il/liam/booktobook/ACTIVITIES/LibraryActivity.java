package co.il.liam.booktobook.ACTIVITIES;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import co.il.liam.booktobook.R;

public class LibraryActivity extends BaseActivity {
    private RecyclerView rvLibrary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        initializeViews();
    }

    @Override
    protected void initializeViews() {
        rvLibrary = findViewById(R.id.rvLibrary);
    }

    @Override
    protected void setListeners() {

    }
}