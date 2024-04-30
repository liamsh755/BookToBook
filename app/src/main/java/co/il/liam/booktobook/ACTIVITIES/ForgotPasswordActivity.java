package co.il.liam.booktobook.ACTIVITIES;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.Properties;
import java.util.Random;
import java.util.regex.Pattern;

//import javax.mail.Authenticator;
//import javax.mail.MessagingException;
//import javax.mail.PasswordAuthentication;
//import javax.mail.Session;
//import javax.mail.Transport;
//import javax.mail.internet.MimeMessage;
//import javax.mail.internet.InternetAddress;

import co.il.liam.booktobook.R;
import co.il.liam.model.User;
import co.il.liam.viewmodel.UsersViewModel;

public class ForgotPasswordActivity extends BaseActivity {
    private EditText etForgotEmail;
    private ProgressBar pbForgot;
    private Button btnForgotRequest;
    private TextView tvCancel;

    private UsersViewModel usersViewModel;
    private User givenUser = new User();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        initializeViews();
        setListeners();
        setSentInfo();
        setObservers();
    }

    private void setObservers() {
        usersViewModel = new ViewModelProvider(this).get(UsersViewModel.class);

        usersViewModel.getUserExists().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

                if (aBoolean) {
                    usersViewModel.findUserData(givenUser);
                }

                else {
                    pbForgot.setVisibility(View.INVISIBLE);

                    String recipientEmail = givenUser.getEmail();

                    Intent intentGoCode = new Intent(getApplicationContext(), CodeActivity.class);
                    intentGoCode.putExtra("email", recipientEmail);
                    startActivity(intentGoCode);
                }

            }
        });

        usersViewModel.getUserData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String name) {
                String recipientEmail = givenUser.getEmail();

                if (!name.isEmpty()) {

                    pbForgot.setVisibility(View.INVISIBLE);

                    Intent intentGoCode = new Intent(getApplicationContext(), CodeActivity.class);
                    intentGoCode.putExtra("name", name);
                    intentGoCode.putExtra("email", recipientEmail);
                    startActivity(intentGoCode);
                }
            }
        });
    }

    private void setSentInfo() {
        Intent sentInfoIntent = getIntent();
        String email = sentInfoIntent.getStringExtra("email");

        etForgotEmail.setText(email);
    }

    @Override
    protected void initializeViews() {
        etForgotEmail = findViewById(R.id.etForgotEmail);
        pbForgot = findViewById(R.id.pbForgot);
        btnForgotRequest = findViewById(R.id.btnForgotRequest);
        tvCancel = findViewById(R.id.tvForgotCancel);
    }

    @Override
    protected void setListeners() {
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentGoBack = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intentGoBack);
            }
        });

        btnForgotRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sEmail = String.valueOf(etForgotEmail.getText());

                if (sEmail.isEmpty()) {
                    etForgotEmail.setError("Enter your email");
                }
                else if (!validEmail(sEmail)) {
                    etForgotEmail.setError("Enter a valid email");
                }
                else {
                    givenUser.setEmail(sEmail);
                    pbForgot.setVisibility(View.VISIBLE);

                    usersViewModel.userExists(givenUser);
                }
            }
        });
    }

    private boolean validEmail(String sEmail) {
        final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        return pattern.matcher(sEmail).matches();
    }
}