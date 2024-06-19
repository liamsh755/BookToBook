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
import java.util.List;
import java.util.Objects;

import co.il.liam.model.Book;
import co.il.liam.model.Books;
import co.il.liam.model.User;

public class UsersRepository {
    private final String COLLECTION_NAME = "Users";
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

        collection = db.collection(COLLECTION_NAME);
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

    public Task<Book> findBookWithLocation(User loggedUser, String stateOrCity, Book book) {
        TaskCompletionSource<Book> foundBooks = new TaskCompletionSource<>();

        String findUserIdFs = book.getUserId();

        collection.whereEqualTo("idFs", findUserIdFs).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                            DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                            if (document != null) {

                                if (Objects.equals(stateOrCity, "city")) {
                                    String bookCity = document.getString("city");
                                    String userCity = loggedUser.getCity();

                                    if (Objects.equals(bookCity, userCity)) {
                                        foundBooks.setResult(book);
                                    }
                                    else {
                                        foundBooks.setResult(null);
                                    }

                                }
                                else {
                                    String bookState = document.getString("state");
                                    String userState = loggedUser.getState();

                                    if (Objects.equals(bookState, userState)) {
                                        foundBooks.setResult(book);
                                    }
                                    else {
                                        foundBooks.setResult(null);
                                    }
                                }
                            }

                            else {
                                foundBooks.setResult(null);
                            }
                        }

                        else {
                            foundBooks.setResult(null);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        foundBooks.setResult(null);
                    }
                });


        return foundBooks.getTask();
    }

    public Task<User> getUserDataByEmail(User user) {
        TaskCompletionSource<User> userData = new TaskCompletionSource<>();

        collection.whereEqualTo("email", user.getEmail()).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                            DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                            if (document != null) {
                                User fullUserData = new User();

                                String username = document.getString("username");
                                String idFs = document.getString("idFs");
                                String state = document.getString("state");
                                String city = document.getString("city");

                                fullUserData.setEmail(user.getEmail());
                                fullUserData.setPassword(user.getPassword());
                                fullUserData.setUsername(username);
                                fullUserData.setIdFs(idFs);
                                fullUserData.setState(state);
                                fullUserData.setCity(city);

                                userData.setResult(fullUserData);

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

    public Task<User> getUserDataByIdFs(User user) {
        TaskCompletionSource<User> userData = new TaskCompletionSource<>();

        collection.whereEqualTo("idFs", user.getIdFs()).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                            DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                            if (document != null) {
                                User fullUserData = new User();

                                String email = document.getString("email");
                                String password = document.getString("password");
                                String username = document.getString("username");
                                String idFs = document.getString("idFs");
                                String state = document.getString("state");
                                String city = document.getString("city");

                                fullUserData.setEmail(email);
                                fullUserData.setPassword(password);
                                fullUserData.setUsername(username);
                                fullUserData.setIdFs(idFs);
                                fullUserData.setState(state);
                                fullUserData.setCity(city);

                                userData.setResult(fullUserData);

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

    public Task<List<String>> getLocations() {
        TaskCompletionSource<List<String>> locations = new TaskCompletionSource<>();

        collection.whereEqualTo("state", "Israel").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {

                            List<String> foundLocations = new ArrayList<>();

                            for (DocumentSnapshot document : queryDocumentSnapshots) {
                                User user = document.toObject(User.class);
                                if (user != null) {
                                    String state = user.getState();
                                    String city = user.getCity();

                                    String location = state + " - " + city;
                                    foundLocations.add(location);
                                }
                            }

                            locations.setResult(foundLocations);
                        }
                        else {
                            locations.setResult(null);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        locations.setResult(null);
                    }
                });

        return locations.getTask();
    }

}