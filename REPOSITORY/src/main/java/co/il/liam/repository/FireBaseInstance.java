package co.il.liam.repository;

import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

public class FireBaseInstance {

    private static volatile FireBaseInstance _instance = null;
    public static FirebaseApp app;

    private FireBaseInstance(Context context) {
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setProjectId("book-to-book-e5c56")
                .setApplicationId("1:85416729973:android:df330f9395b0737077073c")
                .setApiKey("AIzaSyAxGa8vc9BoLC7HNDrsgIXQQqEv44Qtb4A")
                .setStorageBucket("book-to-book-e5c56.appspot.com")
                .build();

        app = FirebaseApp.initializeApp(context, options);
    }

    public static FireBaseInstance instance (Context context) {
        if (_instance == null) {
            synchronized (FireBaseInstance.class) {
                if (_instance == null) {
                    _instance = new FireBaseInstance(context);
                }
            }
        }
        return _instance;
    }

}
