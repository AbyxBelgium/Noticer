package com.abyx.noticer;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;
import java.util.Date;

/**
 * An object of this class represents
 */
public class Content implements Parcelable{
    private String occasion;
    private String location;
    private Currency expenses;
    private Calendar date;
    private int color;

    public Content(String occasion, String location, Currency expenses, Calendar date, int color){
        this.occasion = occasion;
        this.location = location;
        this.expenses = expenses;
        this.date = date;
        this.color = color;
    }

    public Content(Parcel in){
        this.occasion = in.readString();
        this.location = in.readString();
        this.expenses = new Currency(Double.parseDouble(in.readString()));
        Calendar temp = Calendar.getInstance();
        String out = in.readString();
        System.out.println(out);
        String[] dates = out.split("-");
        temp.set(Integer.parseInt(dates[0]), Integer.parseInt(dates[1]), Integer.parseInt(dates[2]));
        this.date = temp;
        this.color = Integer.parseInt(in.readString());
    }

    public String getOccasion() {
        return occasion;
    }

    public void setOccasion(String occasion) {
        this.occasion = occasion;
    }

    public Currency getExpenses() {
        return expenses;
    }

    public void setExpenses(Currency expenses) {
        this.expenses = expenses;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public int getColor(){
        return this.color;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    private String getDateString(){
        return date.get(Calendar.YEAR) + "-" + date.get(Calendar.MONTH) + "-" + date.get(Calendar.DAY_OF_MONTH);
    }

    public String getSaveRepresentation(){
        return occasion + "\t" + location + "\t" + expenses.getPrice() + "\t" + getDateString() + "\t" + color;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(occasion);
        parcel.writeString(location);
        parcel.writeString(String.valueOf(expenses.getPrice()));
        parcel.writeString(date.get(Calendar.YEAR) + "-" + date.get(Calendar.MONTH) + "-" + date.get(Calendar.DAY_OF_MONTH));
        parcel.writeString(String.valueOf(color));
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
