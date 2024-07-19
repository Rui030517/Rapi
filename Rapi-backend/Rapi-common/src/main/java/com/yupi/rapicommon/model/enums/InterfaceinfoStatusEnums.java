package com.yupi.rapicommon.model.enums;

public enum InterfaceinfoStatusEnums {
    OFFLINE("下线状态",0),
    ONLINE("上线状态",1);

    private final String status;
    private final int va;
    private

    InterfaceinfoStatusEnums(String status, int va) {
        this.status = status;
        this.va = va;
    }

    public int getValue() {
        return va;
    }

    public String getStatus(){
        return status;
    }
}
