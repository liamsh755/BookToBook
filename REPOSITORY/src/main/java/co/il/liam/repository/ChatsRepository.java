package co.il.liam.repository;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

import co.il.liam.model.Chat;
import co.il.liam.model.Chats;
import co.il.liam.model.Message;
import co.il.liam.model.User;

public class ChatsRepository {
    private final String COLLECTION_NAME = "Chats";
    private FirebaseFirestore db;
    private CollectionReference collection;

    public ChatsRepository(Context context) {
        try {
            db = FirebaseFirestore.getInstance();
        }
        catch (Exception e) {
            FireBaseInstance instance = FireBaseInstance.instance(context);
            db = FirebaseFirestore.getInstance(FireBaseInstance.app);
        }

        collection = db.collection(COLLECTION_NAME);
    }

    public Task<Boolean> addChat(Chat chat) {
        TaskCompletionSource<Boolean> taskAddedChat = new TaskCompletionSource<Boolean>();

        Task<Boolean> chatExists = chatExists(chat)
                .addOnSuccessListener(new OnSuccessListener<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {

                        if (!aBoolean) {
                            DocumentReference document = collection.document();
                            chat.setIdFs(document.getId());
                            document.set(chat)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            taskAddedChat.setResult(true);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            taskAddedChat.setResult(false);
                                        }
                                    });
                        }
                        else {
                            taskAddedChat.setResult(false);
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        taskAddedChat.setResult(false);
                    }
                });

        return taskAddedChat.getTask();
    }

    public Task<Boolean> chatExists(Chat chat) {

        User userOne = chat.getUserOne();
        User userTwo = chat.getUserTwo();

        TaskCompletionSource<Boolean> taskChatExists = new TaskCompletionSource<Boolean>();

        collection.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                            Boolean found = false;

                            for (DocumentSnapshot document : queryDocumentSnapshots) {
                                Chat chat = document.toObject(Chat.class);
                                if (chat != null) {
                                    User foundChatUserOne = chat.getUserOne();
                                    User foundChatUserTwo = chat.getUserTwo();

                                    if (userOne.equals(foundChatUserOne) && userTwo.equals(foundChatUserTwo)
                                            || userOne.equals(foundChatUserTwo) && userTwo.equals(foundChatUserOne)) {

                                        found = true;
                                    }
                                }
                            }

                            if (found) {
                                taskChatExists.setResult(true);
                            }
                            else {
                                taskChatExists.setResult(false);
                            }

                        }
                        else {
                            taskChatExists.setResult(false);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        taskChatExists.setResult(false);
                    }
                });

//        taskChatExists.setResult(false);

        return taskChatExists.getTask();
    }

    public Task<Chats> getChats(User user) {
        TaskCompletionSource<Chats> foundChats = new TaskCompletionSource<>();
        Chats chats = new Chats();

        collection.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                            for (DocumentSnapshot document : queryDocumentSnapshots) {
                                Chat chat = document.toObject(Chat.class);
                                if (chat != null) {

                                    User foundChatUserOne = chat.getUserOne();
                                    User foundChatUserTwo = chat.getUserTwo();

                                    if (user.equals(foundChatUserTwo) || user.equals(foundChatUserOne))
                                    {
                                        chats.add(chat);
                                    }
                                }
                            }
                            foundChats.setResult(chats);

                        }
                        else {
                            foundChats.setResult(null);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        foundChats.setResult(null);
                    }
                });

        return foundChats.getTask();
    }

    public Task<ArrayList<String>> findEmails(User user) {
        TaskCompletionSource<ArrayList<String>> foundEmails = new TaskCompletionSource<>();

        collection.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ArrayList<String> emails = new ArrayList<>();

                        if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                            for (DocumentSnapshot document : queryDocumentSnapshots) {
                                Chat chat = document.toObject(Chat.class);

                                if (chat != null) {
                                    User foundChatUserOne = chat.getUserOne();
                                    User foundChatUserTwo = chat.getUserTwo();

                                    if (foundChatUserOne.equals(user)) {
                                        emails.add(foundChatUserTwo.getEmail());
                                    }
                                    else if (foundChatUserTwo.equals(user)) {
                                        emails.add(foundChatUserOne.getEmail());
                                    }
                                }
                            }

                            foundEmails.setResult(emails);

                        }
                        else {
                            foundEmails.setResult(null);
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        foundEmails.setResult(null);
                    }
                });

        return foundEmails.getTask();
    }

    public Task<Boolean> deleteChat(Chat chat) {
        TaskCompletionSource<Boolean> removedChat = new TaskCompletionSource<>();

        DocumentReference document = collection.document(chat.getIdFs());
        document.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        removedChat.setResult(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        removedChat.setResult(false);
                    }
                });

        return removedChat.getTask();
    }

    public Task<Boolean> updateLastMessage(Chat chat, Message message) {
        String sender = message.getSender().getUsername();
        String recipient = message.getRecipient().getUsername();

        String showLastMessage = message.getContent();
        String firstWord = showLastMessage.split(" ")[0];

        if ((Objects.equals(firstWord, sender) || Objects.equals(firstWord, recipient))
                && showLastMessage.contains("at")
                && showLastMessage.contains(":")
                && showLastMessage.contains("/")
                && showLastMessage.contains("said")
                && showLastMessage.contains("\""))
        {
            showLastMessage = sender + " quoted " + firstWord;
        }
        else {
            showLastMessage = sender + ": " + showLastMessage;
        }

        TaskCompletionSource<Boolean> updatedLastMessage = new TaskCompletionSource<>();

        String finalShowLastMessage = showLastMessage;
        collection.whereEqualTo("idFs", chat.getIdFs()).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                            DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                            if (document != null) {

                                updatedLastMessage.setResult(true);
                                DocumentReference documentRef = document.getReference();

                                documentRef.update("lastMessage", finalShowLastMessage)
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                updatedLastMessage.setResult(false);
                                            }
                                        });

                                documentRef.update("lastTime", message.getTime())
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                updatedLastMessage.setResult(false);
                                            }
                                        });

                                documentRef.update("lastDate", message.getDate())
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                updatedLastMessage.setResult(false);
                                            }
                                        });

                            }

                            else {
                                updatedLastMessage.setResult(false);
                            }
                        }

                        else {
                            updatedLastMessage.setResult(false);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        updatedLastMessage.setResult(false);
                    }
                });

        return updatedLastMessage.getTask();
    }

}
