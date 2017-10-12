package com.abyx.noticer;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Locale;

/**
 * This class represents an object that's used to store information that has something to do
 * with money and currency formats.
 */
public class Currency implements Parcelable {
    private double price;
    private double decimals;

    public Currency(double price, int decimals){
        this.price = price;
        this.decimals = decimals;
    }

    public Currency(double price){
        this.price = price;
        this.decimals = 2;
    }

    public Currency(Parcel in){
        this.price = Double.parseDouble(in.readString());
    }

    public double getPrice(){
        return price;
    }

    public void setPrice(double price){
        this.price = price;
    }

    public void add(Currency add){
        this.price += add.getPrice();
    }

    @Override
    public String toString(){
        java.util.Currency currency = java.util.Currency.getInstance(Locale.getDefault());
        return currency.getSymbol() + String.format("%.2f", price);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(String.valueOf(price));
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Content createFromParcel(Parcel in) {
            return new Content(in);
        }
        public Content[] newArray(int size) {
            return new Content[size];
        }
    };
}
