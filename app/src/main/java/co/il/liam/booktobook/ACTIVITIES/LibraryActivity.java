package co.il.liam.booktobook.ACTIVITIES;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Random;

import co.il.liam.booktobook.ADAPTERS.LibraryAdapter;
import co.il.liam.booktobook.CheckInternetConnection;
import co.il.liam.booktobook.R;
import co.il.liam.helper.BitMapHelper;
import co.il.liam.model.Book;
import co.il.liam.model.Books;
import co.il.liam.model.User;
import co.il.liam.viewmodel.BooksViewModel;

public class LibraryActivity extends BaseActivity {
    private TextView tvLibraryGoBack;
    private RecyclerView rvLibrary;
    private LibraryAdapter libraryAdapter;
    private AlertDialog loadingBooksDialog;
    private Books library;

    private User loggedUser;

    private BooksViewModel booksViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        initializeViews();
        setListeners();
        setObservers();
        setRecyclerView();
        setLibrary();
    }

    private void setObservers() {
        booksViewModel = new ViewModelProvider(this).get(BooksViewModel.class);
        booksViewModel.getFoundBooks().observe(this, new Observer<Books>() {
            @Override
            public void onChanged(Books books) {
                loadingBooksDialog.dismiss();

                if (books != null) {
                    if (!books.isEmpty()) {
                        books.reverseContents();   //since the recyclerview and book xml-s are flipped for books to touch the shelf
                        // you need to reverse the order of the books so that it looks organized on the shelf
                        library = books;
                        libraryAdapter.setBooks(books);
                    }

                    else {
                        showDialogNoBooks();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Books not found!!!", Toast.LENGTH_SHORT).show();
                    Books defaultBooks = new Books();
                    defaultBooks = addBooks(defaultBooks);
                    defaultBooks.reverseContents();

                    library = defaultBooks;
                    libraryAdapter.setBooks(defaultBooks);
                }
            }
        });

        booksViewModel.getDeletedBook().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    Toast.makeText(getApplicationContext(), "Deleted book", Toast.LENGTH_SHORT).show();
                    libraryAdapter.setBooks(library);
                    if (library.isEmpty()){
                        showDialogNoBooks();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Couldn't delete book", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void showDialogNoBooks() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LibraryActivity.this);
        builder.setTitle("Wow so empty...");
        builder.setMessage("You have no books.");
        builder.setCancelable(false);
        builder.setPositiveButton("Go back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setResult(RESULT_OK);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public int getRandomColor() {
        Random random = new Random();

        int[] colors = {
                ContextCompat.getColor(this, R.color.book_dark_red),
                ContextCompat.getColor(this, R.color.book_red),
                ContextCompat.getColor(this, R.color.book_orange),
                ContextCompat.getColor(this, R.color.book_gold),
                ContextCompat.getColor(this, R.color.book_yellow),
                ContextCompat.getColor(this, R.color.book_green),
                ContextCompat.getColor(this, R.color.book_dark_green),
                ContextCompat.getColor(this, R.color.book_turquoise),
                ContextCompat.getColor(this, R.color.book_blue),
                ContextCompat.getColor(this, R.color.book_indigo),
                ContextCompat.getColor(this, R.color.book_purple),
                ContextCompat.getColor(this, R.color.book_pink),
                ContextCompat.getColor(this, R.color.book_brown),
                ContextCompat.getColor(this, R.color.book_black),
                ContextCompat.getColor(this, R.color.book_gray),
                ContextCompat.getColor(this, R.color.book_white)
        };

        return colors[random.nextInt(colors.length)];
    }

    public Book.Font getRandomFont() {
        Random random = new Random();

        Book.Font[] fonts = {
                Book.Font.COMIC_SANS,
                Book.Font.CLASSIC,
                Book.Font.CURSIVE,
                Book.Font.GOTHIC,
                Book.Font.FUN
        };

        return fonts[random.nextInt(fonts.length)];
    }

    public Book.Decoration getRandomDec() {
        Random random = new Random();

        Book.Decoration[] decs = {
                Book.Decoration.ONE_LINE,
                Book.Decoration.TWO_LINES,
                Book.Decoration.THREE_LINES,
                Book.Decoration.THICK_LINE
        };

        return decs[random.nextInt(decs.length)];
    }


    public Books addBooks(Books books) {
        String userId = "CLurT7kqcbpqqcGR5dq4";
        books.add(new Book(userId, "The secret garden", "Graham Rust",                      BitMapHelper.encodeTobase64(BitmapFactory.decodeResource(this.getResources(), R.drawable.books_background)), Color.parseColor("#FDA802"), Color.parseColor("#4A9FAE"), Book.Height.TALL, Book.Width.THICK, getRandomDec(), getRandomFont()));
        books.add(new Book(userId, "I'm glad my mom died", "Jennette McCurdy",              BitMapHelper.encodeTobase64(BitmapFactory.decodeResource(this.getResources(), R.drawable.books_background)), Color.parseColor("#BC4C59"), Color.parseColor("#8D26EA"), Book.Height.MEDIUM, Book.Width.THICK, getRandomDec(), getRandomFont()));
        books.add(new Book(userId, "Little fires everywhere", "Celeste Ng",                 BitMapHelper.encodeTobase64(BitmapFactory.decodeResource(this.getResources(), R.drawable.books_background)), getRandomColor(), getRandomColor(), Book.Height.SHORT, Book.Width.THICK, getRandomDec(), getRandomFont()));
        books.add(new Book(userId, "Dictionary idk", "Some long name yes",                  BitMapHelper.encodeTobase64(BitmapFactory.decodeResource(this.getResources(), R.drawable.books_background)), getRandomColor(), getRandomColor(), Book.Height.TALL, Book.Width.THIN, getRandomDec(), getRandomFont()));
        books.add(new Book(userId, "Some very long title yes i know long", "Lala lalaa",    BitMapHelper.encodeTobase64(BitmapFactory.decodeResource(this.getResources(), R.drawable.books_background)), getRandomColor(), getRandomColor(), Book.Height.MEDIUM, Book.Width.THIN, getRandomDec(), getRandomFont()));
        books.add(new Book(userId, "The life of a legend", "Liam Shvarts",                  BitMapHelper.encodeTobase64(BitmapFactory.decodeResource(this.getResources(), R.drawable.books_background)), getRandomColor(), getRandomColor(), Book.Height.SHORT, Book.Width.THIN, getRandomDec(), getRandomFont()));
        books.add(new Book(userId, "Lessons in chemistry", "Bonnie Garmus",                 BitMapHelper.encodeTobase64(BitmapFactory.decodeResource(this.getResources(), R.drawable.books_background)), getRandomColor(), getRandomColor(), Book.Height.TALL, Book.Width.THICK, getRandomDec(), getRandomFont()));
        books.add(new Book(userId, "Tommorow and Tommorow and Tommorow", "Gabrielle Zevin", BitMapHelper.encodeTobase64(BitmapFactory.decodeResource(this.getResources(), R.drawable.books_background)), getRandomColor(), getRandomColor(), Book.Height.TALL, Book.Width.THICK, getRandomDec(), getRandomFont()));

        return books;
    }

    private void setLibrary()  {
        Intent userInfoIntent = getIntent();
        loggedUser = (User) userInfoIntent.getSerializableExtra("user");

        library = new Books();

        if (CheckInternetConnection.check(this)) {

            AlertDialog.Builder builder = new AlertDialog.Builder(LibraryActivity.this);
            builder.setTitle("Loading books...");
            builder.setMessage("This will only take a few seconds");
            builder.setCancelable(false);

            loadingBooksDialog = builder.create();
            loadingBooksDialog.show();

            booksViewModel.getAll(loggedUser);
        }
    }


    private void setRecyclerView() {
        LibraryAdapter.OnItemClickListener listener = new LibraryAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(Book book, int position) {
                libraryAdapter.enlargeItem(position);

                Toast.makeText(getApplicationContext(), "Click", Toast.LENGTH_SHORT).show();
            }
        };

        LibraryAdapter.OnItemLongClickListener longListener = new LibraryAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClicked(Book book) {

                AlertDialog.Builder builder = new AlertDialog.Builder(LibraryActivity.this);
                builder.setTitle(book.getTitle());
                builder.setMessage("What action would you like to do?");
                builder.setCancelable(true);
                builder.setNegativeButton("Edit book", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "edit", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setPositiveButton("Delete book", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startDeletingBook(book);
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

                return true;
            }
        };

        Books libraryTest = new Books();
        libraryTest.reverseContents();

        libraryAdapter = new LibraryAdapter(this, R.layout.book_thick_tall, libraryTest, listener, longListener);
        rvLibrary.setAdapter(libraryAdapter);
        rvLibrary.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    private void startDeletingBook(Book book) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LibraryActivity.this);
        builder.setTitle(book.getTitle());
        builder.setMessage("Are you sure you want to delete this book?");
        builder.setCancelable(true);
        builder.setNegativeButton("Keep book ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setPositiveButton("Delete book", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (CheckInternetConnection.check(LibraryActivity.this)) {
                    library.remove(book);
                    booksViewModel.deleteBook(book);
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void initializeViews() {
        rvLibrary = findViewById(R.id.rvLibrary);
        tvLibraryGoBack = findViewById(R.id.tvLibraryGoBack);
    }

    @Override
    protected void setListeners() {
        tvLibraryGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }
}