package com.example.robien.beachbuddy;

/**
 * Created by Robien on 4/8/2016.
 */
public class Group {
    private String groupType, groupID;

    public Group(String groupType, String groupID) {
        this.setGroupType(groupType);
        this.setGroupID(groupID);
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }
}
