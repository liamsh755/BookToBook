package co.il.liam.model;

import java.io.Serializable;
import java.util.Objects;

public class User extends BaseEntity implements Serializable {
    private String username;
    private String email;
    private String cityId;
    private String password;
    private Library library;

    public User(String username, String email, String cityId, String password, Library library) {
        this.username = username;
        this.email = email;
        this.cityId = cityId;
        this.password = password;
        this.library = library;
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

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Library getLibrary() {
        return library;
    }

    public void setLibrary(Library library) {
        this.library = library;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) && Objects.equals(email, user.email) && Objects.equals(cityId, user.cityId) && Objects.equals(password, user.password) && Objects.equals(library, user.library);
    }

}
