package co.il.liam.booktobook.ADAPTERS;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.List;

import co.il.liam.booktobook.ACTIVITIES.MainScreenActivity;
import co.il.liam.booktobook.R;
import co.il.liam.booktobook.VPbuttonsItem;

public class VPbuttonsAdapter extends RecyclerView.Adapter<VPbuttonsAdapter.viewHolder>{

    private List<VPbuttonsItem> vpItems;
    private ViewPager2 vpWidget;
    private Context context;
    private ViewPagerButtonClickListener clickListener;

    public VPbuttonsAdapter(List<VPbuttonsItem> vpItems, ViewPager2 vpWidget, Context context, ViewPagerButtonClickListener clickListener) {
        this.vpItems = vpItems;
        this.vpWidget = vpWidget;
        this.context = context;
        this.clickListener = clickListener;

        for (int i = 0; i < 6; i++) {
            vpItems.addAll(vpItems);
        }
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewHolder( LayoutInflater.from(parent.getContext()).inflate(R.layout.viewpager_item_button, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        holder.tvButtonTitle.setText(vpItems.get(position).getTitle());
        holder.ivButton.setImageResource(vpItems.get(position).getImage());

        if (position == vpItems.size() - 2) {
            vpWidget.post(runnable);
        }


        holder.ivButton.setOnTouchListener(new View.OnTouchListener() {
            private float startX, startY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        float endX = event.getX();
                        float endY = event.getY();
                        float dx = endX - startX;
                        float dy = endY - startY;
                        float distance = (float) Math.sqrt(dx * dx + dy * dy);
                        if (distance < 10) { // Adjust the threshold for what constitutes a click
                            // Click detected
                            // Calculate the boundaries of the square around the center
                            int squareSize = 400;
                            int centerX = v.getWidth() / 2;
                            int centerY = v.getHeight() / 2;
                            int squareLeft = centerX - squareSize / 2;
                            int squareTop = centerY - squareSize / 2;
                            int squareRight = centerX + squareSize / 2;
                            int squareBottom = centerY + squareSize / 2;

                            // Get the touch coordinates relative to the view
                            float x = event.getX();
                            float y = event.getY();

                            // Check if the touch coordinates fall within the square area around the center of the view
                            if (x >= squareLeft && x <= squareRight && y >= squareTop && y <= squareBottom) {
                                // Touch occurred within the square area around the center of the view
                                // Your logic here
//                                Toast.makeText(context, String.valueOf(position % 5), Toast.LENGTH_SHORT).show();
//                                actionPosition(position);
                                clickListener.onViewPagerButtonClicked(position);
                            }
                        }
                        return true;
                    default:
                        return false;
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return vpItems.size();
    }

    class viewHolder extends RecyclerView.ViewHolder {

         TextView tvButtonTitle;
         ImageView ivButton;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            tvButtonTitle = itemView.findViewById(R.id.tvButtonTitle);
            ivButton = itemView.findViewById(R.id.ivVpButton);
        }

    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            vpItems.addAll(vpItems);
            notifyDataSetChanged();
        }
    };

    public interface ViewPagerButtonClickListener {
        void onViewPagerButtonClicked(int position);
    }
}
