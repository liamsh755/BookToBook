package co.il.liam.booktobook.ADAPTERS;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import co.il.liam.booktobook.R;
import co.il.liam.booktobook.VPitem;

public class VPadapter extends RecyclerView.Adapter<VPadapter.ViewHolder> {

    private ArrayList<VPitem> vPitemArrayList;

    public VPadapter(ArrayList<VPitem> vPitemArrayList) {
        this.vPitemArrayList = vPitemArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewpager_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VPitem vPitem = vPitemArrayList.get(position);

        holder.vpIvImage.setImageResource(vPitem.imageId);
        holder.vpTvHeader.setText(vPitem.heading);
        holder.vpTvDesc.setText(vPitem.description);
    }

    @Override
    public int getItemCount() {
        return vPitemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView vpIvImage;
        TextView vpTvHeader, vpTvDesc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            vpIvImage = itemView.findViewById(R.id.vpIvImage);
            vpTvHeader = itemView.findViewById(R.id.tvButtonTitle);
            vpTvDesc = itemView.findViewById(R.id.vpTvDesc);
        }
    }
}
