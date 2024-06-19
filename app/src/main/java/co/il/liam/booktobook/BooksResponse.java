package co.il.liam.booktobook;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BooksResponse {
    @SerializedName("items")
    public List<BookItem> items;

    public static class BookItem {
        @SerializedName("volumeInfo")
        public VolumeInfo volumeInfo;

        public static class VolumeInfo {
            @SerializedName("title")
            public String title;
            @SerializedName("authors")
            public List<String> authors;
            @SerializedName("description")
            public String description;
            @SerializedName("pageCount")
            public int pageCount;
            @SerializedName("imageLinks")
            public ImageLinks imageLinks;
            @SerializedName("language")
            public String language;

            public static class ImageLinks {
                @SerializedName("thumbnail")
                public String thumbnail;
            }

        }
    }
}
