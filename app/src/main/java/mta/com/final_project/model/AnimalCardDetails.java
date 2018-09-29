package mta.com.final_project.model;

/**
 * Created by rgerman on 9/12/2018.
 */

public class AnimalCardDetails {

    private String itemId;
    private String title;
    private String location;
    private String foundOrLost;
    private String description;
    private String animalPhotoUrl;
    private String userPhotoUrl;
    private String userId;
    private String userName;
    private String timeAndDate;
    private boolean iHaveIt;

    public AnimalCardDetails() {}

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getTimeAndDate() {
        return timeAndDate;
    }

    public void setTimeAndDate(String timeAndDate) {
        this.timeAndDate = timeAndDate;
    }

    public String getUserPhotoUrl() {
        return userPhotoUrl;
    }

    public void setUserPhotoUrl(String userPhotoUrl) {
        this.userPhotoUrl = userPhotoUrl;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAnimalPhotoUrl() {
        return animalPhotoUrl;
    }

    public void setAnimalPhotoUrl(String animalPhotoUrl) {
        this.animalPhotoUrl = animalPhotoUrl;
    }

    public void setFoundOrLost(String foundOrLost) {
        this.foundOrLost = foundOrLost;
    }

    public String getFoundOrLost() {

        return foundOrLost;
    }

    public boolean isiHaveIt() {
        return iHaveIt;
    }

    public void setiHaveIt(boolean iHaveIt) {
        this.iHaveIt = iHaveIt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public String getUserId() {
        return userId;
    }
}
