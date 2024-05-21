package co.il.liam.booktobook;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

public class NoInternetDialog extends Dialog {
    private Button btnNoInternetRetry;

    public NoInternetDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.no_internet_dialog);

        initializeViews();
        setListeners();
    }

    private void setListeners() {
        btnNoInternetRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckInternetConnection.isNetworkConnected(getContext())) {
                    dismiss();
                }
            }
        });
    }

    private void initializeViews() {
        btnNoInternetRetry = findViewById(R.id.btnNoInternetRetry);
    }
}
