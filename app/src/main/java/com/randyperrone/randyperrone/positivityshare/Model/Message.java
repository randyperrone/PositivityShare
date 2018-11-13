package com.randyperrone.randyperrone.positivityshare.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Message implements Parcelable{
    //Firebase instruction requires variables to be public for DataSnapshot.getValue(Message.class)
    public String userKey;
    public String date;
    public String message;
    public String dbKey;

    public Message(){
        //Firebase instruction requires blank constructor DataSnapshot.getValue(Message.class)
    }

    public Message(String userKey, String date, String message, String dbKey) {
        this.userKey = userKey;
        this.date = date;
        this.message = message;
        this.dbKey = dbKey;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDbKey() {
        return dbKey;
    }

    public void setDbKey(String dbKey) {
        this.dbKey = dbKey;
    }

    private Message(Parcel in){
        userKey = in.readString();
        date = in.readString();
        message = in.readString();
        dbKey = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userKey);
        parcel.writeString(date);
        parcel.writeString(message);
        parcel.writeString(dbKey);
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

}
