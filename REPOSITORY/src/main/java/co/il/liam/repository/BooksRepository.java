package co.il.liam.repository;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import co.il.liam.helper.BitMapHelper;
import co.il.liam.model.Book;
import co.il.liam.model.Books;
import co.il.liam.model.User;

public class BooksRepository {
    private final String COLLECTION_NAME = "Books";
    private FirebaseFirestore db;
    private CollectionReference collection;
    private String path;
    private FireBaseStorage storage;

    public BooksRepository(Context context) {
        try {
            db = FirebaseFirestore.getInstance();
        }
        catch (Exception e) {
            FireBaseInstance instance = FireBaseInstance.instance(context);
            db = FirebaseFirestore.getInstance(FireBaseInstance.app);

            storage = new FireBaseStorage(context);
        }

        collection = db.collection(COLLECTION_NAME);
        path = COLLECTION_NAME;
    }

    public Task<Boolean> addBook(Book book) {
        TaskCompletionSource<Boolean> successfullyAdd = new TaskCompletionSource<>();

        DocumentReference document = collection.document();
        book.setIdFs(document.getId());

        FireBaseStorage.saveToStorage(book.getIdFs(), BitMapHelper.bitmapToByteArray(BitMapHelper.decodeBase64(book.getImage())), path)
                .addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        book.setImageUrl(s);
                        document.set(book)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        successfullyAdd.setResult(true);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        successfullyAdd.setResult(false);
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        successfullyAdd.setResult(false);
                    }
                });


        return successfullyAdd.getTask();
    }
    public Task<Boolean> updateBook(Book book) {
        TaskCompletionSource<Boolean> successfullyUpdate = new TaskCompletionSource<>();

        DocumentReference document = collection.document(book.getIdFs());

        document.set(book)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        successfullyUpdate.setResult(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        successfullyUpdate.setResult(false);
                    }
                });

        return successfullyUpdate.getTask();
    }

    public Task<Boolean> deleteBook(Book book) {
        TaskCompletionSource deletedBooK = new TaskCompletionSource<>();

        DocumentReference document = collection.document(book.getIdFs());

        FireBaseStorage.deleteFromStorage(book.getIdFs(), path)
                .addOnSuccessListener(new OnSuccessListener<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        document.delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        deletedBooK.setResult(true);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        deletedBooK.setResult(false);
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        deletedBooK.setResult(false);
                    }
                });

        return deletedBooK.getTask();
    }

    public Task<Books> getAll(User user){
        TaskCompletionSource<Books> foundBooks = new TaskCompletionSource<>();
        Books books = new Books();
        String userId = user.getIdFs();

        collection.whereEqualTo("userId", userId).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Task<Void>> storageTasks = new ArrayList<>();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Book book = document.toObject(Book.class);
                        Log.d("qqq", "found book: " + book.getExchange().toString() + ", " + book.getCondition().toString());
                        if (book != null && !book.getExchange().toString().equals("FOR_DISPLAY")) {
                            TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();
                            storageTasks.add(taskCompletionSource.getTask());

                            FireBaseStorage.loadFromStorage(book.getIdFs(), path)
                                    .addOnSuccessListener(bytes -> {
                                        book.setImage(BitMapHelper.encodeTobase64(BitMapHelper.byteArrayToBitmap(bytes)));
                                        books.add(book);
                                        Log.d("qqq", "added book " + books.size() + " : " + book.getExchange().toString() + ", " + book.getCondition().toString());
                                        taskCompletionSource.setResult(null);
                                    })
                                    .addOnFailureListener(e -> {
                                        book.setImage("");
                                        books.add(book);
                                        taskCompletionSource.setResult(null);
                                    });
                        }
                    }

                    // Wait for all storage tasks to complete
                    Tasks.whenAll(storageTasks)
                            .addOnSuccessListener(aVoid -> {
                                Collections.sort(books, (book1, book2) -> book1.getTitle().compareTo(book2.getTitle()));
                                foundBooks.setResult(books);
                            })
                            .addOnFailureListener(e -> {
                                // Handle failure to load pictures
                                foundBooks.setResult(null);
                                foundBooks.setException(e);
                            });
                })


                .addOnFailureListener(e -> {
                    // Handle failure to retrieve members from Firestore
                    foundBooks.setResult(null);
                    foundBooks.setException(e);
                });

        return foundBooks.getTask();
    }

    public Task<Books> getAllOtherBooks(User user){
        TaskCompletionSource<Books> foundBooks = new TaskCompletionSource<>();
        Books books = new Books();
        String userId = user.getIdFs();

        collection.whereNotEqualTo("userId", userId).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Task<Void>> storageTasks = new ArrayList<>();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Book book = document.toObject(Book.class);
                        Log.d("qqq", "found book: " + book.getExchange().toString() + ", " + book.getCondition().toString());
                        if (book != null && !book.getExchange().toString().equals("FOR_DISPLAY")) {
                            TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();
                            storageTasks.add(taskCompletionSource.getTask());

                            FireBaseStorage.loadFromStorage(book.getIdFs(), path)
                                    .addOnSuccessListener(bytes -> {
                                        book.setImage(BitMapHelper.encodeTobase64(BitMapHelper.byteArrayToBitmap(bytes)));
                                        books.add(book);
                                        Log.d("qqq", "added book " + books.size() + " : " + book.getExchange().toString() + ", " + book.getCondition().toString());
                                        taskCompletionSource.setResult(null);
                                    })
                                    .addOnFailureListener(e -> {
                                        book.setImage("");
                                        books.add(book);
                                        taskCompletionSource.setResult(null);
                                    });
                        }
                    }

                    // Wait for all storage tasks to complete
                    Tasks.whenAll(storageTasks)
                            .addOnSuccessListener(aVoid -> {
                                Collections.sort(books, (book1, book2) -> book1.getTitle().compareTo(book2.getTitle()));
                                foundBooks.setResult(books);
                            })
                            .addOnFailureListener(e -> {
                                // Handle failure to load pictures
                                foundBooks.setResult(null);
                                foundBooks.setException(e);
                            });
                })


                .addOnFailureListener(e -> {
                    // Handle failure to retrieve members from Firestore
                    foundBooks.setResult(null);
                    foundBooks.setException(e);
                });

        return foundBooks.getTask();
    }
}
