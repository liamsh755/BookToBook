package co.il.liam.booktobook.ACTIVITIES;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.Guideline;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import co.il.liam.booktobook.ADAPTERS.ChatAdapter;
import co.il.liam.booktobook.CheckInternetConnection;
import co.il.liam.booktobook.R;
import co.il.liam.model.Chat;
import co.il.liam.model.Chats;
import co.il.liam.model.User;
import co.il.liam.viewmodel.ChatsViewModel;
import co.il.liam.viewmodel.MessagesViewModel;

public class ChatListActivity extends BaseActivity {
    private final int ANIMATION_DURATION = 400;
    private TextView tvChatListGoBack;
    private Guideline glChatList7;
    private Guideline glChatList50;
    private Guideline glChatList92;
    private View vChatListTopGradient;
    private View vChatListBottomGradient;
    private TextView tvChatListNoChats;
    private RecyclerView rvChatListChats;
    private TextView tvChatListNameDisplay;

    private ChatAdapter chatAdapter;

    private Handler handler = new Handler();

    private User loggedUser;
    private ChatsViewModel chatsViewModel;
    private MessagesViewModel messagesViewModel;
    private Chats chats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        initializeViews();
        setListeners();
        waitBeforeOpening(ANIMATION_DURATION);
        setNameDisplay();
        setRecyclerView();
        setObservers();
        setChats();

        //checks internet connection
        CheckInternetConnection.check(this);
    }

    private void setObservers() {
        chatsViewModel = new ViewModelProvider(this).get(ChatsViewModel.class);

        chatsViewModel.getFoundChats().observe(this, new Observer<Chats>() {
            @Override
            public void onChanged(Chats foundChats) {
                if (foundChats != null) {
                    chats = foundChats;
                    chatAdapter.setChats(chats);
                    emptyMessage(chats);
                }
                else {
                    chats = new Chats();
                    chatAdapter.setChats(chats);
                    emptyMessage(chats);
                }
            }
        });


        messagesViewModel = new ViewModelProvider(this).get(MessagesViewModel.class);

        messagesViewModel.getDeletedAll().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (!aBoolean) {
                    Toast.makeText(ChatListActivity.this, "Chat messages deletion failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //animation
    private void waitBeforeOpening(int milliseconds) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startOpeningAnimation();
            }
        }, milliseconds);
    }

    private void startOpeningAnimation() {
        vChatListTopGradient.post(() -> {
            float topViewInitialY = vChatListTopGradient.getY();
            float topViewTargetY = glChatList7.getY() - vChatListTopGradient.getHeight();

            float bottomViewInitialY = vChatListBottomGradient.getY();
            float bottomViewTargetY = glChatList92.getY();

            ObjectAnimator topAnimator = ObjectAnimator.ofFloat(vChatListTopGradient, "y", topViewInitialY, bottomViewTargetY);
            topAnimator.setDuration(ANIMATION_DURATION);

            ObjectAnimator bottomAnimator = ObjectAnimator.ofFloat(vChatListBottomGradient, "y", bottomViewInitialY, topViewTargetY);
            bottomAnimator.setDuration(ANIMATION_DURATION);

            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(topAnimator, bottomAnimator);
            animatorSet.start();

            animatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {

                    fadeIn(tvChatListGoBack, ANIMATION_DURATION);
                    fadeIn(rvChatListChats, ANIMATION_DURATION);
                    fadeIn(tvChatListNameDisplay, ANIMATION_DURATION);
                    if (tvChatListNoChats.getVisibility() == View.VISIBLE) {
                        fadeIn(tvChatListNoChats, ANIMATION_DURATION);
                    }
                }
            });

        });
    }
    private void startLeavingAnimation() {
        vChatListTopGradient.post(() -> {
            // Get the positions of the guidelines
            float guideline50pY = glChatList50.getY();

            // Calculate the target positions for the top and bottom views
            float topViewInitialY = vChatListTopGradient.getY();
            float topViewTargetY = guideline50pY;

            float bottomViewInitialY = vChatListBottomGradient.getY();
            float bottomViewTargetY = guideline50pY - vChatListBottomGradient.getHeight();

            // Create the animators
            ObjectAnimator topAnimator = ObjectAnimator.ofFloat(vChatListTopGradient, "y", topViewInitialY, topViewTargetY);
            topAnimator.setDuration(ANIMATION_DURATION);

            ObjectAnimator bottomAnimator = ObjectAnimator.ofFloat(vChatListBottomGradient, "y", bottomViewInitialY, bottomViewTargetY);
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


    //initializing
    @Override
    protected void initializeViews() {
        tvChatListGoBack = findViewById(R.id.tvChatListGoBack);
        glChatList7 = findViewById(R.id.glChatList7);
        glChatList50 = findViewById(R.id.glChatList50);
        glChatList92 = findViewById(R.id.glChatList92);
        vChatListTopGradient = findViewById(R.id.vChatListBottomGradient);
        vChatListBottomGradient = findViewById(R.id.vChatListTopGradient);
        tvChatListNoChats = findViewById(R.id.tvChatListChat);
        rvChatListChats = findViewById(R.id.rvChatListChats);
        tvChatListNameDisplay = findViewById(R.id.tvChatListNameDisplay);

        handler = new Handler();
    }
    @Override
    protected void setListeners() {
        tvChatListGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fadeOut(tvChatListGoBack, ANIMATION_DURATION);
                if (tvChatListNoChats.getVisibility() == View.VISIBLE) {
                    fadeOut(tvChatListNoChats, ANIMATION_DURATION);
                }
                fadeOut(rvChatListChats, ANIMATION_DURATION);
                fadeOut(tvChatListNameDisplay, ANIMATION_DURATION);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startLeavingAnimation();
                    }
                }, ANIMATION_DURATION);

            }
        });

    }
    private void setNameDisplay() {
        Intent  userInfoIntent = getIntent();
        loggedUser = (User) userInfoIntent.getSerializableExtra("user");

        assert loggedUser != null;
        String name = loggedUser.getUsername();
        tvChatListNameDisplay.setText("Hello " + name + "!");
    }

    private void setRecyclerView() {
        ChatAdapter.OnItemClickListener listener = new ChatAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(Chat chat, int position) {
                Intent goChat = new Intent(getApplicationContext(), ChatActivity.class);
                goChat.putExtra("chat", chat);
                goChat.putExtra("loggedUser", loggedUser);
                startActivityForResult(goChat, 1);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        };

        ChatAdapter.OnItemLongClickListener longListener = new ChatAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClicked(Chat chat) {
                String recipientUsername = "";
                if (chat.getUserOne().getUsername().equals(loggedUser.getUsername())) {
                    recipientUsername = chat.getUserTwo().getUsername();
                }
                else {
                    recipientUsername = chat.getUserOne().getUsername();
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(ChatListActivity.this);
                builder.setTitle("Stop chatting with " + recipientUsername + "?");
                builder.setMessage("You won't be able to chat to this person until you find their books and message them again.");
                builder.setCancelable(true);
                builder.setNegativeButton("Keep chatting ", null);
                builder.setPositiveButton("Delete chat", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (CheckInternetConnection.check(ChatListActivity.this)) {
                            chats.remove(chat);
                            chatAdapter.setChats(chats);
                            emptyMessage(chats);
                            chatsViewModel.deleteChat(chat);
                            messagesViewModel.deleteChatsMessages(chat);
                        }
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

                return true;
            }
        };

        chatAdapter = new ChatAdapter(this, R.layout.chat_single_layout, new Chats(), loggedUser, listener, longListener);
        rvChatListChats.setAdapter(chatAdapter);
        rvChatListChats.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void setChats() {
//        User userBob = new User();
//        userBob.setUsername("Bob");
//        userBob.setEmail("BOB@gmail.com");
//        userBob.setPassword("123456");
//        userBob.setIdFs("BBBBBBBBBBBBBBBBBB");
//        userBob.setState("Israel");
//        userBob.setCity("Jerusalem");
//
//        User userAlice = new User();
//        userAlice.setUsername("Alice");
//        userAlice.setEmail("ALICE@gmail.com");
//        userAlice.setPassword("654321");
//        userAlice.setIdFs("AAAAAAAAAAAAAAAAAAAA");
//        userAlice.setState("Israel");
//        userAlice.setCity("Kfar Saba");
//
//        Chat chat1 = new Chat();
//        chat1.setUserOne(loggedUser);
//        chat1.setUserTwo(userBob);
//        chat1.setLastMessage("Hey hey HEYYYY");
//        chat1.setLastDate("3/6/2024");
//        chat1.setLastTime("12:34");
//
//        Chat chat2 = new Chat();
//        chat2.setUserOne(userAlice);
//        chat2.setUserTwo(loggedUser);
//        chat2.setLastMessage("ok sure I'll meet you there");
//        chat2.setLastDate("4/6/2024");
//        chat2.setLastTime("10:00");
//
//        chats = new Chats();
//        chats.add(chat1);
//        chats.add(chat2);
//
//        chatAdapter.setChats(chats);


        chats = new Chats();

        if (CheckInternetConnection.check(this)) {
            chatsViewModel.getChats(loggedUser);
        }
    }

    private void emptyMessage(Chats chats) {
        if (chats == null || chats.isEmpty()) {
            tvChatListNoChats.setVisibility(View.VISIBLE);
        } else {
            tvChatListNoChats.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {

                chatsViewModel.getChats(loggedUser);
            }
        }
    }
}