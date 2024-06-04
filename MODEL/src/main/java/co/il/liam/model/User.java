package co.il.liam.model;

import java.io.Serializable;
import java.util.Objects;

public class User extends BaseEntity implements Serializable {
    private String username;
    private String email;
    private String state;
    private String city;
    private String password;

    public User(String username, String email, String state, String city, String password) {
        this.username = username;
        this.email = email;
        this.state = state;
        this.city = city;
        this.password = password;
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username)
                && Objects.equals(email, user.email)
                && Objects.equals(state, user.state)
                && Objects.equals(city, user.city)
                && Objects.equals(password, user.password);
    }

}
