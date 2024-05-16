package co.il.liam.booktobook.ACTIVITIES;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import co.il.liam.booktobook.ADAPTERS.MessagesAdapter;
import co.il.liam.booktobook.R;
import co.il.liam.model.ChatList;
import co.il.liam.model.MessagesList;
import co.il.liam.model.MemoryData;

public class ChatListActivity extends BaseActivity {

    private RecyclerView rvChatListUsers;
    private List<MessagesList> userMessagesList;
    private MessagesAdapter messagesAdapter;

    private String mobileNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        
        initializeViews();
        setListeners();
        setRecyclerViews();
    }

    private void setRecyclerViews() {
        rvChatListUsers.setLayoutManager(new LinearLayoutManager(this));

        // Set adapter to RecyclerView
        messagesAdapter = new MessagesAdapter(userMessagesList, ChatListActivity.this);
        rvChatListUsers.setAdapter(messagesAdapter);
    }

    @Override
    protected void initializeViews() {
        rvChatListUsers = findViewById(R.id.rvChatListUsers);
        userMessagesList = new ArrayList<>();

        // Get logged in user's mobile number from memory
        mobileNumber = MemoryData.getMobile(this);
    }

    @Override
    protected void setListeners() {

    }
}