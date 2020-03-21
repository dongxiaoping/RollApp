package com.tyj.onepiece.model;

public class Room {
    public String id;
    public int creatUserId;
    public int memberLimit;
    public int playCount;
    public int roomState;
    public float roomFee;
    public int roomPay;
    public int playMode;
    public int costLimit;
    public int oningRaceNum;
    public String creatTime;
    public  String modTime;

    public void setId(String id) {
        this.id = id;
    }

    public void setCreatUserId(int creatUserId) {
        this.creatUserId = creatUserId;
    }

    public void setMemberLimit(int memberLimit) {
        this.memberLimit = memberLimit;
    }

    public void setPlayCount(int playCount) {
        this.playCount = playCount;
    }

    public void setRoomState(int roomState) {
        this.roomState = roomState;
    }

    public void setRoomFee(float roomFee) {
        this.roomFee = roomFee;
    }

    public void setRoomPay(int roomPay) {
        this.roomPay = roomPay;
    }

    public void setPlayMode(int playMode) {
        this.playMode = playMode;
    }

    public void setCostLimit(int costLimit) {
        this.costLimit = costLimit;
    }

    public void setOningRaceNum(int oningRaceNum) {
        this.oningRaceNum = oningRaceNum;
    }

    public void setCreatTime(String creatTime) {
        this.creatTime = creatTime;
    }

    public void setModTime(String modTime) {
        this.modTime = modTime;
    }

    public String getId() {
        return id;
    }

    public int getCreatUserId() {
        return creatUserId;
    }

    public int getMemberLimit() {
        return memberLimit;
    }

    public int getPlayCount() {
        return playCount;
    }

    public int getRoomState() {
        return roomState;
    }

    public int getRoomPay() {
        return roomPay;
    }

    public int getPlayMode() {
        return playMode;
    }

    public int getCostLimit() {
        return costLimit;
    }

    public int getOningRaceNum() {
        return oningRaceNum;
    }

    public String getCreatTime() {
        return creatTime;
    }

    public String getModTime() {
        return modTime;
    }

    public float getRoomFee() {
        return roomFee;
    }

}
