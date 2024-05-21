package co.il.liam.repository;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class FireBaseStorage {
    private static FirebaseStorage storage;
    private static StorageReference reference;

    public FireBaseStorage(Context context){
        try{
            storage = FirebaseStorage.getInstance();
        }
        catch (Exception ex){
            FireBaseInstance instance = FireBaseInstance.instance(context);
            storage = FirebaseStorage.getInstance(FireBaseInstance.app);
        }

        reference = storage.getReference();
    }

    public static Task<String> saveToStorage(String id, byte[] bytes, String path){
        TaskCompletionSource<String> completionSource = new TaskCompletionSource<>();

        try{
            UploadTask uploadTask = getReference(id, path).putBytes(bytes);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            String url = taskSnapshot.getStorage().getDownloadUrl().toString();
                            completionSource.setResult(url);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            completionSource.setResult(null);
                            completionSource.setException(e);
                        }
                    });
        }
        catch (Exception ex){

        }

        return completionSource.getTask();
    }

    public static Task<byte[]> loadFromStorage(String id, String path){
        TaskCompletionSource<byte[]> completionPicture = new TaskCompletionSource<>();

        StorageReference storageReference;
        int maxDownloadSizeBytes = 1024 * 1024;

        try{
            getReference(id, path).getBytes(maxDownloadSizeBytes)
                    .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            completionPicture.setResult(bytes);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            completionPicture.setResult(null);
                            completionPicture.setException(e);
                        }
                    });
        }
        catch (Exception ex){
            completionPicture.setResult(null);
        }

        return completionPicture.getTask();
    }

    public static Task<Boolean> deleteFromStorage(String id, String path){
        TaskCompletionSource<Boolean> completionDelete = new TaskCompletionSource<>();

        getReference(id, path).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        completionDelete.setResult(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completionDelete.setResult(false);
                        completionDelete.setException(e);
                    }
                });

        return completionDelete.getTask();
    }



    private static StorageReference getReference(String id, String path){
        reference = storage.getReference();

        if (path != null){
            reference = makePath(reference, path);
        }

        reference = reference.child(id);

        return reference;
    }

    private static StorageReference makePath (StorageReference reference, String path)
    {
        if (path != null)
        {
            String[] vs = path.split("[\\\\/]");

            if (vs.length > 0)
                for (String v : vs)
                    reference = reference.child(v);
        }

        return reference;
    }
}
