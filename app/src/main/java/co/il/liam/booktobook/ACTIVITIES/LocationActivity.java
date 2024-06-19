package co.il.liam.booktobook.ACTIVITIES;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import co.il.liam.booktobook.CheckInternetConnection;
import co.il.liam.booktobook.LoadingDialog;
import co.il.liam.booktobook.MapFragment;
import co.il.liam.booktobook.R;
import co.il.liam.model.User;
import co.il.liam.viewmodel.UsersViewModel;

public class LocationActivity extends BaseActivity {
    private TextView tvLocationGoBack;
    private FrameLayout flLocationMap;


    private LoadingDialog loadingBooksDialog;


    private UsersViewModel usersViewModel;
    private User loggedUser;


    private GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        setScreenOrientation();

        initializeViews();
        setListeners();
        setObservers();
        getLocations();
    }

    private void setObservers() {
        usersViewModel = new ViewModelProvider(this).get(UsersViewModel.class);

        usersViewModel.getFoundAllLocations().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> locations) {

                loadingBooksDialog.dismiss();

                locations = locations.stream().distinct().collect(Collectors.toList());

                //final location variables
                String userLocation = loggedUser.getState() + " - " + loggedUser.getCity();
                ArrayList<String> otherLocations = new ArrayList<>();

                //filter the user location
                for (String address : locations) {
                    if (!Objects.equals(address, userLocation)) {
                        otherLocations.add(address);
                    }
                }

                //set up the map
                Fragment fragment = new MapFragment();
                Bundle allLocations = new Bundle();
                allLocations.putStringArrayList("locations", otherLocations);
                allLocations.putString("userLocation", userLocation);
                fragment.setArguments(allLocations);
                getSupportFragmentManager().beginTransaction().replace(R.id.flLocationMap, fragment).commit();
            }
        });
    }

    private void getLocations() {
        LoadingDialog loadingDialog = new LoadingDialog(LocationActivity.this, "Finding locations...", "This will only take a few seconds");
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        loadingBooksDialog = loadingDialog;

        if (CheckInternetConnection.check(LocationActivity.this)) {
            loadingBooksDialog.show();

            usersViewModel.findAllLocations();
        }
    }

    @Override
    protected void initializeViews() {
        tvLocationGoBack = findViewById(R.id.tvLocationGoBack);
        flLocationMap = findViewById(R.id.flLocationMap);

        Intent userInfoIntent = getIntent();
        loggedUser = (User) userInfoIntent.getSerializableExtra("user");
    }

    @Override
    protected void setListeners() {
        tvLocationGoBack.setOnClickListener(new View.OnClickListener() {
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