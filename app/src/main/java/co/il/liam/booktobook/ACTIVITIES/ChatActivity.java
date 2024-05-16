package co.il.liam.booktobook.ACTIVITIES;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import co.il.liam.booktobook.R;

public class ChatActivity extends BaseActivity {

    private RecyclerView rvChatMessaging;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initializeViews();
        setListeners();
    }

    @Override
    protected void initializeViews() {
        rvChatMessaging = findViewById(R.id.rvChatMessaging);
    }

    @Override
    protected void setListeners() {

    }
}