package co.il.liam.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import co.il.liam.model.Chat;
import co.il.liam.model.Message;
import co.il.liam.model.Messages;
import co.il.liam.repository.MessagesRepository;

public class MessagesViewModel extends AndroidViewModel {
    private MessagesRepository messagesRepository;

    private MutableLiveData<Boolean> addedMessage;
    private MutableLiveData<Messages> chatsMessages;
    private MutableLiveData<Boolean> deletedAll;
    private MutableLiveData<Messages> listener;

    public MessagesViewModel(@NonNull Application application) {
        super(application);

        messagesRepository = new MessagesRepository(application);
        addedMessage = new MutableLiveData<>();
        chatsMessages = new MutableLiveData<>();
        deletedAll = new MutableLiveData<>();
        listener = new MutableLiveData<>();
    }

    public MutableLiveData<Boolean> getAddedMessage() {
        return addedMessage;
    }
    public MutableLiveData<Messages> getChatsMessages() {
        return chatsMessages;
    }
    public MutableLiveData<Boolean> getDeletedAll() {
        return deletedAll;
    }
    public MutableLiveData<Messages> getListener() {
        return listener;
    }


    public void addMessage(Message message) {
        messagesRepository.addMessage(message)
                .addOnSuccessListener(aBoolean -> {
                    addedMessage.setValue(aBoolean); })
                .addOnFailureListener(aBoolean -> {
                   addedMessage.setValue(false);  });
    }

    public void findChatsMessages(Chat chat) {
        messagesRepository.findChatsMessages(chat)
                .addOnSuccessListener(messages -> {
                    chatsMessages.setValue(messages); })
                .addOnFailureListener(e -> {
                    chatsMessages.setValue(null); });
    }

    public void deleteChatsMessages(Chat chat) {
        messagesRepository.deleteAll(chat)
                .addOnSuccessListener(aBoolean -> {
                    deletedAll.setValue(aBoolean); })
                .addOnFailureListener(e -> {
                    deletedAll.setValue(null); });
    }

    public void listenChatsMessages(Chat chat) {
        messagesRepository.listenChatsMessages(chat)
                .addOnSuccessListener(messages -> {
                    listener.setValue(messages); })
                .addOnFailureListener(e -> {
                    listener.setValue(null); });
    }
}
