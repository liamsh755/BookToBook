package co.il.liam.repository;

import android.content.Context;

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
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
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

    public Task<Books> getAll(User user) {
        TaskCompletionSource<Books> foundBooks = new TaskCompletionSource<>();

        Books books = new Books();
        String userId = user.getIdFs();

        collection.whereEqualTo("userId", userId).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        List<Task<Void>> storageTasks = new ArrayList<>();

                        if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()){
                            for (DocumentSnapshot document : queryDocumentSnapshots ) {
                                Book book = document.toObject(Book.class);
                                if (book != null) {

                                    TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();
                                    storageTasks.add(taskCompletionSource.getTask());

                                    FireBaseStorage.loadFromStorage(book.getIdFs(), path)
                                            .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                                @Override
                                                public void onSuccess(byte[] bytes) {
                                                    book.setImage(BitMapHelper.encodeTobase64(BitMapHelper.byteArrayToBitmap(bytes)));
                                                    books.add(book);
                                                    taskCompletionSource.setResult(null);
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    book.setImage("");
                                                    books.add(book);
                                                    taskCompletionSource.setResult(null);
                                                }
                                            });
                                }

                            }

                            Tasks.whenAll(storageTasks)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            foundBooks.setResult(books);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            foundBooks.setResult(null);
                                        }
                                    });
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
}
