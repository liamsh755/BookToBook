package co.il.liam.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import co.il.liam.model.Chat;
import co.il.liam.model.Chats;
import co.il.liam.model.Message;
import co.il.liam.model.User;
import co.il.liam.repository.ChatsRepository;

public class ChatsViewModel extends AndroidViewModel {
    private ChatsRepository chatsRepository;

    private MutableLiveData<Boolean> addedChat;
    private MutableLiveData<Chats> foundChats;
    private MutableLiveData<Boolean> deletedChat;
    private MutableLiveData<Boolean> updatedLastMessage;

    public ChatsViewModel(@NonNull Application application) {
        super(application);

        chatsRepository = new ChatsRepository(application);
        addedChat = new MutableLiveData<>();
        foundChats = new MutableLiveData<>();
        deletedChat = new MutableLiveData<>();
        updatedLastMessage = new MutableLiveData<>();
    }

    public MutableLiveData<Boolean> getAddedChat() {
        return addedChat;
    }
    public MutableLiveData<Chats> getFoundChats() {
        return foundChats;
    }
    public MutableLiveData<Boolean> getDeletedChat() {
        return deletedChat;
    }
    public MutableLiveData<Boolean> getUpdatedLastMessage() {
        return updatedLastMessage;
    }

    public void addChat(Chat chat) {
        chatsRepository.addChat(chat)
                .addOnSuccessListener(aBoolean -> {
                    addedChat.setValue(aBoolean); })
                .addOnFailureListener(aBoolean -> {
                    addedChat.setValue(false); });
    }

    public void getChats(User user) {
        chatsRepository.getChats(user)
                .addOnSuccessListener(chats -> {
                    foundChats.setValue(chats); })
                .addOnFailureListener(e -> {
                    foundChats.setValue(null); });
    }

    public void deleteChat(Chat chat) {
        chatsRepository.deleteChat(chat)
                .addOnSuccessListener(aBoolean -> {
                    deletedChat.setValue(aBoolean); })
                .addOnFailureListener(aBoolean -> {
                    deletedChat.setValue(false); });
    }

    public void updateLastMessage(Chat chat, Message message) {
        chatsRepository.updateLastMessage(chat, message)
                .addOnSuccessListener(aBoolean -> {
                    updatedLastMessage.setValue(aBoolean); })
                .addOnFailureListener(aBoolean -> {
                    updatedLastMessage.setValue(false); });
    }
}
