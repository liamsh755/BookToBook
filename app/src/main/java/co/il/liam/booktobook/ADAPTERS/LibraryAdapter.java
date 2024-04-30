package co.il.liam.booktobook.ADAPTERS;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import co.il.liam.booktobook.R;
import co.il.liam.model.Book;
import co.il.liam.model.Library;

public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.LibraryHolder> {

    private Context context;
    private int bookLayout;
    private Library library;

    private OnItemClickListener listener;
    private OnItemLongClickListener longListener;

    public LibraryAdapter(Context context, int bookLayout, Library library, OnItemClickListener listener, OnItemLongClickListener longListener) {
        this.context = context;
        this.bookLayout = bookLayout;
        this.library = library;
        this.listener = listener;
        this.longListener = longListener;
    }

    @NonNull
    @Override
    public LibraryAdapter.LibraryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LibraryHolder(LayoutInflater.from(context).inflate(bookLayout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LibraryAdapter.LibraryHolder holder, int position) {
        Book book = library.get(position);

        if (book != null) {
            holder.bind(book, listener, longListener);
        }
    }

    @Override
    public int getItemCount() {
        return (library != null) ? library.size() : 0;
    }


    public static class LibraryHolder extends RecyclerView.ViewHolder {
        private TextView tvBookTitle;
        private TextView tvBookAuthor;
        private ImageView ivBookImage;

        public LibraryHolder(@NonNull View itemView) {
            super(itemView);
//            tvBookTitle = itemView.findViewById(R.id.tvBookTitle);
//            tvBookAuthor = itemView.findViewById(R.id.tvBookAuthor);
//            ivBookImage = itemView.findViewById(R.id.ivBookImage);
        }

        public void bind (Book book, OnItemClickListener listener, OnItemLongClickListener longListener) {
            tvBookTitle.setText(book.getTitle());
            tvBookAuthor.setText(book.getAuthor());
            ivBookImage.setImageBitmap(book.getImage());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClicked(book);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    longListener.onItemLongClicked(book);
                    return true;
                }
            });
        }
    }

    public void setLibrary(Library library) {
        this.library = library;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        public void onItemClicked(Book book);
    }

    public interface OnItemLongClickListener {
        public boolean onItemLongClicked(Book book);
    }
}
