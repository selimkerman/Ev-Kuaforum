package com.abaskan.evkuaforum.BarberClass;

public class Barber {
    private String barberName;
    private String barberStoreName;
    private String barberProvince;
    private String barberPhone;
    private String barberMail;
    private String barberType;
    private String barberDistrict;
    private String barberAdress;
    private String barberImageUrl;
    private long barberSignupDate;
    private double barberPoint;
    private double barberPrice;
    private double barberAdressLat;
    private double barberAdressLng;
    private long commentCount;

    public Barber() {
    }

    public Barber(String barberName, String barberStoreName, String barberProvince, String barberPhone, String barberMail, String barberType, String barberDistrict, String barberAdress, String barberImageUrl, long barberSignupDate, double barberPoint, double barberPrice, double barberAdressLat, double barberAdressLng, long commentCount) {
        this.barberName = barberName;
        this.barberStoreName = barberStoreName;
        this.barberProvince = barberProvince;
        this.barberPhone = barberPhone;
        this.barberMail = barberMail;
        this.barberType = barberType;
        this.barberDistrict = barberDistrict;
        this.barberAdress = barberAdress;
        this.barberImageUrl = barberImageUrl;
        this.barberSignupDate = barberSignupDate;
        this.barberPoint = barberPoint;
        this.barberPrice = barberPrice;
        this.barberAdressLat = barberAdressLat;
        this.barberAdressLng = barberAdressLng;
        this.commentCount = commentCount;
    }

    public String getBarberName() {
        return barberName;
    }

    public void setBarberName(String barberName) {
        this.barberName = barberName;
    }

    public String getBarberStoreName() {
        return barberStoreName;
    }

    public void setBarberStoreName(String barberStoreName) {
        this.barberStoreName = barberStoreName;
    }

    public String getBarberProvince() {
        return barberProvince;
    }

    public void setBarberProvince(String barberProvince) {
        this.barberProvince = barberProvince;
    }

    public String getBarberPhone() {
        return barberPhone;
    }

    public void setBarberPhone(String barberPhone) {
        this.barberPhone = barberPhone;
    }

    public String getBarberMail() {
        return barberMail;
    }

    public void setBarberMail(String barberMail) {
        this.barberMail = barberMail;
    }

    public String getBarberType() {
        return barberType;
    }

    public void setBarberType(String barberType) {
        this.barberType = barberType;
    }

    public String getBarberDistrict() {
        return barberDistrict;
    }

    public void setBarberDistrict(String barberDistrict) {
        this.barberDistrict = barberDistrict;
    }

    public String getBarberAdress() {
        return barberAdress;
    }

    public void setBarberAdress(String barberAdress) {
        this.barberAdress = barberAdress;
    }

    public String getBarberImageUrl() {
        return barberImageUrl;
    }

    public void setBarberImageUrl(String barberImageUrl) {
        this.barberImageUrl = barberImageUrl;
    }

    public long getBarberSignupDate() {
        return barberSignupDate;
    }

    public void setBarberSignupDate(long barberSignupDate) {
        this.barberSignupDate = barberSignupDate;
    }

    public double getBarberPoint() {
        return barberPoint;
    }

    public void setBarberPoint(double barberPoint) {
        this.barberPoint = barberPoint;
    }

    public double getBarberPrice() {
        return barberPrice;
    }

    public void setBarberPrice(double barberPrice) {
        this.barberPrice = barberPrice;
    }

    public double getBarberAdressLat() {
        return barberAdressLat;
    }

    public void setBarberAdressLat(double barberAdressLat) {
        this.barberAdressLat = barberAdressLat;
    }

    public double getBarberAdressLng() {
        return barberAdressLng;
    }

    public void setBarberAdressLng(double barberAdressLng) {
        this.barberAdressLng = barberAdressLng;
    }

    public long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(long commentCount) {
        this.commentCount = commentCount;
    }
}
