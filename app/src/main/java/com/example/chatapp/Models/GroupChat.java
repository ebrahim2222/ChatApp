package com.example.chatapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class GroupChat implements Parcelable {
    private String groupName;
    private String groupKey;

    public GroupChat() {
    }

    public GroupChat(String groupName) {
        this.groupName = groupName;
    }

    protected GroupChat(Parcel in) {
        groupName = in.readString();
    }

    public GroupChat(String groupName, String groupKey) {
        this.groupName = groupName;
        this.groupKey = groupKey;
    }

    public static final Creator<GroupChat> CREATOR = new Creator<GroupChat>() {
        @Override
        public GroupChat createFromParcel(Parcel in) {
            return new GroupChat(in);
        }

        @Override
        public GroupChat[] newArray(int size) {
            return new GroupChat[size];
        }
    };

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupKey() {
        return groupKey;
    }

    public void setGroupKey(String groupKey) {
        this.groupKey = groupKey;
    }

    public static Creator<GroupChat> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(groupName);
    }
}
