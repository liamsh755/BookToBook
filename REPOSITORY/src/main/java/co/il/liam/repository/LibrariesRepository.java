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

import co.il.liam.model.Book;
import co.il.liam.model.Library;
import co.il.liam.model.User;

public class LibrariesRepository {
    private FirebaseFirestore db;
    private CollectionReference collection;

    public LibrariesRepository(Context context) {
        try {
            db = FirebaseFirestore.getInstance();
        }
        catch (Exception e) {
            FireBaseInstance instance = FireBaseInstance.instance(context);
            db = FirebaseFirestore.getInstance(FireBaseInstance.app);
        }

        collection = db.collection("Libraries");
    }

    public Task<Boolean> SetupLibrary(User user) {
        TaskCompletionSource<Boolean> taskSetupLibrary = new TaskCompletionSource<Boolean>();

        DocumentReference document = collection.document();
        Library library = new Library(user.getEmail(), new ArrayList<Book>());
        document.set(library)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        taskSetupLibrary.setResult(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        taskSetupLibrary.setResult(false);
                    }
                });

        return taskSetupLibrary.getTask();
    }

    public Task<ArrayList<Book>> getBooks(User user) {
        TaskCompletionSource<ArrayList<Book>> taskGetBooks = new TaskCompletionSource<>();

        collection.whereEqualTo("email", user.getEmail()).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                            DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);

                            if (document != null) {
                                ArrayList<Book> foundBooks = (ArrayList<Book>) document.get("books");
                                taskGetBooks.setResult(foundBooks);
                            }
                            else
                                taskGetBooks.setResult(null);
                        } else {
                            taskGetBooks.setResult(null);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        taskGetBooks.setResult(null);
                    }
                });

        return taskGetBooks.getTask();
    }
}
