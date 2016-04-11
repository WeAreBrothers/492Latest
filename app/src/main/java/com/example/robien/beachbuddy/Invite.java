package com.example.robien.beachbuddy;

/**
 * Created by Robien on 4/2/2016.
 */
public class Invite {

    private String classType, classID;

    public Invite(String classType, String classID) {
        this.setClassType(classType);
        this.setClassID(classID);
    }

    public String getClassInvite() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public String getClassID() {
        return classID;
    }

    public void setClassID(String classID) {
        this.classID = classID;
    }
}
