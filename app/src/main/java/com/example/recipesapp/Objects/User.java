package com.example.recipesapp.Objects;

import com.example.recipesapp.Constants.Keys;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class User {

    private String uid;
    private String img;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private HashMap<String ,String > groupsIds = new HashMap<>();
//    private ArrayList<String> groupsIds;


    public User() {}

    public User(String uid, String firstName, String lastName, String phoneNumber) {
        this.uid = uid;
        this.img = "https://images.immediate.co.uk/production/volatile/sites/30/2020/08/chorizo-mozarella-gnocchi-bake-cropped-9ab73a3.jpg?quality=90&resize=556,505";
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.groupsIds = new HashMap<>();
//        this.groupsIds = new ArrayList<>();
    }

    public String getUid() {
        return uid;
    }

//    public User setUid(String uid) {
//        this.uid = uid;
//        return this;
//    }

    public String getImg() {
        return img;
    }

    public User setImg(String img) {
        this.img = img;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public User setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public User setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public User setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public HashMap<String, String> getGroupsIds() {
        return groupsIds;
    }

    public User setGroupsIds(HashMap<String, String> groupsIds) {
        this.groupsIds = groupsIds;
        return this;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", img='" + img + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", groupsIds=" + groupsIds +
                '}';
    }

    //    public ArrayList<String> getGroupsIds() {
//        return groupsIds;
//    }
//
//    public User setGroupsIds(ArrayList<String> groupsIds) {
//        this.groupsIds = groupsIds;
//        return this;
//    }

    //Marks a field as excluded from the Database.
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> mapRes = new HashMap<>();
        mapRes.put(Keys.KEY_USER_ID, uid);
        mapRes.put(Keys.KEY_USER_IMG, img);
        mapRes.put(Keys.KEY_USER_FIRST_NAME, firstName);
        mapRes.put(Keys.KEY_USER_LAST_NAME, lastName);
        mapRes.put(Keys.KEY_USER_PHONE, phoneNumber);
        mapRes.put(Keys.KEY_USER_GROUPS_IDS, groupsIds);
        return mapRes;
    }
}