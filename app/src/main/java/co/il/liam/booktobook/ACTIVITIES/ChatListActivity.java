package co.il.liam.booktobook.ACTIVITIES;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
    private EditText etChatListSearch;
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
        setScreenOrientation();

        initializeViews();
        setListeners();
        setNameDisplay();
        setRecyclerView();
        setObservers();
        setChats();
        waitBeforeOpening(ANIMATION_DURATION);

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
                }
                else {
                    chats = new Chats();
                    chatAdapter.setChats(chats);
                }
            }
        });

        chatsViewModel.getDeletedChat().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    Toast.makeText(ChatListActivity.this, "Chat deleted", Toast.LENGTH_SHORT).show();
                }
            }
        });


        messagesViewModel = new ViewModelProvider(this).get(MessagesViewModel.class);

        messagesViewModel.getDeletedAll().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (!aBoolean) {
                    Log.d("qqq", "either there were no messages to delete which is ok or failed to delete messages which is bad");
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
                    fadeIn(etChatListSearch, ANIMATION_DURATION);
                    emptyMessage(chats);
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
        etChatListSearch = findViewById(R.id.etChatListSearch);
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
                fadeOut(etChatListSearch, ANIMATION_DURATION);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startLeavingAnimation();
                    }
                }, ANIMATION_DURATION);

            }
        });

        etChatListSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()) {
                    chatAdapter.setChats(chats);
                }

                else {
                    String search = s.toString();

                    Chats filteredChats = new Chats();
                    for (Chat chat : chats) {

                        User userOne = chat.getUserOne();
                        User userTwo = chat.getUserTwo();

                        if (userOne.equals(loggedUser)) {
                            if (userTwo.getUsername().length() > search.length() && userTwo.getUsername().toLowerCase().startsWith(search.toLowerCase())) {
                                filteredChats.add(chat);
                            }
                        }
                        else {
                            if (userOne.getUsername().length() > search.length() && userOne.getUsername().toLowerCase().startsWith(search.toLowerCase())) {
                                filteredChats.add(chat);
                            }
                        }
                    }

                    chatAdapter.setChats(filteredChats);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    protected void setScreenOrientation() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
                etChatListSearch.clearFocus();
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
                User recipientUser = new User();
                if (chat.getUserOne().getUsername().equals(loggedUser.getUsername())) {
                    recipientUser = chat.getUserTwo();
                }
                else {
                    recipientUser = chat.getUserOne();
                }

                AlertDialog.Builder deleteDialogBuilder = new AlertDialog.Builder(ChatListActivity.this);
                deleteDialogBuilder.setTitle("Stop chatting with " + recipientUser.getUsername() + "?");
                deleteDialogBuilder.setMessage("You won't be able to chat to this person until you find their books and message them again.");
                deleteDialogBuilder.setCancelable(true);
                deleteDialogBuilder.setNegativeButton("Keep chatting ", null);
                deleteDialogBuilder.setPositiveButton("Delete chat", new DialogInterface.OnClickListener() {
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

                AlertDialog deleteDialog = deleteDialogBuilder.create();


                AlertDialog.Builder reportDialogBuilder = new AlertDialog.Builder(ChatListActivity.this);
                reportDialogBuilder.setTitle("Are you sure you want to report " + recipientUser.getUsername() + "?");
                reportDialogBuilder.setMessage("You're welcome to delete the chat instead. If the problem persists please do report!");
                reportDialogBuilder.setCancelable(true);
                reportDialogBuilder.setNegativeButton("Delete chat ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteDialog.show();
                    }
                });
                User finalRecipientUser = recipientUser;
                reportDialogBuilder.setPositiveButton("Report user", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "BookToBookCS@gmail.com" });
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Reporting a BookToBook user");
                        intent.putExtra(Intent.EXTRA_TEXT, "Hello I want to report the user " + finalRecipientUser.getUsername()
                                + " with the email " + finalRecipientUser.getEmail() + "\n\n<<PLEASE ADD MORE INFORMATION ABOUT THE REASON FOR THE REPORT>>");
                        intent.setType("message/rfc822");
                        startActivity(intent.createChooser(intent, "Choose an email client:"));
                    }
                });

                AlertDialog reportDialog = reportDialogBuilder.create();


                AlertDialog.Builder actionsDialogBuilder = new AlertDialog.Builder(ChatListActivity.this);
                actionsDialogBuilder.setTitle("Choosing an action");
                actionsDialogBuilder.setMessage("What action would you like to do?");
                actionsDialogBuilder.setCancelable(true);
                actionsDialogBuilder.setNegativeButton("Report user", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        reportDialog.show();
                    }
                });
                actionsDialogBuilder.setPositiveButton("Delete chat", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteDialog.show();
                    }
                });

                AlertDialog actionsDialog = actionsDialogBuilder.create();
                actionsDialog.show();

                return true;
            }
        };

        chatAdapter = new ChatAdapter(this, R.layout.chat_single_layout, new Chats(), loggedUser, listener, longListener);
        rvChatListChats.setAdapter(chatAdapter);
        rvChatListChats.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void setChats() {

        chats = new Chats();

        if (CheckInternetConnection.check(this)) {
            chatsViewModel.getChats(loggedUser);
        }
    }

    private void emptyMessage(Chats chats) {
        if (chats == null || chats.isEmpty()) {
            fadeIn(tvChatListNoChats, ANIMATION_DURATION);
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

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Go back Confirmation")
                .setMessage("Are you sure you want to go back?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fadeOut(tvChatListGoBack, ANIMATION_DURATION);
                        if (tvChatListNoChats.getVisibility() == View.VISIBLE) {
                            fadeOut(tvChatListNoChats, ANIMATION_DURATION);
                        }
                        fadeOut(rvChatListChats, ANIMATION_DURATION);
                        fadeOut(tvChatListNameDisplay, ANIMATION_DURATION);
                        fadeOut(etChatListSearch, ANIMATION_DURATION);

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startLeavingAnimation();
                            }
                        }, ANIMATION_DURATION);

                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}