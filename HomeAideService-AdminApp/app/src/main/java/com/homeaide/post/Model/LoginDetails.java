package com.homeaide.post.Model;

public class LoginDetails {

    public static String email;
    public static String password;

    public static String chatemail;
    public static String chatpassword;
    private static Boolean successPayment = false;

    public static String projName, projAddress, projPrice;

    public LoginDetails() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public LoginDetails(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static String getProjName() {
        return projName;
    }

    public static void setProjName(String projName) {
        LoginDetails.projName = projName;
    }

    public static String getProjAddress() {
        return projAddress;
    }

    public static void setProjAddress(String projAddress) {
        LoginDetails.projAddress = projAddress;
    }

    public static String getProjPrice() {
        return projPrice;
    }

    public static void setProjPrice(String projPrice) {
        LoginDetails.projPrice = projPrice;
    }

    public String getEmail(){
        return email;
    }

    public String getPassword(){
        return password;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getchatEmail(){
        return chatemail;
    }

    public String getchatPassword(){
        return chatpassword;
    }

    public void setchatEmail(String chatemail){
        this.chatemail = chatemail;
    }

    public void setchatPassword(String chatpassword){
        this.chatpassword = chatpassword;
    }

    public Boolean getSuccessPayment() {
        return successPayment;
    }

    public void setSuccessPayment(Boolean successPayment) {
        LoginDetails.successPayment = successPayment;
    }
}
