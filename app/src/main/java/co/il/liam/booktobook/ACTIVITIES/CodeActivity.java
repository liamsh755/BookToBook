package co.il.liam.booktobook.ACTIVITIES;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Locale;
import java.util.Random;

import co.il.liam.booktobook.R;

public class CodeActivity extends BaseActivity {
    private TextView tvCodeEmail;
    private EditText etCode1;
    private EditText etCode2;
    private EditText etCode3;
    private EditText etCode4;
    private EditText etCode5;
    private EditText etCode6;
    private TextView tvCodeTimer;
    private Button btnCodeConfirm;
    private TextView tvCodeCancel;
    private TextView tvCodeNoCode;

    private String code = "";
    private String recipientEmail = "";
    private static final String SUPPORT_EMAIL = "Liam.Shvarts@gmail.com";
    private static final String SUPPORT_EMAIL_PASSWORD = "Carrots755";

    private long remainingTime = 300000; // Milliseconds (5 minutes)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);
        initializeViews();
        setListeners();
        sendInitialCodeEmail();
        setEmailText();
        startTimer();
    }

    private void sendInitialCodeEmail() {

        code = generateCode();
        Toast.makeText(getApplicationContext(), code, Toast.LENGTH_LONG).show();

        Intent sentInfoIntent = getIntent();
        String name = sentInfoIntent.getStringExtra("name");
        recipientEmail = sentInfoIntent.getStringExtra("email");

        if (name != null) {
            String emailSubject = "Password reset for BookToBook";
            String emailBody = "Hi, " + name + "!\n\n" +
                    getString(R.string.email_msg1)
                    + "Your verification code: " + code + "\n\n" +
                    getString(R.string.email_msg2);

            //send email
        }


    }

    private void startTimer() {
        new CountDownTimer(remainingTime, 1000) { // Milliseconds between updates (1 second)
            @Override
            public void onTick(long millisUntilFinished) {
                remainingTime = millisUntilFinished;

                if (remainingTime < 30000) {
                    tvCodeTimer.setTextColor(Color.RED);
                }

                updateTimerText();
            }

            @Override
            public void onFinish() {
                tvCodeTimer.setText("00:00");
                Intent intentGoCode = new Intent(getApplicationContext(), StartActivity.class);
                startActivity(intentGoCode);
            }
        }.start();
    }

    private void updateTimerText() {
        long minutes = (remainingTime / 1000) / 60;
        long seconds = (remainingTime / 1000) % 60;

        String formattedTime = String.format(Locale.US, "%02d:%02d", minutes, seconds);
        tvCodeTimer.setText(formattedTime);
    }

    private void setEmailText() {
        tvCodeEmail.setText(hideEmail(recipientEmail));
    }

    @Override
    protected void initializeViews() {
        tvCodeEmail = findViewById(R.id.tvCodeEmail);
        etCode1 = findViewById(R.id.etCode1);
        etCode2 = findViewById(R.id.etCode2);
        etCode3 = findViewById(R.id.etCode3);
        etCode4 = findViewById(R.id.etCode4);
        etCode5 = findViewById(R.id.etCode5);
        etCode6 = findViewById(R.id.etCode6);
        tvCodeTimer = findViewById(R.id.tvCodeTimer);
        btnCodeConfirm = findViewById(R.id.btnCodeConfirm);
        tvCodeCancel = findViewById(R.id.tvCodeCancel);
        tvCodeNoCode = findViewById(R.id.tvCodeNoCode);
    }

    @Override
    protected void setListeners() {
        tvCodeCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentGoBack = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intentGoBack);
            }
        });

        btnCodeConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etCode6.getText().toString().isEmpty()) {
                    etCode6.setError("Enter the full code");
                }

                else {
                    String givenCode = etCode1.getText().toString() + etCode2.getText().toString() +
                            etCode3.getText().toString() + etCode4.getText().toString() +
                            etCode5.getText().toString() + etCode6.getText().toString();

                    if (givenCode.equals(code)) {
                        Intent intentChangePassword = new Intent(getApplicationContext(), ChangePasswordActivity.class);
                        intentChangePassword.putExtra("email", recipientEmail);
                        startActivity(intentChangePassword);
                    }

                    else {
                        Toast.makeText(getApplicationContext(), "Wrong code entered", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        etCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                if (text.length() == 1) {
                    etCode2.setEnabled(true);
                    etCode2.requestFocus();
                    etCode1.setEnabled(false);
                }
            }
        });
        etCode2.addTextChangedListener(new TextWatcher() {
            int prevLength = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                prevLength = s.length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                int currentLength = text.length();

                if (prevLength > currentLength && prevLength == 1) {
                    etCode1.setEnabled(true);
                    etCode1.requestFocus();
                    etCode2.setEnabled(false);

                } else if (currentLength == 1) {
                    etCode3.setEnabled(true);
                    etCode3.requestFocus();
                    etCode2.setEnabled(false);
                }

                prevLength = currentLength;
            }
        });
        etCode3.addTextChangedListener(new TextWatcher() {
            int prevLength = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                prevLength = s.length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                int currentLength = text.length();

                if (prevLength > currentLength && prevLength == 1) {
                    etCode2.setEnabled(true);
                    etCode2.requestFocus();
                    etCode3.setEnabled(false);

                } else if (currentLength == 1) {
                    etCode4.setEnabled(true);
                    etCode4.requestFocus();
                    etCode3.setEnabled(false);
                }

                prevLength = currentLength;
            }
        });
        etCode4.addTextChangedListener(new TextWatcher() {
            int prevLength = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                prevLength = s.length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                int currentLength = text.length();

                if (prevLength > currentLength && prevLength == 1) {
                    etCode3.setEnabled(true);
                    etCode3.requestFocus();
                    etCode4.setEnabled(false);

                } else if (currentLength == 1) {
                    etCode5.setEnabled(true);
                    etCode5.requestFocus();
                    etCode4.setEnabled(false);
                }

                prevLength = currentLength;
            }
        });
        etCode5.addTextChangedListener(new TextWatcher() {
            int prevLength = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                prevLength = s.length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                int currentLength = text.length();

                if (prevLength > currentLength && prevLength == 1) {
                    etCode4.setEnabled(true);
                    etCode4.requestFocus();
                    etCode5.setEnabled(false);

                } else if (currentLength == 1) {
                    etCode6.setEnabled(true);
                    etCode6.requestFocus();
                    etCode5.setEnabled(false);
                }

                prevLength = currentLength;
            }
        });
        etCode6.addTextChangedListener(new TextWatcher() {
            int prevLength = 0;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                prevLength = s.length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                int currentLength = text.length();

                if (prevLength > currentLength && prevLength == 1) {
                    etCode5.setEnabled(true);
                    etCode5.requestFocus();
                    etCode6.setEnabled(false);
                }

                prevLength = currentLength;
            }
        });

        etCode1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                String text = etCode1.getText().toString();

                if (!text.isEmpty() && keyCode != KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                    etCode2.setEnabled(true);
                    etCode2.requestFocus();
                    etCode1.setEnabled(false);
                    return true;
                }

                return false;
            }
        });
        etCode2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                String text = etCode2.getText().toString();

                if (text.isEmpty() && keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_UP) {
                    etCode1.setEnabled(true);
                    etCode1.requestFocus();
                    etCode2.setEnabled(false);
                    return true;
                }

                else if (!text.isEmpty() && keyCode != KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                    etCode3.setEnabled(true);
                    etCode3.requestFocus();
                    etCode2.setEnabled(false);
                    return true;
                }

                return false;
            }
        });
        etCode3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                String text = etCode3.getText().toString();

                if (text.isEmpty() && keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_UP) {
                    etCode2.setEnabled(true);
                    etCode2.requestFocus();
                    etCode3.setEnabled(false);
                    return true;
                }

                else if (!text.isEmpty() && keyCode != KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                    etCode4.setEnabled(true);
                    etCode4.requestFocus();
                    etCode3.setEnabled(false);
                    return true;
                }

                return false;
            }
        });
        etCode4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                String text = etCode4.getText().toString();
                if (text.isEmpty() && keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_UP) {
                    etCode3.setEnabled(true);
                    etCode3.requestFocus();
                    etCode4.setEnabled(false);
                    return true;
                }

                else if (!text.isEmpty() && keyCode != KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                    etCode5.setEnabled(true);
                    etCode5.requestFocus();
                    etCode4.setEnabled(false);
                    return true;
                }

                return false;
            }
        });
        etCode5.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                String text = etCode5.getText().toString();
                if (text.isEmpty() && keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_UP) {
                    etCode4.setEnabled(true);
                    etCode4.requestFocus();
                    etCode5.setEnabled(false);
                    return true;
                }

                else if (!text.isEmpty() && keyCode != KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                    etCode6.setEnabled(true);
                    etCode6.requestFocus();
                    etCode5.setEnabled(false);
                    return true;
                }

                return false;
            }
        });
        etCode6.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                String text = etCode6.getText().toString();
                if (text.isEmpty() && keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_UP) {
                    etCode5.setEnabled(true);
                    etCode5.requestFocus();
                    etCode6.setEnabled(false);
                    return true;
                }
                return false;
            }
        });

        tvCodeNoCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(CodeActivity.this)
                        .setTitle("Can't find my code")
                        .setMessage("Make sure you've written your email correctly\nTo contact support email BookToBookSupport@gmail.com")
                        .setCancelable(true)
                        .setPositiveButton("Resend code", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "When email work do this\n" + code, Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton("Retype email", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intentGoBack = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                                startActivity(intentGoBack);
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }


    public static String hideEmail(String email) {
        if (email == null || email.isEmpty()) {
            return "";
        }

        String[] parts = email.split("@");

        if (parts.length != 2) {
            return email;
        }

        String localPart = parts[0];
        String domainPart = parts[1];

        StringBuilder hiddenLocalPart = new StringBuilder();

        hiddenLocalPart.append(localPart.charAt(0));
        for (int i = 1; i < localPart.length() - 1; i++) {
            hiddenLocalPart.append('*');
        }
        hiddenLocalPart.append(localPart.charAt(localPart.length() - 1));

        return hiddenLocalPart + "@" + domainPart;
    }



    public static void sendCodeEmail() {
        //                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                Properties properties = new Properties();
//                                properties.put("mail.smtp.auth", "true");
//                                properties.put("mail.smtp.starttls.enable", "true");
//                                properties.put("mail.smtp.host", "smtp.gmail.com"); // Your SMTP server host
//                                properties.put("mail.smtp.port", "465"); // Your SMTP server port
//
//                                Session session = Session.getInstance(properties, new Authenticator() {
//                                    @Override
//                                    protected PasswordAuthentication getPasswordAuthentication() {
//                                        return new PasswordAuthentication(SUPPORT_EMAIL, SUPPORT_EMAIL_PASSWORD);
//                                    }
//                                });
//
//                                MimeMessage message = new MimeMessage(session);
//                                message.setFrom(new InternetAddress(SUPPORT_EMAIL));
//                                message.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(recepientEmail));
//                                message.setSubject(emailSubject);
//                                message.setText(emailBody);
//
//                                Transport.send(message);
//                                Toast.makeText(getApplicationContext(), "Email sent!", Toast.LENGTH_SHORT).show();
//
//                            }
//                            catch (MessagingException e) {
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        Toast.makeText(getApplicationContext(), "Failed to send email", Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//                            }
//                            catch (Exception e) {
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        Toast.makeText(getApplicationContext(), "Unknown error:" + e, Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//                            }
//                        }
//                    }).start();
//                }
    }

    public static String generateCode() {
        Random random = new Random();
        String sCode = String.valueOf(random.nextInt(900000) + 100000);
        return sCode;
    }
}