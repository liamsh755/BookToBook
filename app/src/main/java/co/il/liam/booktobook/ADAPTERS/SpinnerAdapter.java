package co.il.liam.booktobook.ADAPTERS;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import java.util.List;

import co.il.liam.booktobook.R;

public class SpinnerAdapter extends ArrayAdapter<String> {
    private LayoutInflater inflater;
    private boolean picture;

    public SpinnerAdapter(@NonNull Context context, int resource, @NonNull List<String> objects, boolean picture) {
        super(context, resource, objects);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.picture = picture;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {

            TextView tvSpinnerItem;
            FrameLayout flSpinnerPicture;

            if (picture) {
                convertView = inflater.inflate(R.layout.spinner_item_picture, null);

                //flSpinnerPicture = convertView.findViewById(R.id.flSpinnerPicture);
            }

            else {
                convertView = inflater.inflate(R.layout.spinner_item, null);
            }

            tvSpinnerItem = convertView.findViewById(R.id.tvSpinnerItem);
            tvSpinnerItem.setText(getItem(position));
            Typeface font = ResourcesCompat.getFont(getContext(), R.font.montserrat_alternates_medium);
            tvSpinnerItem.setTypeface(font);
        }

        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}
