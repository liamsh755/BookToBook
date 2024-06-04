package co.il.liam.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import co.il.liam.model.Book;
import co.il.liam.model.Books;
import co.il.liam.model.User;
import co.il.liam.repository.BooksRepository;

public class BooksViewModel extends AndroidViewModel {
    private BooksRepository booksRepository;

    private MutableLiveData<Boolean> addedBook;
    private MutableLiveData<Books> foundBooks;
    private MutableLiveData<Books> foundOtherBooks;
    private MutableLiveData<Boolean> deletedBook;

    public BooksViewModel(@NonNull Application application) {
        super(application);

        booksRepository = new BooksRepository(application);
        addedBook = new MutableLiveData<>();
        foundBooks = new MutableLiveData<>();
        foundOtherBooks = new MutableLiveData<>();
        deletedBook = new MutableLiveData<>();
    }

    public MutableLiveData<Boolean> getAddedBook() {
        return addedBook;
    }

    public MutableLiveData<Books> getFoundBooks() {
        return foundBooks;
    }

    public MutableLiveData<Books> getFoundOtherBooks() {
        return foundOtherBooks;
    }

    public MutableLiveData<Boolean> getDeletedBook() {
        return deletedBook;
    }

    public void addBook(Book book){
        booksRepository.addBook(book)
                .addOnSuccessListener(aBoolean -> {
                    addedBook.setValue(aBoolean); })
                .addOnFailureListener(aBoolean -> {
                   addedBook.setValue(false); });
    }

    public void getAll(User user){
        booksRepository.getAll(user)
                .addOnSuccessListener(books -> {
                    foundBooks.setValue(books); })
                .addOnFailureListener(books -> {
                   foundBooks.setValue(null); });
    }

    public void getAllOtherBooks(User user){
        booksRepository.getAllOtherBooks(user)
                .addOnSuccessListener(books -> {
                    foundOtherBooks.setValue(books); })
                .addOnFailureListener(books -> {
                    foundOtherBooks.setValue(null); });
    }

    public void deleteBook(Book book) {
        booksRepository.deleteBook(book)
                .addOnSuccessListener(aBoolean -> {
                    deletedBook.setValue(aBoolean); })
                .addOnFailureListener(aBoolean -> {
                    deletedBook.setValue(false);  });
    }
}
