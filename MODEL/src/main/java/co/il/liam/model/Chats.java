package co.il.liam.model;

public class Chats extends BaseList<Chat, Chats>{
    private String userId;

    public Chats() {}


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    //add method to order this list by date and time ascending
}
