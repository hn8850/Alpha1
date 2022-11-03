package com.example.alpha1;


public class Upload {
    private String name;
    private String picURL;

    public Upload() {
        // Empty Constructor
    }

    public Upload(String pName, String pPicURL) {
        if (pName.trim().equals("")) pName = "No Name";
        name = pName;
        picURL = pPicURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicURL() {
        return picURL;
    }

    public void setPicURL(String picURL) {
        this.picURL = picURL;
    }
}
