package com.randyperrone.randyperrone.positivityshare.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable{
    private String userKey;
    private String picUrl;
    private String username;
    private String age;
    private String about;

    public User(String userKey, String picUrl, String username, String age, String about) {
        this.userKey = userKey;
        this.picUrl = picUrl;
        this.username = username;
        this.age = age;
        this.about = about;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }


    private User(Parcel in){
        userKey = in.readString();
        username = in.readString();
        age = in.readString();
        about = in.readString();
        picUrl = in.readString();
    }
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userKey);
        parcel.writeString(username);
        parcel.writeString(age);
        parcel.writeString(about);
        parcel.writeString(picUrl);
    }
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>(){

        @Override
        public User createFromParcel(Parcel parcel) {
            return new User(parcel);
        }
        @Override
        public User[] newArray(int i) {
            return new User[i];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }


}
