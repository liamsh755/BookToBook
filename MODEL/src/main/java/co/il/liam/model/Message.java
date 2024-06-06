package co.il.liam.model;

public class Message extends BaseEntity{
    private User sender;
    private User recipient;
    private String content;
    private String date;
    private String time;
    private int seconds;

    public Message() {}
    public Message(User sender, User recipient, String content, String time, int seconds, String date) {
        this.sender = sender;
        this.recipient = recipient;
        this.content = content;
        this.date = date;
        this.seconds = seconds;
        this.time = time;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }
}
