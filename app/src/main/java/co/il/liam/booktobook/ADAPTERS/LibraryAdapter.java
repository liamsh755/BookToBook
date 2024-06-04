package co.il.liam.booktobook.ADAPTERS;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import co.il.liam.booktobook.R;
import co.il.liam.helper.BitMapHelper;
import co.il.liam.model.Book;
import co.il.liam.model.Books;
import de.hdodenhof.circleimageview.CircleImageView;

public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.LibraryHolder> {

    private Context context;
    private Books books;

    private OnItemClickListener listener;
    private OnItemLongClickListener longListener;

    public int expandedPosition = -1;

    public LibraryAdapter(Context context, Books books, OnItemClickListener listener, OnItemLongClickListener longListener) {
        this.context = context;
        this.books = books;
        this.listener = listener;
        this.longListener = longListener;
    }

    private int getLayoutForBook(Book book) {
        int[] layouts = {R.layout.book_thick_tall, R.layout.book_thick_med, R.layout.book_thick_short,
                         R.layout.book_thin_tall,  R.layout.book_thin_med,  R.layout.book_thin_short  };

        int layout = 0;

        if (book.getWidth() == Book.Width.THIN) {
            layout += 3;
        }

        switch (book.getHeight()) {
            case TALL:
                layout += 0;  //redundant, for readability
                break;

            case MEDIUM:
                layout += 1;
                break;

            case SHORT:
                layout += 2;
                break;

        }

        return layouts[layout];
    }

    @Override
    public int getItemViewType(int position) {
        Book book  = books.get(position);
        return getLayoutForBook(book);
    }

    @NonNull
    @Override
    public LibraryAdapter.LibraryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LibraryHolder(LayoutInflater.from(context).inflate(viewType, parent, false), this);
    }

    @Override
    public void onBindViewHolder(@NonNull LibraryAdapter.LibraryHolder holder, int position) {
        Book book = books.get(position);

        if (book != null) {
            holder.bind(book, listener, longListener);
        }
    }

    @Override
    public int getItemCount() {
        return (books != null) ? books.size() : 0;
    }

    public void enlargeItem(int position) {
        if (expandedPosition == position) {
            expandedPosition = -1;
        }
        else {
            expandedPosition = position;
        }
        notifyDataSetChanged();
    }


    public static class LibraryHolder extends RecyclerView.ViewHolder {
        private LibraryAdapter adapterInstance;

        private TextView tvBookTitle;
        private TextView tvBookAuthor;
        private CircleImageView civBookImage;

        private CardView cvBookBackground;  // for changing the margin of a selected book + for mainColor
        private ConstraintLayout clBookBorder; // for selected / unselected black border

        private ConstraintLayout vBookDec1;
        private View vBookDec2;
        private View vBookDec3;
        private View vBookDec4;

        public LibraryHolder(@NonNull View itemView, LibraryAdapter adapterInstance) {
            super(itemView);

            this.adapterInstance = adapterInstance;

            tvBookTitle = itemView.findViewById(R.id.tvBookTitle);
            tvBookAuthor = itemView.findViewById(R.id.tvBookAuthor);
            civBookImage = itemView.findViewById(R.id.civBookImage);

            cvBookBackground = itemView.findViewById(R.id.cvBookBackground);
            clBookBorder = itemView.findViewById(R.id.clBookBorder);

            vBookDec1 = itemView.findViewById(R.id.vBookDec1);
            vBookDec2 = itemView.findViewById(R.id.vBookDec2);
            vBookDec3 = itemView.findViewById(R.id.vBookDec3);
            vBookDec4 = itemView.findViewById(R.id.vBookDec4);
        }

        public void bind (Book book, OnItemClickListener listener, OnItemLongClickListener longListener) {
            //set info
            tvBookTitle.setText(book.getTitle());
            tvBookAuthor.setText(book.getAuthor());
            civBookImage.setImageBitmap(BitMapHelper.decodeBase64(book.getImage()));

            //set colors
            int mainColor = book.getMainColor();
            int secColor = book.getSecColor();

            cvBookBackground.setBackgroundTintList(ColorStateList.valueOf(mainColor));
            vBookDec1.setBackgroundColor(secColor);
            vBookDec2.setBackgroundColor(secColor);
            vBookDec3.setBackgroundColor(secColor);
            vBookDec4.setBackgroundColor(secColor);

            if (mainColor == ContextCompat.getColor(itemView.getContext(), R.color.book_black)) {
                tvBookTitle.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.book_white));
                tvBookAuthor.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.book_white));
            }

            //set selected item
            int position = getAdapterPosition();

            if (position == adapterInstance.expandedPosition) {
                clBookBorder.setBackground(ContextCompat.getDrawable(itemView.getContext(), R.drawable.book_border_selected));

                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) cvBookBackground.getLayoutParams();
                layoutParams.setMargins(30, 0, 30, 0);
                cvBookBackground.setLayoutParams(layoutParams);
            }
            else {
                clBookBorder.setBackground(ContextCompat.getDrawable(itemView.getContext(), R.drawable.book_border));

                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) cvBookBackground.getLayoutParams();
                layoutParams.setMargins(0, 0, 0, 0);
                cvBookBackground.setLayoutParams(layoutParams);
            }

            //set font
            switch (book.getFont()) {
                case CLASSIC:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        Typeface font_reg = ResourcesCompat.getFont(itemView.getContext(), R.font.archivo);
                        tvBookAuthor.setTypeface(font_reg);
                        Typeface font_bold = ResourcesCompat.getFont(itemView.getContext(), R.font.archivo_semibold);
                        tvBookTitle.setTypeface(font_bold);
                    }
                    break;
                case COMIC_SANS:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        Typeface font_reg = ResourcesCompat.getFont(itemView.getContext(), R.font.comicsans);
                        tvBookAuthor.setTypeface(font_reg);
                        Typeface font_bold = ResourcesCompat.getFont(itemView.getContext(), R.font.comicsansbold);
                        tvBookTitle.setTypeface(font_bold);
                    }
                    break;
                case FUN:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        Typeface font_reg = ResourcesCompat.getFont(itemView.getContext(), R.font.lobster);
                        tvBookAuthor.setTypeface(font_reg);
                        tvBookTitle.setTypeface(font_reg);
                    }
                    break;
                case CURSIVE:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        Typeface font_reg = ResourcesCompat.getFont(itemView.getContext(), R.font.playball);
                        tvBookAuthor.setTypeface(font_reg);
                        tvBookTitle.setTypeface(font_reg);
                    }
                    break;
                case GOTHIC:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        Typeface font_reg = ResourcesCompat.getFont(itemView.getContext(), R.font.almendra);
                        tvBookAuthor.setTypeface(font_reg);
                        Typeface font_bold = ResourcesCompat.getFont(itemView.getContext(), R.font.almendra_bold);
                        tvBookTitle.setTypeface(font_bold);
                    }
                    break;
            }

            //set decoration lines
            switch (book.getDecorations()) {
                case ONE_LINE:
                    vBookDec2.setVisibility(View.GONE);
                    vBookDec4.setVisibility(View.GONE);
                    break;
                case TWO_LINES:
                    vBookDec2.setVisibility(View.GONE);
                    break;
                case THICK_LINE:
                    vBookDec3.setVisibility(View.GONE);
                    break;
            }

            // click listeners
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClicked(book, position);

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

    public void setBooks(Books books) {
        this.books = books;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClicked(Book book, int position);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClicked(Book book);
    }
}
