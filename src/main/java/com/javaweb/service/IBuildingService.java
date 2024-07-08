package com.javaweb.service;

import com.javaweb.model.dto.AssignmentBuildingDTO;
import com.javaweb.model.dto.BuildingDTO;
import com.javaweb.model.request.BuildingSearchRequest;
import com.javaweb.model.response.BuildingSearchResponse;
import com.javaweb.model.response.ResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface IBuildingService {
    public List<BuildingSearchResponse> findAllBuildingSearch(BuildingSearchRequest buildingSearchRequest);
    public void addBuilding(BuildingDTO buildingDTO);
    public void updateBuilding(BuildingDTO buildingDTO);
    public BuildingDTO findById(Long id);
    public void deleteBuildingById(List<Long> ids);
    public ResponseDTO getStaffs(Long id);
    public void updateAssignmentBuilding(AssignmentBuildingDTO assignmentBuildingDTO);
}
