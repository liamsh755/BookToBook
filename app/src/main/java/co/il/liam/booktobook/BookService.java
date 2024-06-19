package co.il.liam.booktobook;

import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BookService {
    private static final String BASE_URL = "https://www.googleapis.com/books/v1/";
    private GoogleBooksApiService apiService;

    public BookService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(GoogleBooksApiService.class);
    }

    public void getRandomBook(String genre, RandomBookCallback callback) {
        int startIndex = new Random().nextInt(100);
        int maxResult = 40;

        Call<BooksResponse> call = apiService.getBooksByGenre(genre, maxResult, startIndex);
        call.enqueue(new Callback<BooksResponse>() {
            @Override
            public void onResponse(Call<BooksResponse> call, Response<BooksResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<BooksResponse.BookItem> items = response.body().items;
                    if (items != null && !items.isEmpty()) {
                        int randomIndex = new Random().nextInt(items.size());
                        callback.onSuccess(items.get(randomIndex));
                    }
                    else {
                        callback.onFailure("No books were found for the given genre.");
                    }
                }
                else {
                    callback.onFailure("Failed to fetch books: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<BooksResponse> call, Throwable t) {
                callback.onFailure("Request failed: " + t.getMessage());
            }
        });
    }


    public interface RandomBookCallback {
        void onSuccess(BooksResponse.BookItem book);
        void onFailure(String errorMessage);
    }

}
