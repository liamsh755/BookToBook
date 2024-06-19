package co.il.liam.booktobook;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleBooksApiService {
    @GET("volumes")
    Call<BooksResponse> getBooksByGenre(
            @Query("q") String genre,
            @Query("maxResults") int maxResults,
            @Query("startIndex") int startIndex
    );
}
