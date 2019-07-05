package com.lovecoin.extra.facebook.entity;

public class User {
    private FacebookPicture picture;
    private String name;
    private String id;
    private String email;
    private String gender;


    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getGender() {
        if ("male".equals(gender)) {
            return 1;
        } else if ("female".equals(gender)) {
            return 2;
        } else {
            return 0;
        }
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public static class FacebookPicture {
        public Data data;
    }

    public static class Data {
        public String url;
    }

    public String getPicture() {
        if (picture == null) {
            return "";
        }

        if (picture.data == null) {
            return "";
        }

        return picture.data.url;
    }

}