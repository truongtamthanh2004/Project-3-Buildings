package com.javaweb.repository.custom.impl;

import com.javaweb.entity.BuildingEntity;
import com.javaweb.model.request.BuildingSearchRequest;
import com.javaweb.repository.custom.BuildingRepositoryCustom;
import com.javaweb.utils.NumberUtils;
import com.javaweb.utils.StringUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

@Repository
public class BuildingRepositoryImpl implements BuildingRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;
    public String join(BuildingSearchRequest buildingSearchRequest) {
        String sql = "";

        Long areaFrom = buildingSearchRequest.getAreaFrom();
        Long areaTo = buildingSearchRequest.getAreaTo();
        Long staffId = buildingSearchRequest.getStaffId();

        if (NumberUtils.isValid(areaFrom) || NumberUtils.isValid(areaTo)) {
            sql += " JOIN rentarea on rentarea.buildingid = building.id ";
        }
        if (NumberUtils.isValid(staffId)) {
            sql += " JOIN assignmentbuilding on assignmentbuilding.buildingid = building.id ";
        }

        return sql;
    }

//    public String whereCondition(BuildingSearchRequest buildingSearchRequest) {
//        String sql = "";
//
//        try {
//            Field[] fields = BuildingSearchRequest.class.getDeclaredFields();
//            for (Field item : fields) {
//                item.setAccessible(true);
//                String key = item.getName();
//                Object value = item.get(buildingSearchRequest).toString();
//
//                if (key.equals("floorArea") || key.equals("staffId") || key.equals("numberOfBasement") || key.equals("level")) {
//                    if (value != null) {
//                        sql += " AND " + key + " = " + value + " ";
//                    }
//                }
//
//                if (key.equals("areaFrom")) {
//                    if (value != null) {
//                        sql += " AND value >= " + value + " ";
//                    }
//                }
//
//                if (key.equals("areaTo")) {
//                    if (value != null) {
//                        sql += " AND value <= " + value + " ";
//                    }
//                }
//
//                if (key.equals("rentPriceFrom")) {
//                    if (value != null) {
//                        sql += " AND rentprice >= " + value + " ";
//                    }
//                }
//
//                if (key.equals("rentPriceTo")) {
//                    if (value != null) {
//                        sql += " AND rentprice <= " + value + " ";
//                    }
//                }
//
//                if (key.equals("name") || key.equals("district") || key.equals("ward") ||
//                        key.equals("street") || key.equals("direction") || key.equals("managerName") || key.equals("managerPhone")) {
//                    if (value != null) {
//                        String tmp = value.toString();
//                        if (!tmp.equals(""))
//                        {
//                            sql += " AND " + key + " like '%" + tmp + "%'";
//                        }
//                    }
//                }
//
//                if (key.equals("typeCode")) {
//                    if (value != null) {
//                        List<String> typeCodes = (List<String>) value;
//                        if (!typeCodes.isEmpty()) {
//                            sql += " AND type like '%";
//                            for (int i = 0; i < typeCodes.size(); i++) {
//                                sql += typeCodes.get(i);
//                                if (i < typeCodes.size() - 1) {
//                                    sql += ",";
//                                }
//                            }
//                            sql += "%' ";
//                        }
//                    }
//                }
//            }
//        }
//        catch(Exception ex) {
//            ex.printStackTrace();
//        }
//
//        return sql;
//    }

    public String whereCondition(BuildingSearchRequest buildingSearchRequest) {
        String sql = "";

        try {
            Field[] fields = BuildingSearchRequest.class.getDeclaredFields();
            for (Field item : fields) {
                item.setAccessible(true);
                String key = item.getName();
                Object value = item.get(buildingSearchRequest);

                // Kiểm tra null trước khi sử dụng value
                if (value != null) {
                    String valueStr = value.toString();

                    if (key.equals("floorArea") || key.equals("staffId") || key.equals("numberOfBasement") || key.equals("level")) {
                        sql += " AND " + key + " = " + valueStr + " ";
                    }

                    if (key.equals("areaFrom")) {
                        sql += " AND value >= " + valueStr + " ";
                    }

                    if (key.equals("areaTo")) {
                        sql += " AND value <= " + valueStr + " ";
                    }

                    if (key.equals("rentPriceFrom")) {
                        sql += " AND rentprice >= " + valueStr + " ";
                    }

                    if (key.equals("rentPriceTo")) {
                        sql += " AND rentprice <= " + valueStr + " ";
                    }

                    if (key.equals("name") || key.equals("district") || key.equals("ward") ||
                            key.equals("street") || key.equals("direction") || key.equals("managerName") || key.equals("managerPhone")) {
                        if (!valueStr.isEmpty()) {
                            sql += " AND " + key + " like '%" + valueStr + "%'";
                        }
                    }

                    if (key.equals("typeCode")) {
                        List<String> typeCodes = (List<String>) value;
                        if (!typeCodes.isEmpty()) {
                            sql += " AND type like '%";
                            for (int i = 0; i < typeCodes.size(); i++) {
                                sql += typeCodes.get(i);
                                if (i < typeCodes.size() - 1) {
                                    sql += ",";
                                }
                            }
                            sql += "%' ";
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return sql;
    }


    @Override
    public List<BuildingEntity> findAllBuildingSearch(BuildingSearchRequest buildingSearchRequest) {
        String sql = "SELECT distinct building.* FROM building ";
        sql += join(buildingSearchRequest);
        String where = " WHERE 1=1 ";
        where += whereCondition(buildingSearchRequest);
        sql += where;

        System.out.println(sql);

        Query query = entityManager.createNativeQuery(sql, BuildingEntity.class);

        return query.getResultList();
    }
}
