package co.il.liam.booktobook;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class EmptyDialog extends Dialog {
    private TextView tvEmptyDescription;
    private String description;
    private Button btnEmptyGoBack;
    private Boolean showButton;
    private View.OnClickListener listener;

    public EmptyDialog(@NonNull Context context, String description, Boolean showButton, View.OnClickListener listener) {
        super(context);
        this.description = description;
        this.showButton = showButton;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty_dialog);

        initializeViews();
    }

    private void initializeViews() {
        tvEmptyDescription = findViewById(R.id.tvEmptyDescription);
        btnEmptyGoBack = findViewById(R.id.btnEmptyGoBack);

        tvEmptyDescription.setText(description);
        if (showButton) {
            btnEmptyGoBack.setVisibility(View.VISIBLE);
            btnEmptyGoBack.setOnClickListener(listener);
        }
        else {
            btnEmptyGoBack.setVisibility(View.GONE);
        }
    }
}
