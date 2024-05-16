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

import java.util.ArrayList;
import java.util.Random;

import co.il.liam.booktobook.ADAPTERS.LibraryAdapter;
import co.il.liam.booktobook.R;
import co.il.liam.model.Book;
import co.il.liam.model.Library;
import co.il.liam.model.User;
import co.il.liam.viewmodel.LibrariesViewModel;
import co.il.liam.viewmodel.UsersViewModel;

public class LibraryActivity extends BaseActivity {
    private TextView tvLibraryGoBack;
    private RecyclerView rvLibrary;
    private LibraryAdapter libraryAdapter;
    private Library library;

    private User loggedUser;

    private LibrariesViewModel librariesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        initializeViews();
        setListeners();
        setObservers();
        setLibrary();
        setRecyclerView();
    }

    private void setObservers() {
        librariesViewModel = new ViewModelProvider(this).get(LibrariesViewModel.class);

        librariesViewModel.getBooks().observe(this, new Observer<ArrayList<Book>>() {
            @Override
            public void onChanged(ArrayList<Book> books) {
                if (books != null) {
                    library.setBooks(books);
                    addBooks();  //adds the first initial books
                }
                else {
                    Toast.makeText(getApplicationContext(), "Books not found", Toast.LENGTH_SHORT).show();
                    library = new Library(loggedUser.getEmail(), new ArrayList<>());
                }
            }
        });
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


    public void addBooks() {
        library.add(new Book("The secret garden", "Graham Rust", BitmapFactory.decodeResource(this.getResources(), R.drawable.books_background), Color.parseColor("#FDA802"), Color.parseColor("#4A9FAE"), Book.Height.TALL, Book.Width.THICK, getRandomDec(), getRandomFont()));
        library.add(new Book("I'm glad my mom died", "Jennette McCurdy", BitmapFactory.decodeResource(this.getResources(), R.drawable.books_background), Color.parseColor("#BC4C59"), Color.parseColor("#8D26EA"), Book.Height.MEDIUM, Book.Width.THICK, getRandomDec(), getRandomFont()));
        library.add(new Book("Little fires everywhere", "Celeste Ng", BitmapFactory.decodeResource(this.getResources(), R.drawable.books_background), getRandomColor(), getRandomColor(), Book.Height.SHORT, Book.Width.THICK, getRandomDec(), getRandomFont()));
        library.add(new Book("Dictionary idk", "Some long name yes", BitmapFactory.decodeResource(this.getResources(), R.drawable.books_background), getRandomColor(), getRandomColor(), Book.Height.TALL, Book.Width.THIN, getRandomDec(), getRandomFont()));
        library.add(new Book("Some very long title yes i know long", "Lala lalaa", BitmapFactory.decodeResource(this.getResources(), R.drawable.books_background), getRandomColor(), getRandomColor(), Book.Height.MEDIUM, Book.Width.THIN, getRandomDec(), getRandomFont()));
        library.add(new Book("The life of a legend", "Liam Shvarts", BitmapFactory.decodeResource(this.getResources(), R.drawable.books_background), getRandomColor(), getRandomColor(), Book.Height.SHORT, Book.Width.THIN, getRandomDec(), getRandomFont()));
        library.add(new Book("Lessons in chemistry", "Bonnie Garmus", BitmapFactory.decodeResource(this.getResources(), R.drawable.books_background), getRandomColor(), getRandomColor(), Book.Height.TALL, Book.Width.THICK, getRandomDec(), getRandomFont()));
        library.add(new Book("Tommorow and Tommorow and Tommorow", "Gabrielle Zevin", BitmapFactory.decodeResource(this.getResources(), R.drawable.books_background), getRandomColor(), getRandomColor(), Book.Height.TALL, Book.Width.THICK, getRandomDec(), getRandomFont()));
    }

    private void setLibrary()  {
        Intent userInfoIntent = getIntent();
        loggedUser = (User) userInfoIntent.getSerializableExtra("user");

        librariesViewModel.findBooks(loggedUser);

        assert loggedUser != null;
//        library = new Library(loggedUser.getEmail(), new ArrayList<>());
//        addBooks();

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
                builder.setPositiveButton("Edit book", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "edit", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("Delete book", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "remove", Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

                return true;
            }
        };

        library.reverseContents(); //since the recyclerview and book xml-s are flipped for books to touch the shelf
                                   // you need to reverse the order of the books so that it looks organized on the shelf
        libraryAdapter = new LibraryAdapter(this, R.layout.book_thick_tall, library.getBooks(), listener, longListener);

        rvLibrary.setAdapter(libraryAdapter);
        rvLibrary.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
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
                finish();
            }
        });
    }
}