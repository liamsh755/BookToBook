package co.il.liam.booktobook.ADAPTERS;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import co.il.liam.booktobook.R;
import co.il.liam.model.BookRec;
import co.il.liam.model.BookRecs;
import co.il.liam.model.Chats;

public class BookRecsAdapter extends RecyclerView.Adapter<BookRecsAdapter.BookRecViewHolder> {
    private Context context;
    private int bookRecLayout;
    private BookRecs bookRecs;

    public BookRecsAdapter(Context context, int bookRecLayout, BookRecs bookRecs) {
        this.context = context;
        this.bookRecLayout = bookRecLayout;
        this.bookRecs = bookRecs;
    }
    @NonNull
    @Override
    public BookRecViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BookRecViewHolder(LayoutInflater.from(context).inflate(bookRecLayout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BookRecViewHolder holder, int position) {
        BookRec bookRec = bookRecs.get(position);

        if (bookRec != null) {
            holder.bind(bookRec);
        }
    }

    @Override
    public int getItemCount() {
        return bookRecs != null ? bookRecs.size() : 0;
    }

    public void setBookRecs(BookRecs bookRecs) {
        this.bookRecs = bookRecs;
        notifyDataSetChanged();
    }



    class BookRecViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDiscoverBookTitle;
        private TextView tvDiscoverBookAuthors;
        private TextView tvDiscoverBookPages;
        private TextView tvDiscoverBookDescription;
        private ImageView ivDiscoverBookPhoto;

        public BookRecViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDiscoverBookTitle = itemView.findViewById(R.id.tvDiscoverBookTitle);
            tvDiscoverBookAuthors = itemView.findViewById(R.id.tvDiscoverBookAuthors);
            tvDiscoverBookPages = itemView.findViewById(R.id.tvDiscoverBookPages);
            tvDiscoverBookDescription = itemView.findViewById(R.id.tvDiscoverBookDescription);
            ivDiscoverBookPhoto = itemView.findViewById(R.id.ivDiscoverBookPhoto);
        }

        public void bind(BookRec bookRec) {
            tvDiscoverBookTitle.setText(bookRec.getTitle());
            StringBuilder allAuthors = new StringBuilder();
            List<String> authors = bookRec.getAuthors();
            for (String author : authors) {
                allAuthors.append(author).append("\n");
            }
            tvDiscoverBookAuthors.setText(authors.toString());
            tvDiscoverBookPages.setText("Page count -" + String.valueOf(bookRec.getPageCount()));
            tvDiscoverBookDescription.setText(bookRec.getDescription());
            //ivDiscoverBookPhoto.setImageBitmap(bookRec.getPhoto());
        }
    }


}
