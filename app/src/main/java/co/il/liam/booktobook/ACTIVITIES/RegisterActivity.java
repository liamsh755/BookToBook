package co.il.liam.booktobook.ACTIVITIES;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import co.il.liam.booktobook.CheckInternetConnection;
import co.il.liam.booktobook.R;
import co.il.liam.model.User;
import co.il.liam.viewmodel.UsersViewModel;

public class RegisterActivity extends BaseActivity {
    private EditText etRegUsername;
    private EditText etRegEmail;
    private TextView tvRegState;
    private TextView tvRegCity;
    private ImageView btnRegSelectLocation;
    private ImageView ivRegInfo;
    private EditText etRegPassword;
    private EditText etRegRePassword;
    private Button btnRegRegister;
    private ProgressBar pbWait;
    private TextView tvLoginGo;
    private TextView tvRegGoBack;
    private TextView tvRegStateTitle;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private final int REQUEST_LOCATION_PERMISSION = 50;

    private UsersViewModel usersViewModel;

    private User newUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setScreenOrientation();

        initializeViews();
        setListeners();
        setObservers();
        setSentInfo();
    }


    private void setSentInfo() {
        Intent sentInfoIntent = getIntent();
        String email = sentInfoIntent.getStringExtra("email");
        String password = sentInfoIntent.getStringExtra("password");

        etRegEmail.setText(email);
        etRegPassword.setText(password);
    }

    private void setObservers() {
        usersViewModel = new ViewModelProvider(this).get(UsersViewModel.class);

        usersViewModel.getAddedUser().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                pbWait.setVisibility(View.INVISIBLE);
                if (aBoolean) {
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                } else {
                    Toast.makeText(RegisterActivity.this, "Registration failed, Contact support", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void initializeViews() {
        etRegUsername = findViewById(R.id.etRegUsername);
        etRegEmail = findViewById(R.id.etRegEmail);
        tvRegState = findViewById(R.id.tvRegState);
        tvRegCity = findViewById(R.id.tvRegCity);
        btnRegSelectLocation = findViewById(R.id.btnRegSelectLocation);
        ivRegInfo = findViewById(R.id.ivRegInfo);
        etRegPassword = findViewById(R.id.etRegPassword);
        etRegRePassword = findViewById(R.id.etRegRePassword);
        btnRegRegister = findViewById(R.id.btnRegRegister);
        pbWait = findViewById(R.id.pbRegister);
        tvLoginGo = findViewById(R.id.tvRegGo);
        tvRegGoBack = findViewById(R.id.tvRegGoBack);
        tvRegStateTitle = findViewById(R.id.tvRegStateTitle);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        newUser = new User();
    }

    @Override
    protected void setListeners() {
        btnRegRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Valid()) {
                    String sEmail = String.valueOf(etRegEmail.getText());
                    String sUsername = String.valueOf(etRegUsername.getText());
                    String sPassword = String.valueOf(etRegPassword.getText());
                    String sState = String.valueOf(tvRegState.getText());
                    String sCity = String.valueOf(tvRegCity.getText());

                    newUser.setEmail(sEmail);
                    newUser.setUsername(sUsername);
                    newUser.setPassword(sPassword);
                    newUser.setState(sState);
                    newUser.setCity(sCity);

                    if (CheckInternetConnection.check(RegisterActivity.this)) {
                        pbWait.setVisibility(View.VISIBLE);

                        usersViewModel.add(newUser);
                    }
                }
            }
        });

        tvLoginGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sEmail = String.valueOf(etRegEmail.getText());
                String sPassword = String.valueOf(etRegPassword.getText());

                Intent intentLogin = new Intent(getApplicationContext(), LoginActivity.class);
                intentLogin.putExtra("email", sEmail);
                intentLogin.putExtra("password", sPassword);
                startActivity(intentLogin);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        tvRegGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentGoBack = new Intent(getApplicationContext(), StartActivity.class);
                startActivity(intentGoBack);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        ivRegInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setTitle("My location??");
                builder.setMessage("Worry not! The location selector only checks your state and city, nothing else.\n" +
                        "This information is necessary for other users and for you to use the app properly.");
                builder.setCancelable(true);

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        btnRegSelectLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("qqq", "clicked button");

                if (checkLocationPermission()) {
                    Log.d("qqq", "permission found");
                    checkGPSandNetworkEnabled();

                    Log.d("qqq", "gps found");
                    getLocationVariable();
                }
                else {
                    Log.d("qqq", "requesting permission");
                    requestLocationPermission();
                }
            }
        });
    }

    @Override
    protected void setScreenOrientation() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    private void checkGPSandNetworkEnabled() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gpsEnabled = false;
        boolean networkEnabled = false;

        try {
            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!gpsEnabled || !networkEnabled) {

            new AlertDialog.Builder(RegisterActivity.this)
                    .setTitle("Enable GPS service")
                    .setMessage("Your GPS is disabled, do you want to enable it?")
                    .setCancelable(false)
                    .setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();

        }
    }

    private boolean checkLocationPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocationVariable();
            } else {
                Toast.makeText(getApplicationContext(), "Please allow the app to check your location", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void getLocationVariable() {
        try {
            Log.d("qqq", "founding location");

            fusedLocationProviderClient.getLastLocation()
                    .addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                Log.d("qqq", "found location");
                                Location location = task.getResult();
                                getLocationInfo(location);
                            }
                            else {
                                Log.d("qqq", "failed to find location");
                            }
                        }
                    });

        }
        catch (Exception e) {
            Log.d("qqq", "failed to find location");
            e.printStackTrace();
        }
    }

    private void getLocationInfo(Location location) {
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses != null && !addresses.isEmpty()) {
                String stateName = addresses.get(0).getCountryName();
                String cityName = addresses.get(0).getLocality();

                tvRegState.setText(stateName);
                tvRegCity.setText(cityName);
            } else {
                Log.d("qqq", "info not found");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            Log.d("qqq", "geocoder failed");
        }
    }

    private boolean Valid() {
        String sEmail = String.valueOf(etRegEmail.getText());
        String sUsername = String.valueOf(etRegUsername.getText());
        String sPassword = String.valueOf(etRegPassword.getText());
        String sRePassword = String.valueOf(etRegRePassword.getText());


        if (sUsername.equals("")) {
            etRegUsername.setError("Enter a username");
            return false;
        }
        if (sEmail.equals("")) {
            etRegEmail.setError("Enter an email address");
            return false;
        }
        else if (sPassword.equals("")) {
            etRegPassword.setError("Enter a password");
            return false;
        }
        else if (sRePassword.equals("")) {
            etRegRePassword.setError("Retype the password");
            return false;
        }
        else if (!sPassword.equals(sRePassword)) {
            etRegPassword.setError("Passwords don't match");
            return false;
        }
        else if (sPassword.length() < 8 ) {
            etRegPassword.setError("Password must be at least 8 characters long");
            return false;
        }
        else if (!containsDigit(sPassword)) {
            etRegPassword.setError("Password must include a number");
            return false;
        }
        else if (sUsername.length() < 4) {
            etRegUsername.setError("Username must be at least 4 characters long");
            return false;
        }
        else if (!validEmail(sEmail)) {
            etRegEmail.setError("Invalid email");
            return false;
        }
        else if (tvRegState.getText().equals("")) {
            tvRegStateTitle.setError("Find your location");
            return false;
        }

        return true;
    }

    private boolean validEmail(String sEmail) {
        final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        return pattern.matcher(sEmail).matches();
    }

    public boolean containsDigit(String str) {
        return str.chars().anyMatch(Character::isDigit);
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
                        Intent intentGoBack = new Intent(getApplicationContext(), StartActivity.class);
                        startActivity(intentGoBack);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

}