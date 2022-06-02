package com.homeaide.post.Booking.booking;

public class Users {


    String userId;
    String firstName;
    String lastName;
    String contactNum;
    String email;
    String password;
    String imageName;
    String imageUrl;
    String ratings;

    public Users(){}

    public Users(String userId, String firstName, String lastName, String contactNum, String email, String password, String imageName, String imageUrl, String ratings) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.contactNum = contactNum;
        this.email = email;
        this.password = password;
        this.imageName = imageName;
        this.imageUrl = imageUrl;
        this.ratings = ratings;
    }

    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getContactNum() {
        return contactNum;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public String getUserId() {
        return userId;
    }
    public String getImageName() {
        return imageName;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public String getRatings() {
        return ratings;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setContactNum(String contactNum) {
        this.contactNum = contactNum;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public void setRatings(String ratings) {
        this.ratings = ratings;
    }

}
