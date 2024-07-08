package com.javaweb.enums;

import java.util.LinkedHashMap;
import java.util.Map;

public enum typeCode {
    TANG_TRET("Tầng Trệt"),
    NGUYEN_CAN("Nguyên Căn"),
    NOI_THAT("Nội Thất");

    private final String typeCodeName;

    typeCode(String typeCodeName) {
        this.typeCodeName = typeCodeName;
    }

    public String getTypeCodeName() {
        return typeCodeName;
    }

    public static Map<String, String> getTypeCode() {
        Map<String, String> listDistrict = new LinkedHashMap<>();
        for(typeCode item : typeCode.values()) {
            listDistrict.put(item.toString(), item.getTypeCodeName());
        }
        return listDistrict;
    }
}
