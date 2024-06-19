package co.il.liam.booktobook.ACTIVITIES;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

import co.il.liam.booktobook.ADAPTERS.LibraryAdapter;
import co.il.liam.booktobook.CheckInternetConnection;
import co.il.liam.booktobook.EmptyDialog;
import co.il.liam.booktobook.LargerPhotoDialog;
import co.il.liam.booktobook.LoadingDialog;
import co.il.liam.booktobook.R;
import co.il.liam.booktobook.SearchDialog;
import co.il.liam.helper.BitMapHelper;
import co.il.liam.model.Book;
import co.il.liam.model.Books;
import co.il.liam.model.Chat;
import co.il.liam.model.User;
import co.il.liam.viewmodel.BooksViewModel;
import co.il.liam.viewmodel.ChatsViewModel;
import co.il.liam.viewmodel.UsersViewModel;

import static co.il.liam.helper.EnumHelper.fixEnumText;


public class LibraryActivity extends BaseActivity implements SearchDialog.DialogResultsListener {
    private TextView tvLibraryGoBack;
    private RecyclerView rvLibrary;
    private LibraryAdapter libraryAdapter;
    private LoadingDialog loadingBooksDialog;
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
    public static final String jobLibrary = "library";
    public static final String jobSearch = "search";
    private ArrayList<String> emails = new ArrayList<>();

    private String getAllCalledFrom = "initialization";
    private Books filteredBooks;
    private int givenBooks;
    private int unmatchingBooks;

    private Book editingBook;

    private User loggedUser;
    private BooksViewModel booksViewModel;
    private UsersViewModel usersViewModel;
    private ChatsViewModel chatsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        setScreenOrientation();

        initializeViews();
        setListeners();
        setObservers();
        setRecyclerView();
        setLibrary();
        findUsers();
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

    @Override
    protected void setScreenOrientation() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    private void setObservers()  {
        booksViewModel = new ViewModelProvider(this).get(BooksViewModel.class);
        booksViewModel.getFoundBooks().observe(this, new Observer<Books>() {
            @Override
            public void onChanged(Books books) {
                loadingBooksDialog.dismiss();

                if (books != null) {
                    if (!books.isEmpty()) {

                        if (Objects.equals(currentJob, jobSearch)) {
                            books = removeForDisplay(books);
                        }

                        books.reverseContents();   //since the recyclerview and book xml-s are flipped for books to touch the shelf
                        // you need to reverse the order of the books so that it looks organized on the shelf
                        libraryAdapter.setBooks(books);
                        if (Objects.equals(getAllCalledFrom, "initialization")) {
                            library = books;
                        }
                    }

                    else {
                        if (Objects.equals(currentJob, jobSearch)) {
                            showDialogNoBooksAndStay();
                        }
                        else {
                            showDialogNoBooksAndQuit();
                        }
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
                    books.reverseContents();
                    library = books;
                    libraryAdapter.setBooks(books);

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
                        showDialogNoBooksAndQuit();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Couldn't delete book", Toast.LENGTH_SHORT).show();
                }
            }
        });

        booksViewModel.getUpdatedBook().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    Toast.makeText(getApplicationContext(), "Updated book", Toast.LENGTH_SHORT).show();
                    if (CheckInternetConnection.check(LibraryActivity.this)) {
                        LoadingDialog loadingDialog = new LoadingDialog(LibraryActivity.this, "Loading books...", "This will only take a few seconds");
                        loadingDialog.setCancelable(false);
                        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

                        loadingBooksDialog = loadingDialog;
                        loadingBooksDialog.show();

                        booksViewModel.getAll(loggedUser);
                    }
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

        usersViewModel.getUserDataByEmail().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                booksViewModel.getAll(user);
            }
        });

        usersViewModel.getFoundLocationBook().observe(this, new Observer<Book>() {
            @Override
            public void onChanged(Book book) {
                loadingBooksDialog.dismiss();

                if (book != null) {

                    filteredBooks.add(book);
                    libraryAdapter.setBooks(filteredBooks);

                }
                else {
                    unmatchingBooks++;

                    if (unmatchingBooks == givenBooks) {

                        showDialogNoBooksAndStay();

                        loadingBooksDialog.show();

                        if (Objects.equals(currentJob, jobSearch)) {
                            booksViewModel.getAllOtherBooks(loggedUser);
                        }
                        else {
                            booksViewModel.getAll(loggedUser);
                        }
                    }
                }
            }
        });

        chatsViewModel = new ViewModelProvider(this).get(ChatsViewModel.class);

        chatsViewModel.getAddedChat().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LibraryActivity.this);
                    builder.setTitle("You can now message " + selectedBookOwner.getUsername());
                    builder.setMessage("The chat will appear in your chat list");
                    builder.setCancelable(true);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Chat exists", Toast.LENGTH_SHORT).show();
                }
            }
        });

        chatsViewModel.getFoundEmails().observe(this, new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> foundEmails) {
                if (foundEmails != null) {
                    emails = foundEmails;
                }
            }
        });
    }

    private Books removeForDisplay(Books books) {
        Books newBooks = new Books();

        for (Book book : books) {
            if (!Objects.equals(book.getExchange().toString(), "FOR_DISPLAY")){
                newBooks.add(book);
            }
        }

        return newBooks;
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

                if (Objects.equals(currentJob, jobLibrary)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LibraryActivity.this);
                    builder.setTitle(book.getTitle());
                    builder.setMessage("What action would you like to do?");
                    builder.setCancelable(true);
                    builder.setNegativeButton("Edit book", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            editingBook = book;
                            Intent editBookIntent = new Intent(LibraryActivity.this, AddActivity.class);
                            editBookIntent.putExtra("editBook", book);
                            editBookIntent.putExtra("context", "edit");
                            startActivityForResult(editBookIntent, 2);
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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

                return false;
            }
        };

        Books libraryTest = new Books();
        libraryTest.reverseContents();

        libraryAdapter = new LibraryAdapter(this, libraryTest, listener, longListener);
        rvLibrary.setAdapter(libraryAdapter);
        rvLibrary.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        LinearLayoutManager layoutManager = (LinearLayoutManager) rvLibrary.getLayoutManager();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

    }

    private void setLibrary()  {
        Intent userInfoIntent = getIntent();
        loggedUser = (User) userInfoIntent.getSerializableExtra("user");
        currentJob = userInfoIntent.getStringExtra("job");

        library = new Books();

        if (CheckInternetConnection.check(this)) {
            LoadingDialog loadingDialog = new LoadingDialog(this, "Loading books...", "This will only take a few seconds");
            loadingDialog.setCancelable(false);
            loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

            loadingBooksDialog = loadingDialog;
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

    private void findUsers() {
        chatsViewModel.findEmails(loggedUser);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            Book editedBook = (Book) data.getSerializableExtra("book");

            if (requestCode == 2) {
                if (editedBook != null) {
                    libraryAdapter.setBooks(new Books());
                    booksViewModel.updateBook(editedBook);
                }
                else {
                    library.add(editingBook);
                }
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

    private void filter() {
        SearchDialog searchDialog = new SearchDialog(this, this, emails, loggedUser.getCity(), loggedUser.getState(), currentJob);
        searchDialog.setCancelable(true);
        searchDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        searchDialog.show();
    }

    @Override
    public void onDialogResult(Bundle data) {
        String category = data.getString("category");
        String search = data.getString("search");
        String stateOrCity = data.getString("stateOrCity");


        loadingBooksDialog.show();

        removeBookData();
        selectedBookOwner = null;
        expendedBook = -1;
        filteredBooks = new Books();
        givenBooks = 0;
        unmatchingBooks = 0;

        getAllCalledFrom = "filter";


        if (Objects.equals(category, "My books")) {
            booksViewModel.getAll(loggedUser);
        }
        else if (Objects.equals(category, "Email")) {
            User userEmail = new User();
            userEmail.setEmail(search);
            usersViewModel.findUserDataByEmail(userEmail);
        }

        else {
            Books newLibrary = new Books();

            for (Book book : library) {
                String test = book.generalGet(category);
                if (book.generalGet(category).toLowerCase().contains(search.toLowerCase())) {

                    if (!stateOrCity.isEmpty()) {
                        givenBooks++;

                        User findUser = new User();
                        findUser.setIdFs(book.getUserId());
                        usersViewModel.findBookWithLocation(loggedUser, stateOrCity, book);
                    }

                    else {
                        newLibrary.add(book);
                    }

                }
            }

            loadingBooksDialog.dismiss();

            if (stateOrCity.isEmpty()) {
                libraryAdapter.setBooks(newLibrary);

                if (newLibrary.isEmpty()) {
                    showDialogNoBooksAndStay();

                    loadingBooksDialog.show();

                    if (currentJob.equals(jobLibrary)) {
                        booksViewModel.getAll(loggedUser);
                    }
                    else {
                        booksViewModel.getAllOtherBooks(loggedUser);
                    }
                }
            }

        }

        Toast.makeText(getApplicationContext(), "Searching for " + category + " " + search + " " + stateOrCity, Toast.LENGTH_SHORT).show();
    }




    //myBooks

    public void showDialogNoBooksAndQuit() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        };

        EmptyDialog emptyDialog = new EmptyDialog(this, "You don't have any books", true, listener);
        emptyDialog.setCancelable(false);
        emptyDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        emptyDialog.show();
    }



    //search

    public void showDialogNoBooksAndStay() {
        EmptyDialog emptyDialog = new EmptyDialog(this, "No books match your search results", false, null);
        emptyDialog.setCancelable(true);
        emptyDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        emptyDialog.show();
    }

    private void messageOwner() {
        if (selectedBookOwner != null) {
            Chat chat = new Chat();
            chat.setUserOne(loggedUser);
            chat.setUserTwo(selectedBookOwner);
            chatsViewModel.addChat(chat);
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Go back Confirmation")
                .setMessage("Are you sure you want to go back?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setResult(RESULT_OK);
                        finish();
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }


}