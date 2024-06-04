package co.il.liam.booktobook.ACTIVITIES;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import co.il.liam.booktobook.R;
import co.il.liam.model.Chat;
import co.il.liam.model.User;

public class ChatActivity extends BaseActivity {
    private TextView tvChatGoBack;
    private TextView tvChatRecipient;
    private EditText etChatMessage;
    private ImageView ivChatSendMessage;

    private Chat chat;
    private User loggedUser;
    private User recipientUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initializeViews();
        setListeners();
        loadData();
    }

    private void loadData() {
        chat = (Chat) getIntent().getSerializableExtra("chat");
        loggedUser = (User) getIntent().getSerializableExtra("loggedUser");

        if (loggedUser.equals(chat.getUserOne())) {
            recipientUser = chat.getUserTwo();
        }
        else {
            recipientUser = chat.getUserOne();
        }

        tvChatRecipient.setText(recipientUser.getUsername());
    }

    @Override
    protected void initializeViews() {
        tvChatGoBack = findViewById(R.id.tvChatGoBack);
        tvChatRecipient = findViewById(R.id.tvChatRecipient);
        etChatMessage = findViewById(R.id.etChatMessage);
        ivChatSendMessage = findViewById(R.id.ivChatSendMessage);
    }

    @Override
    protected void setListeners() {
        tvChatGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goBackIntent = new Intent(getApplicationContext(), MainScreenActivity.class);
                startActivity(goBackIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }
}