package co.il.liam.booktobook;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Objects;

import co.il.liam.booktobook.ACTIVITIES.LibraryActivity;

public class SearchDialog extends Dialog {
    private Spinner spnSearchCategory;
    private Spinner spnSearchOptions;
    private EditText etSearchResult;
    private Button btnSearchFind;
    private CheckBox cbSearchCountry;
    private CheckBox cbSearchCity;

    private ArrayList<String> emails;
    private String country;
    private String city;

    private String currentJob;
    private DialogResultsListener listener;

    public SearchDialog(@NonNull Context context, DialogResultsListener listener, ArrayList<String> emails, String city, String country, String currentJob) {
        super(context);

        this.emails = emails;
        this.city = city;
        this.country = country;

        this.listener = listener;
        this.currentJob = currentJob;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_dialog);

        initializeView();
        setCategories();
        setListeners();
    }

    private void setListeners() {
        spnSearchCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spnSearchCategory.getSelectedItem().toString().equals("My books")) {
                    btnSearchFind.setVisibility(View.VISIBLE);
                    etSearchResult.setVisibility(View.GONE);
                    spnSearchOptions.setVisibility(View.GONE);
                    cbSearchCountry.setVisibility(View.GONE);
                    cbSearchCity.setVisibility(View.GONE);

                    cbSearchCountry.setChecked(false);
                    cbSearchCity.setChecked(false);
                }
                else if (!spnSearchCategory.getSelectedItem().toString().equals("Choose a category")) {
                    btnSearchFind.setVisibility(View.VISIBLE);
                    if (Objects.equals(currentJob, LibraryActivity.jobSearch)) {
                        cbSearchCountry.setVisibility(View.VISIBLE);

                    }
                    switch (spnSearchCategory.getSelectedItem().toString()) {
                        case "Email":
                            spnSearchOptions.setVisibility(View.VISIBLE);
                            etSearchResult.setVisibility(View.GONE);
                            cbSearchCountry.setVisibility(View.GONE);
                            cbSearchCity.setVisibility(View.GONE);
                            cbSearchCity.setChecked(false);
                            cbSearchCountry.setChecked(false);

                            ArrayAdapter<String> usernamesAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, emails);
                            usernamesAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                            spnSearchOptions.setAdapter(usernamesAdapter);

                            break;
                        case "Title":
                        case "Author":
                            spnSearchOptions.setVisibility(View.GONE);
                            etSearchResult.setVisibility(View.VISIBLE);
                            if (Objects.equals(currentJob, LibraryActivity.jobSearch)) {
                                cbSearchCountry.setVisibility(View.VISIBLE);

                            }

                            break;
                        case "Genre":
                            spnSearchOptions.setVisibility(View.VISIBLE);
                            etSearchResult.setVisibility(View.GONE);
                            if (Objects.equals(currentJob, LibraryActivity.jobSearch)) {
                                cbSearchCountry.setVisibility(View.VISIBLE);

                            }

                            ArrayList<String> genres = new ArrayList<>();
                            genres.add("Other");
                            genres.add("Action & Adventure");
                            genres.add("Adventure");
                            genres.add("Anthology");
                            genres.add("Art & Photography");
                            genres.add("Biography");
                            genres.add("Business & Economics");
                            genres.add("Children's Books");
                            genres.add("Classic Literature");
                            genres.add("Coming-of-Age");
                            genres.add("Contemporary");
                            genres.add("Cookbooks");
                            genres.add("Crafts & Hobbies");
                            genres.add("Cultural");
                            genres.add("Cyberpunk");
                            genres.add("Dark Fantasy");
                            genres.add("Dystopian");
                            genres.add("Education & Teaching");
                            genres.add("Epistolary");
                            genres.add("Essays");
                            genres.add("Family Saga");
                            genres.add("Fantasy");
                            genres.add("Fiction");
                            genres.add("Game Lit");
                            genres.add("Graphic Novels");
                            genres.add("Gothic");
                            genres.add("Health & Wellness");
                            genres.add("Historical Fiction");
                            genres.add("History");
                            genres.add("Holiday");
                            genres.add("Home & Garden");
                            genres.add("Horror");
                            genres.add("Humor");
                            genres.add("Inspirational");
                            genres.add("Legal Thrillers");
                            genres.add("LGBTQ+");
                            genres.add("Magical Realism");
                            genres.add("Martial Arts");
                            genres.add("Medical Thrillers");
                            genres.add("Memoir");
                            genres.add("Music");
                            genres.add("Mystery");
                            genres.add("Nature & Environment");
                            genres.add("New Adult");
                            genres.add("Non-Fiction");
                            genres.add("Paranormal");
                            genres.add("Parenting");
                            genres.add("Poetry");
                            genres.add("Political");
                            genres.add("Political Thrillers");
                            genres.add("Psychology");
                            genres.add("Religious");
                            genres.add("Romance");
                            genres.add("Satire");
                            genres.add("Science");
                            genres.add("Science Fiction");
                            genres.add("Self-Help");
                            genres.add("Short Stories");
                            genres.add("Sociology");
                            genres.add("Space Opera");
                            genres.add("Spirituality");
                            genres.add("Sports");
                            genres.add("Steampunk");
                            genres.add("Suspense");
                            genres.add("Thriller");
                            genres.add("Time Travel");
                            genres.add("Travel");
                            genres.add("True Adventure");
                            genres.add("True Crime");
                            genres.add("Urban Fiction");
                            genres.add("War & Military");
                            genres.add("Westerns");
                            genres.add("Whodunit");
                            genres.add("Women's Fiction");
                            genres.add("Young Adult");

                            ArrayAdapter<String> genresAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, genres);
                            genresAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                            spnSearchOptions.setAdapter(genresAdapter);

                            break;
                        case "Condition":
                            spnSearchOptions.setVisibility(View.VISIBLE);
                            etSearchResult.setVisibility(View.GONE);
                            if (Objects.equals(currentJob, LibraryActivity.jobSearch)) {
                                cbSearchCountry.setVisibility(View.VISIBLE);

                            }

                            ArrayList<String> conditions = new ArrayList<>();
                            conditions.add("Perfect");
                            conditions.add("Used");
                            conditions.add("Old");

                            ArrayAdapter<String> conditionsAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, conditions);
                            conditionsAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                            spnSearchOptions.setAdapter(conditionsAdapter);

                            break;
                        case "Exchange":
                            spnSearchOptions.setVisibility(View.VISIBLE);
                            etSearchResult.setVisibility(View.GONE);
                            if (Objects.equals(currentJob, LibraryActivity.jobSearch)) {
                                cbSearchCountry.setVisibility(View.VISIBLE);

                            }

                            ArrayList<String> exchanges = new ArrayList<>();
                            exchanges.add("Permanent");
                            exchanges.add("Temporary");

                            ArrayAdapter<String> exchangesAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, exchanges);
                            exchangesAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                            spnSearchOptions.setAdapter(exchangesAdapter);

                            break;

                    }
                }
                else {
                    btnSearchFind.setVisibility(View.GONE);
                    spnSearchOptions.setVisibility(View.GONE);
                    etSearchResult.setVisibility(View.GONE);
                    cbSearchCountry.setVisibility(View.GONE);
                    cbSearchCity.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cbSearchCountry.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cbSearchCity.setVisibility(View.VISIBLE);
                }
                else {
                    cbSearchCity.setVisibility(View.GONE);
                    cbSearchCity.setChecked(false);
                }
            }
        });

        btnSearchFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String category = "";
                String search = "";

                if (etSearchResult.getVisibility() == View.VISIBLE) {

                    if (etSearchResult.getText().toString().isEmpty()) {
                        etSearchResult.setError("Enter a search result");
                    }
                    else {
                        category = spnSearchCategory.getSelectedItem().toString();
                        search = etSearchResult.getText().toString().toLowerCase();
                    }
                }
                else if (spnSearchOptions.getVisibility() == View.VISIBLE) {
                    category = spnSearchCategory.getSelectedItem().toString();
                    search = spnSearchOptions.getSelectedItem().toString();
                }
                else {
                    category = spnSearchCategory.getSelectedItem().toString();
                }

                String stateOrCity = "";

                if (cbSearchCity.getVisibility() == View.VISIBLE && cbSearchCity.isChecked()) {
                    stateOrCity = "city";
                }
                else if (cbSearchCountry.getVisibility() == View.VISIBLE && cbSearchCountry.isChecked() ) {
                    stateOrCity = "country";
                }


                if (!category.isEmpty() && listener != null) {
                    Bundle resultsBundle = new Bundle();
                    resultsBundle.putString("category", category);
                    resultsBundle.putString("search", search);
                    resultsBundle.putString("stateOrCity", stateOrCity);

                    listener.onDialogResult(resultsBundle);

                    dismiss();
                }
            }
        });
    }

    private void setCategories() {
        ArrayList<String> categories  = new ArrayList<>();
        categories.add("Choose a category");
        if (Objects.equals(currentJob, LibraryActivity.jobSearch)) {
            if (!emails.isEmpty()) {
                categories.add("Email");
            }
        }
        else {
            categories.add("My books");
        }
        categories.add("Title");
        categories.add("Author");
        categories.add("Genre");
        categories.add("Condition");
        categories.add("Exchange");
        ArrayAdapter<String> conditionsAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categories);
        conditionsAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spnSearchCategory.setAdapter(conditionsAdapter);
    }

    private void initializeView() {
        spnSearchCategory = findViewById(R.id.spnSearchCategory);
        spnSearchOptions = findViewById(R.id.spnSearchOptions);
        etSearchResult = findViewById(R.id.etSearchResult);
        btnSearchFind = findViewById(R.id.btnSearchFind);
        cbSearchCountry = findViewById(R.id.cbSearchCountry);
        cbSearchCity = findViewById(R.id.cbSearchCity);

        cbSearchCountry.setText("Search in " + country);
        cbSearchCity.setText("Search in " + city);

        spnSearchOptions.setVisibility(View.GONE);
        etSearchResult.setVisibility(View.GONE);
        btnSearchFind.setVisibility(View.GONE);
        cbSearchCountry.setVisibility(View.GONE);
        cbSearchCity.setVisibility(View.GONE);
    }


    public interface DialogResultsListener {
        void onDialogResult(Bundle data); // Method to receive data from the dialog
    }
}
