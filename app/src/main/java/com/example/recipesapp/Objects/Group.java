package com.example.recipesapp.Objects;

import com.example.recipesapp.Constants.Keys;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Group {

    private String idGroup;
    private String name;
    private String description;
    private String imgGroup;
    private String groupCreator;                    //who create the group
    private HashMap<String,Boolean> recipesIds ;
    private HashMap<String,Boolean> usersIds ;       //list of all the users that the group share with them

    public Group() {
    }

    public Group(String name, String description, String groupCreator) {
        this.idGroup = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.imgGroup = "https://firebasestorage.googleapis.com/v0/b/grandma-s-secrets.appspot.com/o/userPng.png?alt=media&token=3fa265c9-66f4-42ed-81d5-390177206207"; // TODO: 29/06/2022 change to url from fire base storage
        this.recipesIds = new HashMap<>();
        this.groupCreator = groupCreator;
        this.usersIds = new HashMap<>();
        this.usersIds.put(groupCreator,true);
    }

    public String getIdGroup() {
        return idGroup;
    }

    public String getName() {
        return name;
    }

    public Group setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Group setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getImgGroup() {
        return imgGroup;
    }

    public Group setImgGroup(String imgGroup) {
        this.imgGroup = imgGroup;
        return this;
    }
    public String getGroupCreator() {
        return groupCreator;
    }

    public Group setGroupCreator(String groupCreator) {
        this.groupCreator = groupCreator;
        return this;
    }

    public HashMap<String, Boolean> getRecipesIds() {
        return recipesIds;
    }

    public Group setRecipesIds(HashMap<String, Boolean> recipesIds) {
        this.recipesIds = recipesIds;
        return this;
    }

    public HashMap<String, Boolean> getUsersIds() {
        return usersIds;
    }

    public Group setUsersIds(HashMap<String, Boolean> usersIds) {
        this.usersIds = usersIds;
        return this;
    }

    @Override
    public String toString() {
        return "Group{" +
                "idGroup='" + idGroup + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", imgGroup='" + imgGroup + '\'' +
                ", groupCreator='" + groupCreator + '\'' +
                ", recipesIds=" + recipesIds +
                ", usersIds=" + usersIds +
                '}';
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> mapRes = new HashMap<>();
        mapRes.put(Keys.KEY_GROUP_ID, idGroup);
        mapRes.put(Keys.KEY_GROUP_IMG, imgGroup);
        mapRes.put(Keys.KEY_GROUP_NAME, name);
        mapRes.put(Keys.KEY_GROUP_DESCRIPTION, description);
        mapRes.put(Keys.KEY_GROUP_CREATOR, groupCreator);
        mapRes.put(Keys.KEY_GROUP_RECIPES_LIST, recipesIds);
        mapRes.put(Keys.KEY_GROUP_USERS_LIST, usersIds);
        return mapRes;
    }
}
