package com.abaskan.evkuaforum.UserClass;

public class User {
    private String userName;
    private String userPhone;
    private String userMail;
    private String userProvince;
    private String userDistrict;
    private String userAdress;
    private String userGender;
    private double userAdressLat;
    private double userAdressLng;

    public User() {
    }

    public User(String userName, String userPhone, String userMail, String userProvince, String userDistrict, String userAdress, String userGender, double userAdressLat, double userAdressLng) {
        this.userName = userName;
        this.userPhone = userPhone;
        this.userMail = userMail;
        this.userProvince = userProvince;
        this.userDistrict = userDistrict;
        this.userAdress = userAdress;
        this.userGender = userGender;
        this.userAdressLat = userAdressLat;
        this.userAdressLng = userAdressLng;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public String getUserProvince() {
        return userProvince;
    }

    public void setUserProvince(String userProvince) {
        this.userProvince = userProvince;
    }

    public String getUserDistrict() {
        return userDistrict;
    }

    public void setUserDistrict(String userDistrict) {
        this.userDistrict = userDistrict;
    }

    public String getUserAdress() {
        return userAdress;
    }

    public void setUserAdress(String userAdress) {
        this.userAdress = userAdress;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public double getUserAdressLat() {
        return userAdressLat;
    }

    public void setUserAdressLat(double userAdressLat) {
        this.userAdressLat = userAdressLat;
    }

    public double getUserAdressLng() {
        return userAdressLng;
    }

    public void setUserAdressLng(double userAdressLng) {
        this.userAdressLng = userAdressLng;
    }
}
