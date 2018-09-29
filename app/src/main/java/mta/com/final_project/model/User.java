package mta.com.final_project.model;

/**
 * Created by rgerman on 4/27/2018.
 */

public class User {

    private String id;
    private String email;
    private String photoUrl;
    private String username;
    private String phoneNumber;


    public User(){

    }

    public User(String id, String email, String photoUrl, String username) {
        this.username = username;
        this.email = email;
        this.photoUrl = photoUrl;
        this.id = id;

    }

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

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}


