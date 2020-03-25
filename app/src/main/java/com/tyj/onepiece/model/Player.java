package com.tyj.onepiece.model;

public class Player {
    public int id;
    public String nick;
    public String userId;
    public String icon;
    public String roomId;
    public int roleType;
    public int score;
    public int state;
    public String creatTime;
    public String modTime;

    public void setId(int id) {
        this.id = id;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public void setRoleType(int roleType) {
        this.roleType = roleType;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setCreatTime(String creatTime) {
        this.creatTime = creatTime;
    }

    public void setModTime(String modTime) {
        this.modTime = modTime;
    }

    public int getId() {
        return id;
    }

    public String getNick() {
        return nick;
    }

    public String getUserId() {
        return userId;
    }

    public String getIcon() {
        return icon;
    }

    public String getRoomId() {
        return roomId;
    }

    public int getRoleType() {
        return roleType;
    }

    public int getScore() {
        return score;
    }

    public int getState() {
        return state;
    }

    public String getCreatTime() {
        return creatTime;
    }

    public String getModTime() {
        return modTime;
    }
}
