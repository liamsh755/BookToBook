package co.il.liam.booktobook.ACTIVITIES;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import co.il.liam.booktobook.ADAPTERS.BookRecsAdapter;
import co.il.liam.booktobook.BookService;
import co.il.liam.booktobook.BooksResponse;
import co.il.liam.booktobook.LoadingDialog;
import co.il.liam.booktobook.R;
import co.il.liam.model.BookRec;
import co.il.liam.model.BookRecs;

public class DiscoverActivity extends BaseActivity {
    private TextView tvDiscoverGoBack;
    private TextView tvTitle;
    private TextView tvDescription;
    private RecyclerView rvBookRecs;
    private BookRecs bookRecs;
    private BookRecsAdapter bookRecsAdapter;

    private LoadingDialog loadingBooksDialog;

    BookService bookService = new BookService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);
        setScreenOrientation();

        initializeViews();
        getRandomBook();
        setListeners();
        setBookRecs();
        setRecyclerview();
    }

    private void setBookRecs() {
        bookRecs = new BookRecs();

        BookRec one = new BookRec();
        one.setTitle("book1");
        List<String> authorsOne = new ArrayList<>();
        authorsOne.add("author1");
        one.setAuthors(authorsOne);
        one.setDescription("descrpition1 descrpition1 descrpition1 descrpition1 descrpition1 ");
        one.setPageCount(1);

        BookRec two = new BookRec();
        two.setTitle("book2");
        List<String> authorsTwo = new ArrayList<>();
        authorsTwo.add("author2");
        two.setAuthors(authorsTwo);
        two.setDescription("descrpition2 descrpition2 descrpition2 descrpition2 descrpition2 ");
        two.setPageCount(2);

        BookRec three = new BookRec();
        three.setTitle("book3");
        List<String> authorsThree = new ArrayList<>();
        authorsThree.add("author3");
        three.setAuthors(authorsThree);
        three.setDescription("descrpition3 descrpition3 descrpition3 descrpition3 descrpition3 ");
        three.setPageCount(2);

        bookRecs.add(one);
        bookRecs.add(two);
        bookRecs.add(three);
        bookRecs.add(three);
        bookRecs.add(three);
        bookRecs.add(three);
        bookRecs.add(three);
        bookRecs.add(three);
        bookRecs.add(three);
        bookRecs.add(three);
        bookRecs.add(three);
    }

    private void setRecyclerview() {
        bookRecsAdapter = new BookRecsAdapter(getApplicationContext(), R.layout.book_rec_single_layout, bookRecs);
        rvBookRecs.setAdapter(bookRecsAdapter);
        rvBookRecs.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        ItemTouchHelper.SimpleCallback swipeCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                BookRec swipedBook = bookRecs.get(position);
                if (direction == ItemTouchHelper.RIGHT) {
                    Toast.makeText(getApplicationContext(), "Yay!", Toast.LENGTH_SHORT).show();
                }
                else if (direction == ItemTouchHelper.LEFT) {
                    Toast.makeText(getApplicationContext(), "NAH!", Toast.LENGTH_SHORT).show();
                }
                bookRecs.remove(position);
                bookRecsAdapter.setBookRecs(bookRecs);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeCallback);
        itemTouchHelper.attachToRecyclerView(rvBookRecs);

        rvBookRecs.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // Get the top card
                View topView = recyclerView.getChildAt(0);
                if (topView != null) {
                    // Animate translation for next card peeking
                    View nextView = recyclerView.getChildAt(1);
                    if (nextView != null) {
                        nextView.animate().translationY(-topView.getHeight() * 0.1f).setDuration(100).start();
                    }
                }
            }
        });
    }

    private void getRandomBook() {
        loadingBooksDialog.show();
        bookService.getRandomBook("fantasy", new BookService.RandomBookCallback() {
            @Override
            public void onSuccess(BooksResponse.BookItem book) {
                String title  = book.volumeInfo.title;
                List<String> authors = book.volumeInfo.authors;
                String description = book.volumeInfo.description;
                int pageCount = book.volumeInfo.pageCount;
                String thumbnail = book.volumeInfo.imageLinks != null ? book.volumeInfo.imageLinks.thumbnail : null;
                String language = book.volumeInfo.language;

                tvTitle.setText(title);
                tvDescription.setText(description);

//                tvDiscoverTitle.setText(title);
//                tvDiscoverDescription.setText(description);
//                tvDiscoverPageCount.setText("Page count - " + pageCount);
//
//                StringBuilder authorsText = new StringBuilder();
//                for (String author : authors) {
//                    authorsText.append(author).append("\n");
//                }
//                tvDiscoverAuthors.setText(authorsText.toString());
//
//                if (thumbnail != null) {
//                    Picasso.get().load(thumbnail).into(ivDiscoverImage);
//                }


                loadingBooksDialog.dismiss();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(getApplicationContext(), "Couldn't find a random book", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void initializeViews() {
        tvDiscoverGoBack = findViewById(R.id.tvDiscoverGoBack);
        rvBookRecs = findViewById(R.id.rvDiscoverList);
        tvTitle = findViewById(R.id.tvTitle);
        tvDescription = findViewById(R.id.tvDescription);
        Picasso.get();

        LoadingDialog loadingDialog = new LoadingDialog(DiscoverActivity.this, "Finding books for you...", "This will only take a few seconds");
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        loadingBooksDialog = loadingDialog;
    }

    @Override
    protected void setListeners() {
        tvDiscoverGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    @Override
    protected void setScreenOrientation() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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