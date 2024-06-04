package co.il.liam.booktobook.ACTIVITIES;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.regex.Pattern;

import co.il.liam.booktobook.CheckInternetConnection;
import co.il.liam.booktobook.R;
import co.il.liam.model.User;
import co.il.liam.viewmodel.UsersViewModel;

public class LoginActivity extends BaseActivity {
    private EditText etLoginEmail;
    private EditText etLoginPassword;
    private TextView tvLoginForgot;
    private Button btnLoginEnter;
    private TextView tvRegGo;
    private ProgressBar pbWait;
    private CheckBox cbRemember;
    private TextView tvLoginGoBack;

    private UsersViewModel usersViewModel;
    private User loggedUser;

    private SharedPreferences preferences;
    static final int LOG_OUT_REQUEST_CODE = 1;

    private Boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeViews();
        setListeners();
        setObservers();
        setRemember();
        setSentInfo();
        cheat();
    }

    private void cheat() {
        etLoginEmail.setText("kaftor06@gmail.com");
        etLoginPassword.setText("Carrots755");
    }

    private void setSentInfo() {
        Intent sentInfoIntent = getIntent();
        String email = sentInfoIntent.getStringExtra("email");
        String password = sentInfoIntent.getStringExtra("password");

        etLoginEmail.setText(email);
        etLoginPassword.setText(password);
    }

    private void setRemember() {
        preferences = getSharedPreferences("remember", MODE_PRIVATE);
    }
    private void setObservers() {
        usersViewModel = new ViewModelProvider(this).get(UsersViewModel.class);

        usersViewModel.getUserDetailsRight().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    usersViewModel.findUserDataByEmail(loggedUser);
                }
                else {
                    Toast.makeText(LoginActivity.this, "Account not found. Please sign up if you don't have an account.", Toast.LENGTH_SHORT).show();
                    pbWait.setVisibility(View.INVISIBLE);
                }
            }
        });

        usersViewModel.getUserDataByEmail().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                pbWait.setVisibility(View.INVISIBLE);

                if (user != null) {
                    loggedUser = user;
                    rememberUserInfo();

                    Toast.makeText(getApplicationContext(), "Welcome!", Toast.LENGTH_SHORT).show();

                    Intent intentEnter = new Intent(getApplicationContext(), MainScreenActivity.class);
                    intentEnter.putExtra("user", loggedUser);
                    startActivityForResult(intentEnter, LOG_OUT_REQUEST_CODE);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
                else
                    Toast.makeText(LoginActivity.this, "Error loading user information", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void rememberUserInfo() {
        SharedPreferences.Editor editor = preferences.edit();
        boolean checked = cbRemember.isChecked();
        editor.putBoolean("checkbox", checked);

        if (checked) {
            editor.putString("uUsername", loggedUser.getUsername());
            editor.putString("uEmail", loggedUser.getEmail());
            editor.putString("uPassword", loggedUser.getPassword());
            editor.putString("uIdFs", loggedUser.getIdFs());
            editor.putString("uState", loggedUser.getState());
            editor.putString("uCity", loggedUser.getCity());
        }

        editor.apply();
    }

    protected void setListeners() {
        tvLoginForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sEmail = String.valueOf(etLoginEmail.getText());

                Intent intentForgot = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                intentForgot.putExtra("email", sEmail);
                startActivity(intentForgot);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        tvRegGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sEmail = String.valueOf(etLoginEmail.getText());
                String sPassword = String.valueOf(etLoginPassword.getText());

                Intent intentRegister = new Intent(getApplicationContext(), RegisterActivity.class);
                intentRegister.putExtra("email", sEmail);
                intentRegister.putExtra("password", sPassword);
                startActivity(intentRegister);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        btnLoginEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sEmail = String.valueOf(etLoginEmail.getText());
                String sPassword = String.valueOf(etLoginPassword.getText());

                if (sEmail.isEmpty()) {
                    etLoginEmail.setError("Enter your email");
                }
                else if (sPassword.isEmpty()) {
                    etLoginPassword.setError("Enter your password");
                }
                else if(sPassword.length() < 8) {
                    etLoginPassword.setError("Password must be at least 8 characters long");
                }
                else if (!validEmail(sEmail)) {
                    etLoginEmail.setError("Enter a valid email");
                }
                else {
                    User user = new User();
                    user.setEmail(sEmail);
                    user.setPassword(sPassword);

                    if (CheckInternetConnection.check(LoginActivity.this)) {
                        pbWait.setVisibility(View.VISIBLE);

                        loggedUser = user;
                        usersViewModel.userDetailsRight(user);
                    }
                }
            }
        });

        tvLoginGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentGoBack = new Intent(getApplicationContext(), StartActivity.class);
                startActivity(intentGoBack);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

    }
    protected void initializeViews() {
        etLoginEmail = findViewById(R.id.etLoginEmail);
        etLoginPassword = findViewById(R.id.etLoginPassword);
        tvLoginForgot = findViewById(R.id.tvLoginForgot);
        btnLoginEnter = findViewById(R.id.btnLoginConfirm);
        tvRegGo = findViewById(R.id.tvRegGo);
        pbWait = findViewById(R.id.pbLogin);
        cbRemember = findViewById(R.id.cbRemember);
        tvLoginGoBack = findViewById(R.id.tvLoginGoBack);
    }

    private boolean validEmail(String sEmail) {
        final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        return pattern.matcher(sEmail).matches();
    }

    private void togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible; // Toggle flag

        if (isPasswordVisible) {
            etLoginPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);  // Show password
            etLoginPassword.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.eye_logo), null);  // Change to open eye icon
        } else {
            etLoginPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);  // Hide password
            etLoginPassword.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.eye_off_logo), null);  // Change to closed eye icon
        }

        etLoginPassword.setSelection(etLoginPassword.getText().length());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LOG_OUT_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("checkbox", false);
                editor.putString("uUsername", "");
                editor.putString("uEmail", "");
                editor.putString("uPassword", "");
                editor.putString("uIdFs", "");
                editor.putString("uState", "");
                editor.putString("uCity", "");
                editor.apply();

                Intent intentLogOut = new Intent(getApplicationContext(), StartActivity.class);
                startActivity(intentLogOut);
            }
        }
    }

}