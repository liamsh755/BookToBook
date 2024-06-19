package co.il.liam.booktobook.ACTIVITIES;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import co.il.liam.booktobook.ADAPTERS.MessagesAdapter;
import co.il.liam.booktobook.R;
import co.il.liam.model.Chat;
import co.il.liam.model.Message;
import co.il.liam.model.Messages;
import co.il.liam.model.User;
import co.il.liam.viewmodel.ChatsViewModel;
import co.il.liam.viewmodel.MessagesViewModel;

public class ChatActivity extends BaseActivity {
    private TextView tvChatGoBack;
    private TextView tvChatRecipient;
    private RecyclerView rvChatMessages;
    private EditText etChatMessage;
    private ImageView ivChatSendMessage;
    private ProgressBar pbChat;

    private Chat chat;
    private User loggedUser;
    private User recipientUser;

    private Messages messages;
    private MessagesAdapter messagesAdapter;
    private MessagesViewModel messagesViewModel;
    private ChatsViewModel chatsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        setScreenOrientation();

        initializeViews();
        setListeners();
        loadData();
        setObservers();
        setMessages();
        setRecyclerView();
    }

    private void setObservers() {
        messagesViewModel = new ViewModelProvider(this).get(MessagesViewModel.class);

        messagesViewModel.getAddedMessage().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (!aBoolean) {
                    Log.d("qqq", "failed to add messages which is bad");
                }
            }
        });

        messagesViewModel.getChatsMessages().observe(this, new Observer<Messages>() {
            @Override
            public void onChanged(Messages foundMessages) {
                pbChat.setVisibility(View.INVISIBLE);
                if (foundMessages != null) {
                    messages = foundMessages;
                    messages.sortByTime();
                    messagesAdapter.setMessages(messages);

                    rvChatMessages.scrollToPosition(messagesAdapter.getItemCount() - 1);
                }
                else {
                    Log.d("qqq", "either no messages exist which is ok or failed to load messages which is bad");
                }
            }
        });

        messagesViewModel.getListener(chat).observe(this, new Observer<Messages>() {
            @Override
            public void onChanged(Messages foundMessages) {
                pbChat.setVisibility(View.INVISIBLE);
                if (foundMessages != null) {
                    messages = foundMessages;
                    messages.sortByTime();
                    messagesAdapter.setMessages(messages);

                    rvChatMessages.scrollToPosition(messagesAdapter.getItemCount() - 1);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Failed to load messages", Toast.LENGTH_SHORT).show();
                }
            }
        });

        chatsViewModel = new ViewModelProvider(this).get(ChatsViewModel.class);

        chatsViewModel.getUpdatedLastMessage().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (!aBoolean) {
                    Toast.makeText(getApplicationContext(), "Failed to update last message", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setRecyclerView() {
        MessagesAdapter.OnItemClickListener listener = new MessagesAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(Message message, int position) {

                String messageContent = message.getContent();

                String reply = message.getSender().getUsername() + " at "
                        + message.getDate() + " " + message.getTime()
                        + " said:\n" + "\"" + messageContent + "\"\n\n";

                if (!etChatMessage.getText().toString().equals(reply)) {
                    etChatMessage.setText(reply);
                    etChatMessage.setSelection(etChatMessage.getText().length());
                    etChatMessage.requestFocus();
                }
                else {
                    etChatMessage.setText("");
                    etChatMessage.clearFocus();
                }
            }
        };

        MessagesAdapter.OnItemLongClickListener longListener = new MessagesAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClicked(Message message) {
                String messageContent = message.getContent();

                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("BTB Chat Message", messageContent);
                clipboard.setPrimaryClip(clip);

                return true;
            }
        };

        messagesAdapter = new MessagesAdapter(this, R.layout.message_single_layout, new Messages(), loggedUser, listener, longListener);
        rvChatMessages.setAdapter(messagesAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvChatMessages.setLayoutManager(linearLayoutManager);
    }

    private void setMessages() {
        messages = new Messages();

        pbChat.setVisibility(View.VISIBLE);
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
        rvChatMessages = findViewById(R.id.rvChatMessages);
        etChatMessage = findViewById(R.id.etChatMessage);
        ivChatSendMessage = findViewById(R.id.ivChatSendMessage);
        pbChat = findViewById(R.id.pbChat);
    }

    @Override
    protected void setListeners() {
        tvChatGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        ivChatSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etChatMessage.getText().toString().isEmpty()) {

                    // Get current date
                    Date currentDate = new Date();

                    // Format date (DD/MM/YYYY)
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    String formattedDate = dateFormat.format(currentDate);

                    // Get current time (HH:mm) in 24-hour format
                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                    String formattedTime = timeFormat.format(currentDate);

                    // Get current seconds in the minute (0-59)
                    Calendar calendar = Calendar.getInstance();
                    int seconds = calendar.get(Calendar.SECOND);

                    // Get contents of the edit text
                    String content = etChatMessage.getText().toString();

                    Message message = new Message(
                            loggedUser,
                            recipientUser,
                            content,
                            formattedTime,
                            seconds,
                            formattedDate
                    );

                    messagesViewModel.addMessage(message);
                    chatsViewModel.updateLastMessage(chat, message);

                    //messagesViewModel.findChatsMessages(chat);

                    etChatMessage.setText("");
                    rvChatMessages.scrollToPosition(messagesAdapter.getItemCount() - 1);
                }
            }
        });

        etChatMessage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    rvChatMessages.scrollToPosition(messagesAdapter.getItemCount() - 1);
                }
            }
        });
    }

    @Override
    protected void setScreenOrientation() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Go back Confirmation")
                .setMessage("Are you sure you want to go back?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setResult(RESULT_OK);
                        finish();
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}