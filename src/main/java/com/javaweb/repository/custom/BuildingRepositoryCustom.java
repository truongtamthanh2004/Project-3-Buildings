package com.javaweb.repository.custom;

import com.javaweb.entity.BuildingEntity;
import com.javaweb.model.request.BuildingSearchRequest;
import com.javaweb.model.response.BuildingSearchResponse;

import java.util.Collections;
import java.util.List;

public interface BuildingRepositoryCustom {
    public List<BuildingEntity> findAllBuildingSearch(BuildingSearchRequest buildingSearchRequest);
}
