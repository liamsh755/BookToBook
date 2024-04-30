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

import java.util.Objects;

import co.il.liam.model.User;

public class UsersRepository {
    private FirebaseFirestore db;
    private CollectionReference collection;
    public UsersRepository(Context context) {

        try {
            db = FirebaseFirestore.getInstance();
        }
        catch (Exception e) {
            FireBaseInstance instance = FireBaseInstance.instance(context);
            db = FirebaseFirestore.getInstance(FireBaseInstance.app);
        }

        collection = db.collection("Users");
    }

    public Task<Boolean> add (User user) {
        TaskCompletionSource<Boolean> taskAddedUser = new TaskCompletionSource<Boolean>();

        Task<Boolean> exists = emailExists(user)
                .addOnSuccessListener(new OnSuccessListener<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        if (!aBoolean){
                            DocumentReference document = collection.document();
                            user.setIdFs(document.getId());
                            document.set(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            taskAddedUser.setResult(true);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            taskAddedUser.setResult(false);
                                        }
                                    });
                        }
                        else {
                            taskAddedUser.setResult(false);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        taskAddedUser.setResult(false);
                    }
                });


        return taskAddedUser.getTask();
    }

    public   Task<Boolean> emailExists(User user) {
        TaskCompletionSource<Boolean> taskExist = new TaskCompletionSource<>();

        collection.whereEqualTo("email", user.getEmail()).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                            DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);

                            if (document != null) {
                                taskExist.setResult(true);
                            }
                            else
                                taskExist.setResult(false);
                        } else {
                            taskExist.setResult(false);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        taskExist.setResult(false);
                    }
                });


        return taskExist.getTask();
    }

    public   Task<Boolean> userDetailsRight(User user) {
        TaskCompletionSource<Boolean> detailsRight = new TaskCompletionSource<>();

        collection.whereEqualTo("email", user.getEmail()).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                            DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);

                            if (document != null) {
                                String dataPassword = document.getString("password");
                                String givenPassword = user.getPassword();

                                if (Objects.equals(dataPassword, givenPassword))
                                    detailsRight.setResult(true);
                                else
                                    detailsRight.setResult(false);
                            }
                            else
                                detailsRight.setResult(false);
                        } else {
                            detailsRight.setResult(false);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        detailsRight.setResult(false);
                    }
                });


        return detailsRight.getTask();
    }

    public Task<String> getUserData(User user) {
        TaskCompletionSource<String> userData = new TaskCompletionSource<>();

        collection.whereEqualTo("email", user.getEmail()).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                            DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                            if (document != null) {

                                String username = document.getString("username");
                                userData.setResult(username);

                            }
                            else
                                userData.setResult(null);

                        } else {
                            userData.setResult(null);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        userData.setResult(null);
                    }
                });


        return userData.getTask();
    }

    public Task<Boolean> changePassword(String email, String newPassword) {
        TaskCompletionSource<Boolean> changedPassword = new TaskCompletionSource<>();

        collection.whereEqualTo("email", email).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                            DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                            if (document != null) {
                                DocumentReference documentReference = document.getReference();
                                documentReference.update("password", newPassword)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                changedPassword.setResult(true);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                changedPassword.setResult(false);
                                            }
                                        });
                            }
                            else
                                changedPassword.setResult(false);

                        } else {
                            changedPassword.setResult(false);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        changedPassword.setResult(false);
                    }
                });

        return changedPassword.getTask();
    }
}