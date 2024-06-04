package co.il.liam.booktobook.ACTIVITIES;

import static co.il.liam.model.Book.Height;
import static co.il.liam.model.Book.Width;
import static co.il.liam.model.Book.Decoration;
import static co.il.liam.model.Book.Font;
import static co.il.liam.model.Book.Exchange;
import static co.il.liam.model.Book.Condition;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.Context;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import co.il.liam.booktobook.CheckInternetConnection;
import co.il.liam.booktobook.LargerPhotoDialog;
import co.il.liam.booktobook.R;
import co.il.liam.helper.BitMapHelper;
import co.il.liam.model.Book;
import co.il.liam.model.User;
import co.il.liam.viewmodel.BooksViewModel;
import co.il.liam.viewmodel.UsersViewModel;
import de.hdodenhof.circleimageview.CircleImageView;

public class AddActivity extends BaseActivity {
    private User loggedUser;
    private BooksViewModel booksViewModel;

    private TextView tvAddGoBack;
    private CheckBox cbAddPreview;
    private FrameLayout flAddPreview;
    private View bookLayout;
    private ConstraintLayout clAddPreviewClickCheck;

    private TextView tvBookTitle;
    private TextView tvBookAuthor;
    private CircleImageView civBookImage;
    private CardView cvBookBackground;
    private ConstraintLayout vBookDec1;
    private View vBookDec2;
    private View vBookDec3;
    private View vBookDec4;

    private Spinner spnAddWidth;
    private Spinner spnAddHeight;
    private Spinner spnAddMainColor;
    private ImageView ivAddInfoMainColor;
    private Spinner spnAddSecColor;
    private ImageView ivAddInfoSecColor;
    private Spinner spnAddDecoration;
    private ImageView ivAddInfoDecoration;
    private Spinner spnAddFont;

    private EditText etAddTitle;
    private EditText etAddAuthor;
    private EditText etAddDescription;
    private ImageView ivAddInfoDescription;
    private EditText etAddGenre;
    private ImageView btnAddImage;
    private ConstraintLayout clAddImagePreview;
    private ImageView ivAddImagePreview;
    private ImageView ivAddInfoPicture;

    private Spinner spnAddCondition;
    private TextView tvAddConditionTitle;
    private Spinner spnAddExchange;
    private ImageView ivAddInfoExchange;
    private TextView tvAddExchangeTitle;
    
    private Button btnAddConfirm;
    private ProgressBar pbAddWait;

    private Boolean listen = false;

    private String format = "";
    private Uri imageUri;
    private Bitmap imageBitmap;

    private final int CAM = 1;
    private final int GALLERY = 2;
    private static final int REQUEST_CAMERA_PERMISSION = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        initializeViews();
        setListeners();
        setSpinners();
        setObservers();
    }

    private void setObservers() {
        booksViewModel = new ViewModelProvider(this).get(BooksViewModel.class);

        booksViewModel.getAddedBook().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                pbAddWait.setVisibility(View.INVISIBLE);
                if (aBoolean) {
                    Toast.makeText(getApplicationContext(), "Added book", Toast.LENGTH_SHORT).show();
                    setResult(Activity.RESULT_OK);
                    finish();
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Error adding book, Please contact support", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setSpinners() {
        //Width
        ArrayList<String> widths  = new ArrayList<>();
        widths.add(fixEnumText(Width.THIN.toString()));
        widths.add(fixEnumText(Width.THICK.toString()));
        ArrayAdapter<String> widthsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, widths);
        widthsAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spnAddWidth.setAdapter(widthsAdapter);

        //Height
        ArrayList<String> heights  = new ArrayList<>();
        heights.add(fixEnumText(Height.TALL.toString()));
        heights.add(fixEnumText(Height.MEDIUM.toString()));
        heights.add(fixEnumText(Height.SHORT.toString()));
        ArrayAdapter<String> heightsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, heights);
        heightsAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spnAddHeight.setAdapter(heightsAdapter);

        ArrayList<String> colors  = new ArrayList<>();
        colors.add("Dark red");
        colors.add("Red");
        colors.add("Orange");
        colors.add("Gold");
        colors.add("Yellow");
        colors.add("Green");
        colors.add("Dark green");
        colors.add("Turquoise");
        colors.add("Blue");
        colors.add("Indigo");
        colors.add("Purple");
        colors.add("Pink");
        colors.add("Brown");
        colors.add("Black");
        colors.add("Gray");
        colors.add("White");

        //Main Colors
        ArrayAdapter<String> mainColorsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, colors);
        mainColorsAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spnAddMainColor.setAdapter(mainColorsAdapter);

        //Sec colors
        ArrayAdapter<String> secColorsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, colors);
        secColorsAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spnAddSecColor.setAdapter(secColorsAdapter);
        spnAddSecColor.setSelection(1);

        //Decorations
        ArrayList<String> decorations  = new ArrayList<>();
        decorations.add(fixEnumText(Decoration.ONE_LINE.toString()));
        decorations.add(fixEnumText(Decoration.TWO_LINES.toString()));
        decorations.add(fixEnumText(Decoration.THREE_LINES.toString()));
        decorations.add(fixEnumText(Decoration.THICK_LINE.toString()));
        ArrayAdapter<String> decorationsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, decorations);
        decorationsAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spnAddDecoration.setAdapter(decorationsAdapter);

        //Font
        ArrayList<String> fonts  = new ArrayList<>();
        fonts.add(fixEnumText(Font.COMIC_SANS.toString()));
        fonts.add(fixEnumText(Font.CLASSIC.toString()));
        fonts.add(fixEnumText(Font.CURSIVE.toString()));
        fonts.add(fixEnumText(Font.GOTHIC.toString()));
        fonts.add(fixEnumText(Font.FUN.toString()));
        ArrayAdapter<String> fontsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, fonts);
        fontsAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spnAddFont.setAdapter(fontsAdapter);

        //conditions
        ArrayList<String> conditions  = new ArrayList<>();
        conditions.add(fixEnumText(Condition.PERFECT.toString()));
        conditions.add(fixEnumText(Condition.USED.toString()));
        conditions.add(fixEnumText(Condition.OLD.toString()));
        ArrayAdapter<String> conditionsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, conditions);
        conditionsAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spnAddCondition.setAdapter(conditionsAdapter);

        //status
        ArrayList<String> statuses  = new ArrayList<>();
        statuses.add(fixEnumText(Exchange.PERMANENT.toString()));
        statuses.add(fixEnumText(Exchange.TEMPORARY.toString()));
        statuses.add(fixEnumText(Exchange.FOR_DISPLAY.toString()));
        ArrayAdapter<String> statusesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, statuses);
        statusesAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spnAddExchange.setAdapter(statusesAdapter);

    }

    private int getLayoutForBook(String width, String height) {
        int[] layouts = {R.layout.book_thick_tall, R.layout.book_thick_med, R.layout.book_thick_short,
                R.layout.book_thin_tall,  R.layout.book_thin_med,  R.layout.book_thin_short  };

        int layout = 0;

        if (width.equals("Thin")) {
            layout += 3;
        }

        switch (height) {
            case "Tall":
                layout += 0;  //redundant, for readability
                break;

            case "Medium":
                layout += 1;
                break;

            case "Short":
                layout += 2;
                break;

        }

        return layouts[layout];
    }

    private void setBookPreview() {
        //saving the text and image if they exists on the book before layout and size changes revert it
        String title = "Title";
        String author = "Author";

        try {
            title = tvBookTitle.getText().toString();
            author = tvBookAuthor.getText().toString();
        }
        catch (Exception ignored) {}

        String width = spnAddWidth.getSelectedItem().toString();
        String height = spnAddHeight.getSelectedItem().toString();

        //match widget size to layout and set the layout
        setBookSize(width, height);
        setBookLayout(width, height);

        //set colors
        int mainColor = getColorFromString(spnAddMainColor.getSelectedItem().toString(), "preview");
        int secColor = getColorFromString(spnAddSecColor.getSelectedItem().toString(), "preview");
        setBookColors(mainColor, secColor);

        //set decorations and font
        setBookDecorations(spnAddDecoration.getSelectedItem().toString());
        setBookFont(spnAddFont.getSelectedItem().toString());

        //return previous data
        tvBookTitle.setText(title);
        tvBookAuthor.setText(author);

        if (Objects.equals(format, "bitmap")) {
            civBookImage.setImageBitmap(imageBitmap);
        }
        else if (Objects.equals(format, "uri")) {
            civBookImage.setImageURI(imageUri);
        }
    }

    @Override
    protected void initializeViews() {
        Intent  userIntent = getIntent();
        loggedUser = (User) userIntent.getSerializableExtra("user");

        tvAddGoBack = findViewById(R.id.tvAddGoBack);
        cbAddPreview = findViewById(R.id.cbAddPreview);
        flAddPreview = findViewById(R.id.flAddPreview);
        clAddPreviewClickCheck = findViewById(R.id.clAddPreviewClickCheck);

        spnAddWidth = findViewById(R.id.spnAddWidth);
        spnAddHeight = findViewById(R.id.spnAddHeight);
        spnAddMainColor = findViewById(R.id.spnAddMainColor);
        ivAddInfoMainColor = findViewById(R.id.ivAddInfoMainColor);
        spnAddSecColor = findViewById(R.id.spnAddSecColor);
        ivAddInfoSecColor = findViewById(R.id.ivAddInfoSecColor);
        spnAddDecoration = findViewById(R.id.spnAddDecoration);
        ivAddInfoDecoration = findViewById(R.id.ivAddInfoDecoration);
        spnAddFont = findViewById(R.id.spnAddFont);

        etAddTitle = findViewById(R.id.etAddTitle);
        etAddAuthor = findViewById(R.id.etAddAuthor);
        etAddDescription = findViewById(R.id.etAddDescription);
        ivAddInfoDescription = findViewById(R.id.ivAddInfoDescription);
        etAddGenre = findViewById(R.id.etAddGenre);
        btnAddImage = findViewById(R.id.btnAddImage);
        clAddImagePreview = findViewById(R.id.clAddImagePreview);
        ivAddImagePreview = findViewById(R.id.ivAddImagePreview);
        ivAddInfoPicture = findViewById(R.id.ivAddInfoPicture);

        spnAddCondition = findViewById(R.id.spnAddCondition);
        tvAddConditionTitle = findViewById(R.id.tvAddConditionTitle);
        spnAddExchange = findViewById(R.id.spnAddExchange);
        ivAddInfoExchange = findViewById(R.id.ivAddInfoExchange);
        tvAddExchangeTitle = findViewById(R.id.tvAddExchangeTitle);

        btnAddConfirm = findViewById(R.id.btnAddConfirm);
        pbAddWait = findViewById(R.id.pbAddWait);
    }

    @Override
    protected void setListeners() {

        setButtonsListeners();

        setInfosListeners();

        setSpinnersListeners();

        setEditTextsListeners();
    }

    private void startPreview() {
        //set book
        setBookPreview();

        //show preview
        cbAddPreview.setClickable(true);
        cbAddPreview.setChecked(true);
        flAddPreview.setVisibility(View.VISIBLE);

        //show widgets
        etAddTitle.setVisibility(View.VISIBLE);
        etAddAuthor.setVisibility(View.VISIBLE);
        etAddDescription.setVisibility(View.VISIBLE);
        ivAddInfoDescription.setVisibility(View.VISIBLE);
        etAddGenre.setVisibility(View.VISIBLE);
        btnAddImage.setVisibility(View.VISIBLE);
        clAddImagePreview.setVisibility(View.VISIBLE);
        ivAddInfoPicture.setVisibility(View.VISIBLE);

        spnAddCondition.setVisibility(View.VISIBLE);
        tvAddConditionTitle.setVisibility(View.VISIBLE);
        spnAddExchange.setVisibility(View.VISIBLE);
        ivAddInfoExchange.setVisibility(View.VISIBLE);
        tvAddExchangeTitle.setVisibility(View.VISIBLE);
    }


    private void setEditTextsListeners() {
        etAddTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvBookTitle.setText(s.toString());
                if (s.length() == 0) {
                    tvBookTitle.setText("Title");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        etAddAuthor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvBookAuthor.setText(s.toString());
                if (s.length() == 0) {
                    tvBookAuthor.setText("Author");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        etAddTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    cbAddPreview.setChecked(false);
                    cbAddPreview.setClickable(false);
                    flAddPreview.setVisibility(View.GONE);
                }
            }
        });
        etAddAuthor.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    cbAddPreview.setChecked(false);
                    cbAddPreview.setClickable(false);
                    flAddPreview.setVisibility(View.GONE);
                }
            }
        });
        etAddDescription.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    cbAddPreview.setChecked(false);
                    cbAddPreview.setClickable(false);
                    flAddPreview.setVisibility(View.GONE);
                }
            }
        });
        etAddGenre.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    cbAddPreview.setChecked(false);
                    cbAddPreview.setClickable(false);
                    flAddPreview.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setSpinnersListeners() {
        spnAddWidth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (listen) {
                    setBookPreview();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spnAddHeight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (listen) {
                    setBookPreview();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spnAddMainColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (listen) {
                    setBookPreview();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spnAddSecColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (listen) {
                    setBookPreview();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spnAddDecoration.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (listen) {
                    setBookPreview();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spnAddFont.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (listen) {
                    setBookPreview();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setInfosListeners() {
        ivAddInfoMainColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popInfoDialogMessage("Main color meaning", "Sets the color of the book itself, the background for the details.");
            }
        });
        ivAddInfoSecColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popInfoDialogMessage("Secondary color meaning", "Sets the color of the book decorations, the colored strips around the details.");
            }
        });
        ivAddInfoDecoration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popInfoDialogMessage("Decorations meaning", "Sets the specific decoration for this book.\nYou can disable decorations by choosing the same main and secondary color.");
            }
        });
        ivAddInfoDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popInfoDialogMessage("Description meaning", "Simply describe the book and the story or just enter the book's blurb on the back.");
            }
        });
        ivAddInfoExchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popInfoDialogMessage("Exchange meaning", "Describes whether this book is a timed switch or not.\nIf you don't plan on trading this book set it as display.");
            }
        });
        ivAddInfoPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popInfoDialogMessage("Selecting a picture", "The camera option may result in a pixelated image, if that occurs you should photograph the picture by yourself and use the gallery option.");
            }
        });
    }

    private void setButtonsListeners() {
        tvAddGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        cbAddPreview.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) { flAddPreview.setVisibility(View.VISIBLE); }
                else { flAddPreview.setVisibility(View.GONE); }
            }
        });

        btnAddConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnAddConfirm.getText().toString().equals("Show preview")) {
                    startPreview();
                    btnAddConfirm.setText("Add book");
                    listen = true;
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddActivity.this);
                    builder.setTitle("Are you sure?");
                    builder.setMessage("Please make sure all the book information you've enter is correct and won't confuse yourself and other users with mistakes and typos.");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Add book", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            addBook();
                        }
                    });
                    builder.setNegativeButton("Recheck", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddActivity.this);
                builder.setTitle("Choose option");
                builder.setMessage("Where do you get the photo from?");
                builder.setCancelable(false);
                builder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (checkCameraPermission()) {
                            Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intentCamera, CAM);
                        } else {
                            requestCameraPermission();
                        }
                    }
                });
                builder.setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent();
                        i.setType("image/*");
                        i.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(i, GALLERY);
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        clAddPreviewClickCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etAddTitle.hasFocus() | etAddAuthor.hasFocus() | etAddDescription.hasFocus() | etAddGenre.hasFocus()) {
                    //remove focus from the widgets
                    etAddTitle.clearFocus();
                    etAddAuthor.clearFocus();
                    etAddDescription.clearFocus();
                    etAddGenre.clearFocus();

                    //open book preview
                    cbAddPreview.setClickable(true);
                    cbAddPreview.setChecked(true);
                    flAddPreview.setVisibility(View.VISIBLE);

                    //close keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etAddTitle.getWindowToken(), 0);
                    imm.hideSoftInputFromWindow(etAddAuthor.getWindowToken(), 0);
                    imm.hideSoftInputFromWindow(etAddDescription.getWindowToken(), 0);
                    imm.hideSoftInputFromWindow(etAddGenre.getWindowToken(), 0);

                }
            }
        });

        ivAddImagePreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

                LargerPhotoDialog enlargeDialog = new LargerPhotoDialog(AddActivity.this, ((BitmapDrawable) ivAddImagePreview.getDrawable()).getBitmap(), displayMetrics);
                enlargeDialog.setCancelable(true);
                enlargeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
                enlargeDialog.show();
            }
        });
    }

    private void addBook() {
        if (valid()) {
            Book book = new Book();

            book.setUserId(loggedUser.getIdFs());
            book.setTitle(etAddTitle.getText().toString());
            book.setAuthor(etAddAuthor.getText().toString());
            book.setDescription(etAddDescription.getText().toString());
            book.setGenre(etAddGenre.getText().toString());

            book.setExchange(Exchange.valueOf(revertFixedEnumText(spnAddExchange.getSelectedItem().toString())));
            book.setCondition(Condition.valueOf(revertFixedEnumText(spnAddCondition.getSelectedItem().toString())));

            if (Objects.equals(format, "bitmap")) {
                book.setImage(BitMapHelper.encodeTobase64(imageBitmap));
            }
            else if (Objects.equals(format, "uri")) {
                book.setImage(BitMapHelper.encodeTobase64(uriToBitmap(getApplicationContext(), imageUri)));
            }

            book.setMainColor(getColorFromString(spnAddMainColor.getSelectedItem().toString(), "bookMainColor"));
            book.setSecColor(getColorFromString(spnAddSecColor.getSelectedItem().toString(), "bookSecColor"));

            book.setHeight(Height.valueOf(revertFixedEnumText(spnAddHeight.getSelectedItem().toString())));
            book.setWidth(Width.valueOf(revertFixedEnumText(spnAddWidth.getSelectedItem().toString())));
            book.setDecorations(Decoration.valueOf(revertFixedEnumText(spnAddDecoration.getSelectedItem().toString())));
            book.setFont(Font.valueOf(revertFixedEnumText(spnAddFont.getSelectedItem().toString())));

            if (CheckInternetConnection.check(this)) {
                pbAddWait.setVisibility(View.VISIBLE);
                booksViewModel.addBook(book);
            }
        }
    }

    private boolean valid() {
        String title = etAddTitle.getText().toString();
        String author = etAddAuthor.getText().toString();
        String description = etAddDescription.getText().toString();
        String genre = etAddGenre.getText().toString();

        Boolean valid = true;

        if (title.isEmpty()) {
            etAddTitle.setError("Enter the title");
            valid = false;
        }
        else if (author.isEmpty()) {
            etAddAuthor.setError("Enter the author");
            valid = false;
        }
        else if (description.isEmpty()) {
            etAddDescription.setError("Enter the description");
            valid = false;
        }
        else if (genre.isEmpty()) {
            etAddGenre.setError("Enter the genre");
            valid = false;
        }

        if (!valid) {
            Toast.makeText(getApplicationContext(), "Please enter all info", Toast.LENGTH_SHORT).show();
        }

        return valid;
    }


    public boolean checkCameraPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }
    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission granted, open camera intent
                Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intentCamera, CAM);
            } else {
                Toast.makeText(getApplicationContext(), "Please allow the app to open the camera", Toast.LENGTH_SHORT).show();
            }
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
    private static String revertFixedEnumText(String s) {
        String upper = s.toUpperCase();
        int space = upper.indexOf(' ');
        if (space != -1) {
            return upper.substring(0, space) + "_" + upper.substring(space + 1);
        }
        return upper;
    }
    private int getColorFromString(String s, String context) {
        // Convert the input string to lowercase for case-insensitive matching
        String lowercaseInput = s.toLowerCase();

        // Create a HashMap for efficient color lookup (key: lowercase color name, value: color resource ID)
        Map<String, Integer> colorMap = new HashMap<>();
        colorMap.put("dark red", ContextCompat.getColor(this, R.color.book_dark_red));
        colorMap.put("red", ContextCompat.getColor(this, R.color.book_red));
        colorMap.put("orange", ContextCompat.getColor(this, R.color.book_orange));
        colorMap.put("gold", ContextCompat.getColor(this, R.color.book_gold));
        colorMap.put("yellow", ContextCompat.getColor(this, R.color.book_yellow));
        colorMap.put("green", ContextCompat.getColor(this, R.color.book_green));
        colorMap.put("dark green", ContextCompat.getColor(this, R.color.book_dark_green));
        colorMap.put("turquoise", ContextCompat.getColor(this, R.color.book_turquoise));
        colorMap.put("blue", ContextCompat.getColor(this, R.color.book_blue));
        colorMap.put("indigo", ContextCompat.getColor(this, R.color.book_indigo));
        colorMap.put("purple", ContextCompat.getColor(this, R.color.book_purple));
        colorMap.put("pink", ContextCompat.getColor(this, R.color.book_pink));
        colorMap.put("brown", ContextCompat.getColor(this, R.color.book_brown));
        colorMap.put("black", ContextCompat.getColor(this, R.color.book_black));
        colorMap.put("gray", ContextCompat.getColor(this, R.color.book_gray));
        colorMap.put("white", ContextCompat.getColor(this, R.color.book_white));

        Integer color;

        color = colorMap.get(lowercaseInput);

        // Handle cases where the input doesn't match a color name
        if (color == null) {
            color = colorMap.get("gold"); //default color for books
        }

        return color != null ? color : 0;
    }
    public static Bitmap uriToBitmap(Context context, Uri uri) {
        Bitmap bitmap = null;
        try {
            ContentResolver contentResolver = context.getContentResolver();
            InputStream inputStream = contentResolver.openInputStream(uri);
            if (inputStream != null) {
                bitmap = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    private void popInfoDialogMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddActivity.this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(true);

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    //methods for book preview changes
    private void setBookLayout(String width, String height) {
        //set layout for book
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        bookLayout = inflater.inflate(getLayoutForBook(width, height), null);
        flAddPreview.addView(bookLayout);

        //set views
        tvBookTitle = bookLayout.findViewById(R.id.tvBookTitle);
        tvBookAuthor = bookLayout.findViewById(R.id.tvBookAuthor);
        civBookImage = bookLayout.findViewById(R.id.civBookImage);
        cvBookBackground = bookLayout.findViewById(R.id.cvBookBackground);
        vBookDec1 = bookLayout.findViewById(R.id.vBookDec1);
        vBookDec2 = bookLayout.findViewById(R.id.vBookDec2);
        vBookDec3 = bookLayout.findViewById(R.id.vBookDec3);
        vBookDec4 = bookLayout.findViewById(R.id.vBookDec4);
    }
    private void setBookSize(String width, String height) {
        int widthNum = 0;
        int heightNum = 0;

        //match variables to layout
        if (width.equals("Thick")) { widthNum = 78; }
        else { widthNum = 58; }

        if (height.equals("Tall")) { heightNum = 250; }
        else if (height.equals("Medium")) { heightNum = 240; }
        else { heightNum = 230; }

        //convert variables to dp
        widthNum = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                widthNum,
                getResources().getDisplayMetrics()
        );

        heightNum = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                heightNum,
                getResources().getDisplayMetrics()
        );

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) flAddPreview.getLayoutParams();
        params.height = heightNum;
        params.width = widthNum;
        flAddPreview.setLayoutParams(params);
    }
    private void setBookColors(int mainColor, int secColor) {
        cvBookBackground.setBackgroundTintList(ColorStateList.valueOf(mainColor));
        vBookDec1.setBackgroundColor(secColor);
        vBookDec2.setBackgroundColor(secColor);
        vBookDec3.setBackgroundColor(secColor);
        vBookDec4.setBackgroundColor(secColor);

        if (mainColor == ContextCompat.getColor(this, R.color.book_black)) {
            tvBookTitle.setTextColor(ContextCompat.getColor(this, R.color.book_white));
            tvBookAuthor.setTextColor(ContextCompat.getColor(this, R.color.book_white));
        }
    }
    private void setBookDecorations(@NonNull String s) {
        switch (s) {
            case "One line":
                vBookDec2.setVisibility(View.GONE);
                vBookDec4.setVisibility(View.GONE);
                vBookDec3.setVisibility(View.VISIBLE);
                break;
            case "Two lines":
                vBookDec2.setVisibility(View.GONE);
                vBookDec3.setVisibility(View.VISIBLE);
                vBookDec4.setVisibility(View.VISIBLE);
                break;
            case "Three lines":
                vBookDec2.setVisibility(View.VISIBLE);
                vBookDec3.setVisibility(View.VISIBLE);
                vBookDec4.setVisibility(View.VISIBLE);
                break;
            case "Thick line":
                vBookDec3.setVisibility(View.GONE);
                vBookDec2.setVisibility(View.VISIBLE);
                vBookDec4.setVisibility(View.VISIBLE);
                break;
        }
    }
    private void setBookFont(String s) {
        switch (s) {
            case "Classic":
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Typeface font_reg = ResourcesCompat.getFont(this, R.font.archivo);
                    tvBookAuthor.setTypeface(font_reg);
                    Typeface font_bold = ResourcesCompat.getFont(this, R.font.archivo_semibold);
                    tvBookTitle.setTypeface(font_bold);
                }
                break;

            case "Comic sans":
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Typeface font_reg = ResourcesCompat.getFont(this, R.font.comicsans);
                    tvBookAuthor.setTypeface(font_reg);
                    Typeface font_bold = ResourcesCompat.getFont(this, R.font.comicsansbold);
                    tvBookTitle.setTypeface(font_bold);
                }
                break;

            case "Fun":
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Typeface font_reg = ResourcesCompat.getFont(this, R.font.lobster);
                    tvBookAuthor.setTypeface(font_reg);
                    tvBookTitle.setTypeface(font_reg);
                }
                break;

            case "Cursive":
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Typeface font_reg = ResourcesCompat.getFont(this, R.font.playball);
                    tvBookAuthor.setTypeface(font_reg);
                    tvBookTitle.setTypeface(font_reg);
                }
                break;

            case "Gothic":
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Typeface font_reg = ResourcesCompat.getFont(this, R.font.almendra);
                    tvBookAuthor.setTypeface(font_reg);
                    Typeface font_bold = ResourcesCompat.getFont(this, R.font.almendra_bold);
                    tvBookTitle.setTypeface(font_bold);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAM) {
            if (resultCode == RESULT_OK) {
                imageBitmap = (Bitmap) data.getExtras().get("data");
                format = "bitmap";
                civBookImage.setImageBitmap(imageBitmap);
                ivAddImagePreview.setImageBitmap(imageBitmap);

            }
        }

        if (requestCode == GALLERY){
            if (resultCode == RESULT_OK) {
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri){
                    imageUri = selectedImageUri;
                    format = "uri";
                    civBookImage.setImageURI(imageUri);
                    ivAddImagePreview.setImageURI(imageUri);
                }
            }
        }

    }
}