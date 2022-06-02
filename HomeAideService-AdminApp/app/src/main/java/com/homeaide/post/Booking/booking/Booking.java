package com.homeaide.post.Booking.booking;

public class Booking {

    String imageUrl;
    String custID;
    String projName;
    String custAddress;
    String latitude;
    String longitude;
    String propertyType;
    String unitType;
    String bookingDate;
    String bookingTime;
    String custContactNum;
    String addInfo;
    String totalPrice;
    String paymentMethod;
    String techID;
    String bookingCreated;

    public Booking(){

    }

    public Booking(String imageUrl, String custID, String projName, String custAddress, String latitude, String longitude, String propertyType, String unitType,
                   String bookingDate, String bookingTime, String custContactNum, String addInfo, String totalPrice, String paymentMethod,
                   String techID, String bookingCreated)
    {
        this.imageUrl = imageUrl;
        this.custID = custID;
        this.projName = projName;
        this.custAddress = custAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.propertyType = propertyType;
        this.unitType = unitType;
        this.bookingDate = bookingDate;
        this.bookingTime = bookingTime;
        this.custContactNum = custContactNum;
        this.addInfo = addInfo;
        this.totalPrice = totalPrice;
        this.paymentMethod = paymentMethod;
        this.techID = techID;
        this.bookingCreated = bookingCreated;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCustID() {
        return custID;
    }

    public void setCustID(String custID) {
        this.custID = custID;
    }

    public String getProjName() {
        return projName;
    }

    public void setProjName(String projName) {
        this.projName = projName;
    }

    public String getCustAddress() {
        return custAddress;
    }

    public void setCustAddress(String custAddress) {
        this.custAddress = custAddress;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(String bookingTime) {
        this.bookingTime = bookingTime;
    }

    public String getCustContactNum() {
        return custContactNum;
    }

    public void setCustContactNum(String custContactNum) {
        this.custContactNum = custContactNum;
    }

    public String getAddInfo() {
        return addInfo;
    }

    public void setAddInfo(String addInfo) {
        this.addInfo = addInfo;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getTechID() {
        return techID;
    }

    public void setTechID(String techID) {
        this.techID = techID;
    }

    public String getBookingCreated() {
        return bookingCreated;
    }

    public void setBookingCreated(String bookingCreated) {
        this.bookingCreated = bookingCreated;
    }
}

