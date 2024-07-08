package com.javaweb.enums;

import java.util.LinkedHashMap;
import java.util.Map;

public enum districtCode {
    QUAN_1("Quận 1"),
    QUAN_2("Quận 2"),
    QUAN_3("Quận 3"),
    QUAN_TB("Quận Tân Bình"),
    QUAN_10("Quận 10"),
    QUAN_11("Quận 11"),
    QUAN_4("Quận 4");

    private final String districtName;

    districtCode(String districtName) {
        this.districtName = districtName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public static Map<String, String> district() {
        Map<String, String> listDistrict = new LinkedHashMap<>();
        for(districtCode item : districtCode.values()) {
            listDistrict.put(item.toString(), item.getDistrictName());
        }
        return listDistrict;
    }

    public static districtCode fromString(String district) {
        for (districtCode code : districtCode.values()) {
            if (code.name().equals(district)) {
                return code;
            }
        }
        throw new IllegalArgumentException("No enum constant " + districtCode.class.getCanonicalName() + "." + district);
    }
}
