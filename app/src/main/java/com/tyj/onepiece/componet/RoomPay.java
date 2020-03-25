package com.tyj.onepiece.componet;

public enum RoomPay {
    AA("AA制", 1), DAI_KAI("代开", 2);

    private String name;
    private String index;

    RoomPay(String name, int index) {
    }

    public String getName() {
        return name;
    }

    public String getIndex() {
        return index;
    }
}
