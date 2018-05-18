package com.example.henry.couponer_x;

public class Information {

    private String storeName;
    private String expirationDate;
    private String couponNumber;

    Information(String storeName, String expirationDate, String couponNumber){
        this.storeName = storeName;
        this.expirationDate = expirationDate;
        this.couponNumber = couponNumber;
    };

    public String getStoreName(){ return this.storeName; }
    public String getExpirationDate(){ return this.expirationDate; }
    public String getCouponNumber(){ return this.couponNumber; }
}
