package com.krescendos.model;

import com.google.gson.annotations.SerializedName;

public class Profile {

    /*
        There's heaps of other fields you can get, but I don't intend to use them
     */

    @SerializedName("id")
    private String userID;

    @SerializedName("product")
    private String productType;

    public boolean isPremiumUser() {
        return productType.equals("premium");
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }
}
