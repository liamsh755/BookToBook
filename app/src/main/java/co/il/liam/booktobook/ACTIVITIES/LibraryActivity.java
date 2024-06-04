package co.il.liam.booktobook.ACTIVITIES;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

import co.il.liam.booktobook.ADAPTERS.LibraryAdapter;
import co.il.liam.booktobook.CheckInternetConnection;
import co.il.liam.booktobook.LargerPhotoDialog;
import co.il.liam.booktobook.LoadingDialog;
import co.il.liam.booktobook.NoInternetDialog;
import co.il.liam.booktobook.R;
import co.il.liam.helper.BitMapHelper;
import co.il.liam.model.Book;
import co.il.liam.model.Books;
import co.il.liam.model.User;
import co.il.liam.viewmodel.BooksViewModel;
import co.il.liam.viewmodel.UsersViewModel;

public class LibraryActivity extends BaseActivity {
    private TextView tvLibraryGoBack;
    private RecyclerView rvLibrary;
    private LibraryAdapter libraryAdapter;
    private AlertDialog loadingBooksDialog;
    private Books library;

    private TextView tvLibraryTitle;
    private TextView tvLibraryAuthor;
    private TextView tvLibraryDescription;
    private TextView tvLibraryGenre;
    private TextView tvLibraryExchange;
    private TextView tvLibraryCondition;
    private ImageView ivLibraryImageView;
    private ProgressBar pbLibraryWait;
    private TextView tvLibraryOwner;
    private TextView tvLibraryCountry;
    private TextView tvLibraryCity;
    private LinearLayout llLibraryOwnerData;

    private Button btnLibraryFilterBack;
    private ImageView ivLibraryFilterImg;
    private TextView tvLibraryFilterText;

    private TextView tvLibraryNoSelection;
    private ScrollView svLibraryBookData;

    private ConstraintLayout clSearchButtons;
    private Button btnSearchMessageBack;
    private ImageView ivSearchMessageImg;
    private TextView tvSearchMessageText;
    private ImageView ivSearchInfo;
    private View divRegOwnerSection;


    private int expendedBook = -1;
    private User selectedBookOwner;

    private String currentJob = "";
    private final String jobLibrary = "library";
    private final String jobSearch = "search";

    private User loggedUser;
    private BooksViewModel booksViewModel;
    private UsersViewModel usersViewModel;

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

    //initializing

    @Override
    protected void initializeViews() {
        rvLibrary = findViewById(R.id.rvLibrary);
        tvLibraryGoBack = findViewById(R.id.tvLibraryGoBack);

        tvLibraryTitle = findViewById(R.id.tvLibraryTitle);
        tvLibraryAuthor = findViewById(R.id.tvLibraryAuthor);
        tvLibraryDescription = findViewById(R.id.tvLibraryDescription);
        tvLibraryGenre = findViewById(R.id.tvLibraryGenre);
        tvLibraryExchange = findViewById(R.id.tvLibraryExchange);
        tvLibraryCondition = findViewById(R.id.tvLibraryCondition);
        ivLibraryImageView = findViewById(R.id.ivLibraryImageView);
        pbLibraryWait = findViewById(R.id.pbLibraryWait);
        tvLibraryOwner = findViewById(R.id.tvLibraryOwner);
        tvLibraryCountry = findViewById(R.id.tvLibraryCountry);
        tvLibraryCity = findViewById(R.id.tvLibraryCity);
        llLibraryOwnerData = findViewById(R.id.llLibraryOwnerData);

        btnLibraryFilterBack = findViewById(R.id.btnLibraryFilterBack);
        ivLibraryFilterImg = findViewById(R.id.ivLibraryFilterImg);
        tvLibraryFilterText = findViewById(R.id.tvLibraryFilterText);

        tvLibraryNoSelection = findViewById(R.id.tvLibraryNoSelection);
        svLibraryBookData = findViewById(R.id.svLibraryBookData);
        clSearchButtons = findViewById(R.id.clSearchButtons);
        btnSearchMessageBack = findViewById(R.id.btnSearchMessageBack);
        ivSearchMessageImg = findViewById(R.id.ivSearchMessageImg);
        tvSearchMessageText = findViewById(R.id.tvSearchMessageText);
        ivSearchInfo = findViewById(R.id.ivSearchInfo);
        divRegOwnerSection = findViewById(R.id.divRegOwnerSection);
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

        btnLibraryFilterBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter();
            }
        });

        ivLibraryFilterImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter();
            }
        });

        tvLibraryFilterText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter();
            }
        });

        btnSearchMessageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageOwner();
            }
        });

        ivSearchMessageImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageOwner();
            }
        });

        tvSearchMessageText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageOwner();
            }
        });

        ivSearchInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LibraryActivity.this);
                builder.setTitle("Now what?");
                builder.setMessage("Talk to the owner of the book you want, and you take the wheel from here.\nCommunicate with users until you find the perfect book swap for you!");
                builder.setCancelable(true);

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        ivLibraryImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

                LargerPhotoDialog enlargeDialog = new LargerPhotoDialog(LibraryActivity.this, ((BitmapDrawable) ivLibraryImageView.getDrawable()).getBitmap(), displayMetrics);
                enlargeDialog.setCancelable(true);
                enlargeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
                enlargeDialog.show();
            }
        });
    }

    private void setObservers()  {
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
                        showDialogNoBooksAndQuit();
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

        booksViewModel.getFoundOtherBooks().observe(this, new Observer<Books>() {
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
                        showDialogNoBooksAndStay();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "There are no other books", Toast.LENGTH_SHORT).show();
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
                        showDialogNoBooksAndQuit();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Couldn't delete book", Toast.LENGTH_SHORT).show();
                }
            }
        });

        usersViewModel = new ViewModelProvider(this).get(UsersViewModel.class);

        usersViewModel.getUserDataByIdFs().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                llLibraryOwnerData.setVisibility(View.VISIBLE);
                pbLibraryWait.setVisibility(View.GONE);

                if (user != null) {
                    selectedBookOwner = user;

                    tvLibraryOwner.setText(user.getUsername());
                    tvLibraryCountry.setText(user.getState());
                    tvLibraryCity.setText(user.getCity());
                }
                else {
                    tvLibraryOwner.setText("Error occured");
                }
            }
        });
    }

    private void setRecyclerView() {
        LibraryAdapter.OnItemClickListener listener = new LibraryAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(Book book, int position) {
                libraryAdapter.enlargeItem(position);
                if (expendedBook != position) {
                    showBookData(book);
                    expendedBook = position;
                }
                else {
                    removeBookData();
                    selectedBookOwner = null;
                    expendedBook = -1;
                }

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

        libraryAdapter = new LibraryAdapter(this, libraryTest, listener, longListener);
        rvLibrary.setAdapter(libraryAdapter);
        rvLibrary.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    private void setLibrary()  {
        Intent userInfoIntent = getIntent();
        loggedUser = (User) userInfoIntent.getSerializableExtra("user");
        currentJob = userInfoIntent.getStringExtra("job");

        library = new Books();

        if (CheckInternetConnection.check(this)) {

            AlertDialog.Builder builder = new AlertDialog.Builder(LibraryActivity.this);
            builder.setTitle("Loading books...");
            builder.setMessage("This will only take a few seconds");
            builder.setCancelable(false);

            loadingBooksDialog = builder.create();
            loadingBooksDialog.show();

            if (Objects.equals(currentJob, jobLibrary)) {
                //show my books
                booksViewModel.getAll(loggedUser);
            }
            else {
                //show other books that aren't for display for their owners
                booksViewModel.getAllOtherBooks(loggedUser);
            }


        }
    }



    //general methods

    public Books addBooks(Books books) {
        String userId = "CLurT7kqcbpqqcGR5dq4";  //my user id
        books.add(new Book(userId, "Books failed loading", "Error occurred", BitMapHelper.encodeTobase64(BitmapFactory.decodeResource(this.getResources(), R.drawable.books_background)), R.color.book_dark_red, R.color.book_red, Book.Height.TALL, Book.Width.THICK, Book.Decoration.ONE_LINE, Book.Font.CLASSIC));

        return books;
    }

    private void startDeletingBook(Book book) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LibraryActivity.this);
        builder.setTitle(book.getTitle());
        builder.setMessage("Are you sure you want to delete this book?");
        builder.setCancelable(true);
        builder.setNegativeButton("Keep book ", null);
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

    private void showBookData(Book book) {
        tvLibraryTitle.setText(book.getTitle());
        tvLibraryAuthor.setText(book.getAuthor());
        tvLibraryDescription.setText(book.getDescription());
        tvLibraryGenre.setText(book.getGenre());
        tvLibraryExchange.setText(fixEnumText(book.getExchange().toString()));
        tvLibraryCondition.setText(fixEnumText(book.getCondition().toString()));
        ivLibraryImageView.setImageBitmap(BitMapHelper.decodeBase64(book.getImage()));

        svLibraryBookData.setVisibility(View.VISIBLE);
        tvLibraryNoSelection.setVisibility(View.GONE);

        if (Objects.equals(currentJob, jobSearch)) {
            clSearchButtons.setVisibility(View.VISIBLE);
            divRegOwnerSection.setVisibility(View.VISIBLE);
            llLibraryOwnerData.setVisibility(View.GONE);
            pbLibraryWait.setVisibility(View.VISIBLE);

            User findUser = new User();
            findUser.setIdFs(book.getUserId());
            usersViewModel.findUserDataByIdFs(findUser);

        }
        else {
            clSearchButtons.setVisibility(View.GONE);
            divRegOwnerSection.setVisibility(View.GONE);
            llLibraryOwnerData.setVisibility(View.GONE);
            pbLibraryWait.setVisibility(View.GONE);
        }

    }

    private void removeBookData() {
        svLibraryBookData.setVisibility(View.GONE);
        tvLibraryNoSelection.setVisibility(View.VISIBLE);

        if (Objects.equals(currentJob, jobSearch)) {
            clSearchButtons.setVisibility(View.GONE);
        }
    }

    private static String fixEnumText(String s) {
        String capitalize =  s.charAt(0) + s.substring(1).toLowerCase();
        int underscore = capitalize.indexOf('_');
        if (underscore != -1) {
            return capitalize.substring(0, underscore) + " " + capitalize.substring(underscore + 1);
        }
        return capitalize;
    }

    private void filter() {
        Toast.makeText(getApplicationContext(), "Filter", Toast.LENGTH_SHORT).show();

        LoadingDialog loadingDialog = new LoadingDialog(this, "Loading books...", "This will only take a few seconds");
        loadingDialog.setCancelable(true);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        loadingDialog.show();
    }




    //myBooks

    public void showDialogNoBooksAndQuit() {
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




    //search

    public void showDialogNoBooksAndStay() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LibraryActivity.this);
        builder.setTitle("Wow so empty...");
        builder.setMessage("No books have been found.");
        builder.setCancelable(true);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void messageOwner() {
        if (selectedBookOwner != null) {
            Toast.makeText(getApplicationContext(), "Message " + selectedBookOwner.getUsername(), Toast.LENGTH_SHORT).show();
        }
    }

}