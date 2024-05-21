package co.il.liam.booktobook.ACTIVITIES;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class RegisterActivity extends BaseActivity {
    private EditText etRegUsername;
    private EditText etRegEmail;
    private EditText etRegPassword;
    private EditText etRegRePassword;
    private Button btnRegRegister;
    private ProgressBar pbWait;
    private TextView tvLoginGo;
    private TextView tvRegGoBack;

    private UsersViewModel usersViewModel;

    private User newUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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
                if (aBoolean){
                    Toast.makeText(RegisterActivity.this, "User saved successfully", Toast.LENGTH_SHORT).show();
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
                else {
                    Toast.makeText(RegisterActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void initializeViews() {
        etRegUsername = findViewById(R.id.etRegUsername);
        etRegEmail = findViewById(R.id.etRegEmail);
        etRegPassword = findViewById(R.id.etRegPassword);
        etRegRePassword = findViewById(R.id.etRegRePassword);
        btnRegRegister = findViewById(R.id.btnRegRegister);
        pbWait = findViewById(R.id.pbRegister);
        tvLoginGo = findViewById(R.id.tvRegGo);
        tvRegGoBack = findViewById(R.id.tvRegGoBack);

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

                    newUser.setEmail(sEmail);
                    newUser.setUsername(sUsername);
                    newUser.setPassword(sPassword);

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
}