package co.il.liam.booktobook.ACTIVITIES;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import co.il.liam.booktobook.R;
import co.il.liam.viewmodel.UsersViewModel;

public class ChangePasswordActivity extends BaseActivity {

    private EditText etChangePassword;
    private EditText etChangeRePassword;
    private Button btnChangeConfirm;
    private TextView tvChangeCancel;

    private UsersViewModel usersViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        initializeViews();
        setListeners();
        setObservers();
    }

    private void setObservers() {
        usersViewModel = new ViewModelProvider(this).get(UsersViewModel.class);

        usersViewModel.getChangedPassword().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    Toast.makeText(getApplicationContext(), "Password changed!", Toast.LENGTH_SHORT).show();
                    Intent intentGoBack = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intentGoBack);
                }
            }
        });
    }

    @Override
    protected void initializeViews() {
        etChangePassword = findViewById(R.id.etChangePassword);
        etChangeRePassword = findViewById(R.id.etChangeRePassword);
        btnChangeConfirm = findViewById(R.id.btnChangeConfirm);
        tvChangeCancel = findViewById(R.id.tvChangeCancel);
    }

    @Override
    protected void setListeners() {
        tvChangeCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentGoBack = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intentGoBack);
            }
        });

        btnChangeConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sPassword = String.valueOf(etChangePassword.getText());
                String sRePassword = String.valueOf(etChangeRePassword.getText());

                if (sPassword.isEmpty()) {
                    etChangePassword.setError("Please enter a new password");
                }
                else if (sRePassword.isEmpty()) {
                    etChangeRePassword.setError("Please confirm your new password");
                }
                else if (!sPassword.equals(sRePassword)) {
                    etChangeRePassword.setError("Passwords don't match");
                }
                else if (sPassword.length() < 8) {
                    etChangePassword.setError("Password must be at least 8 characters long");
                }
                else {
                    Intent sentInfoIntent = getIntent();
                    String email = sentInfoIntent.getStringExtra("email");
                    if (email != null) {
                        usersViewModel.changePassword(email, sPassword);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Email transfer problem", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}