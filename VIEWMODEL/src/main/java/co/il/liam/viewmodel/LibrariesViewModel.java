package co.il.liam.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

import co.il.liam.model.Book;
import co.il.liam.model.User;
import co.il.liam.repository.LibrariesRepository;

public class LibrariesViewModel extends AndroidViewModel {
    private LibrariesRepository librariesRepository;

    private MutableLiveData<Boolean> setupLibrary;
    private MutableLiveData<ArrayList<Book>> getBooks;
    public LibrariesViewModel(@NonNull Application application) {
        super(application);

        librariesRepository = new LibrariesRepository(application);
        setupLibrary = new MutableLiveData<>();
        getBooks = new MutableLiveData<>();
    }

    public LiveData<Boolean> getSetupLibrary() {
        return setupLibrary;
    }

    public LiveData<ArrayList<Book>> getBooks() {
        return getBooks;
    }

    public void setupLibrary(User user) {
        librariesRepository.SetupLibrary(user)
                .addOnSuccessListener(aBoolean -> {
                    setupLibrary.setValue(aBoolean); })
                .addOnFailureListener(aBoolean -> {
                    setupLibrary.setValue(false); });
    }

    public void findBooks(User user) {
        librariesRepository.getBooks(user)
                .addOnSuccessListener(books -> {
                    getBooks.setValue(books); })
                .addOnFailureListener(books -> {
                    getBooks.setValue(null); });
    }
}
