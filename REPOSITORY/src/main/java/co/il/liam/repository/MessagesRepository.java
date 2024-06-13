package co.il.liam.repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import co.il.liam.model.Chat;
import co.il.liam.model.Message;
import co.il.liam.model.Messages;
import co.il.liam.model.User;

public class MessagesRepository {
    private final String COLLECTION_NAME = "Messages";
    private FirebaseFirestore db;
    private CollectionReference collection;

    public MessagesRepository(Context context) {
        try {
            db = FirebaseFirestore.getInstance();
        }
        catch (Exception e) {
            FireBaseInstance instance = FireBaseInstance.instance(context);
            db = FirebaseFirestore.getInstance(FireBaseInstance.app);
        }

        collection = db.collection(COLLECTION_NAME);
    }

    public Task<Boolean> addMessage(Message message) {
        TaskCompletionSource<Boolean> taskAddedMessage = new TaskCompletionSource<Boolean>();

        DocumentReference document = collection.document();
        message.setIdFs(document.getId());
        document.set(message)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        taskAddedMessage.setResult(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        taskAddedMessage.setResult(false);
                    }
                });

        return taskAddedMessage.getTask();
    }

    public LiveData<Messages> listenChatsMessages (Chat chat) {
        User userOne = chat.getUserOne();
        User userTwo = chat.getUserTwo();

        MutableLiveData<Messages> chatsMessages = new MutableLiveData<>();

        collection.orderBy("content", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            chatsMessages.setValue(null);
                        }

                        else if (value != null && !value.isEmpty()) {
                            Messages messages = new Messages();
                            for (DocumentSnapshot document : value) {
                                Message message = document.toObject(Message.class);
                                if (message != null) {
                                    User messageUserOne = message.getSender();
                                    User messageUserTwo = message.getRecipient();

                                    if (messageUserOne.equals(userOne) && messageUserTwo.equals(userTwo)
                                            || messageUserOne.equals(userTwo) && messageUserTwo.equals(userOne)) {
                                        messages.add(message);
                                    }
                                }
                            }
                            chatsMessages.setValue(messages);
                        }

                        else {
                            chatsMessages.setValue(null);
                        }
                    }
                });

        return chatsMessages;
    }

    public Task<Messages> findChatsMessages (Chat chat) {
        User userOne = chat.getUserOne();
        User userTwo = chat.getUserTwo();

        TaskCompletionSource<Messages> chatsMessages = new TaskCompletionSource<Messages>();
        Messages messages = new Messages();

        collection.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                            for (DocumentSnapshot document : queryDocumentSnapshots) {
                                Message message = document.toObject(Message.class);
                                if (message != null) {

                                    User messageUserOne = message.getSender();
                                    User messageUserTwo = message.getRecipient();

                                    if (messageUserOne.equals(userOne) && messageUserTwo.equals(userTwo)
                                            || messageUserOne.equals(userTwo) && messageUserTwo.equals(userOne)) {
                                        messages.add(message);
                                    }
                                }
                            }
                            chatsMessages.setResult(messages);
                        }

                        else {
                            chatsMessages.setResult(null);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        chatsMessages.setResult(null);
                    }
                });

        return chatsMessages.getTask();
    }

    public Task<Boolean> deleteAll(Chat chat) {
        TaskCompletionSource<Boolean> deletedAll = new TaskCompletionSource<Boolean>();

        User userOne = chat.getUserOne();
        User userTwo = chat.getUserTwo();

        collection.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                            deletedAll.setResult(true);
                            for (DocumentSnapshot document : queryDocumentSnapshots) {
                                Message message = document.toObject(Message.class);
                                if (message != null) {

                                    User messageUserOne = message.getSender();
                                    User messageUserTwo = message.getRecipient();

                                    if (messageUserOne.equals(userOne) && messageUserTwo.equals(userTwo)
                                            || messageUserOne.equals(userTwo) && messageUserTwo.equals(userOne)) {

                                        DocumentReference documentReference = collection.document(message.getIdFs());
                                        documentReference.delete()
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        deletedAll.setResult(false);
                                                    }
                                                });
                                    }
                                }
                            }
                        }

                        else {
                            deletedAll.setResult(false);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        deletedAll.setResult(false);
                    }
                });

        return deletedAll.getTask();
    }
}
