package co.il.liam.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import co.il.liam.model.Book;
import co.il.liam.model.User;
import co.il.liam.repository.UsersRepository;

public class UsersViewModel extends AndroidViewModel {
    private UsersRepository usersRepository;

    private MutableLiveData<Boolean> addedUser;
    private MutableLiveData<Boolean> userDetailsRight;
    private MutableLiveData<User> userData;
    private MutableLiveData<Boolean> userExists;
    private MutableLiveData<Boolean> changedPassword;
    private MutableLiveData<Boolean> addedBook;

    public UsersViewModel(Application application) {
        super(application);

        usersRepository = new UsersRepository(application);
        addedUser = new MutableLiveData<>();
        userDetailsRight = new MutableLiveData<>();
        userData = new MutableLiveData<>();
        userExists = new MutableLiveData<>();
        changedPassword = new MutableLiveData<>();
        addedBook = new MutableLiveData<>();
    }

    public LiveData<Boolean> getAddedUser() {
        return addedUser;
    }
    public LiveData<Boolean> getUserDetailsRight() {
        return userDetailsRight;
    }
    public LiveData<User> getUserData() {
        return userData;
    }
    public LiveData<Boolean> getUserExists() {
        return userExists;
    }
    public LiveData<Boolean> getChangedPassword() {
        return changedPassword;
    }
    public LiveData<Boolean> getAddedBook() {
        return addedBook;
    }

    public void add(User user) {
        usersRepository.add(user)
                .addOnSuccessListener(aBoolean -> {
                    addedUser.setValue(aBoolean); })
                .addOnFailureListener(aBoolean -> {
                    addedUser.setValue(false); });
    }

    public void userDetailsRight(User user) {
        usersRepository.userDetailsRight(user)
                .addOnSuccessListener(aBoolean -> {
                    userDetailsRight.setValue(aBoolean); })
                .addOnFailureListener(aBoolean -> {
                    userDetailsRight.setValue(false); });
    }

    public void findUserData(User user) {
        usersRepository.getUserData(user)
                .addOnSuccessListener(usr -> {
                    userData.setValue(usr); })
                .addOnFailureListener(usr -> {
                    userData.setValue(null); });
    }

    public void userExists(User user) {
        usersRepository.emailExists(user)
                .addOnSuccessListener(aBoolean -> {
                    userExists.setValue(aBoolean); })
                .addOnFailureListener(aBoolean -> {
                    userExists.setValue(false); });
    }

    public void changePassword(String email, String newPassword) {
        usersRepository.changePassword(email, newPassword)
                .addOnSuccessListener(aBoolean -> {
                    changedPassword.setValue(aBoolean); })
                .addOnFailureListener(aBoolean -> {
                    changedPassword.setValue(false); });
    }

}
