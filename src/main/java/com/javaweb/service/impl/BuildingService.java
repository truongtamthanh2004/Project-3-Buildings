package com.javaweb.service.impl;

import com.javaweb.converter.AssignmentBuildingConverter;
import com.javaweb.converter.BuildingConverter;
import com.javaweb.entity.AssignmentBuildingEntity;
import com.javaweb.entity.BuildingEntity;
import com.javaweb.entity.RentAreaEntity;
import com.javaweb.enums.districtCode;
import com.javaweb.model.dto.AssignmentBuildingDTO;
import com.javaweb.model.dto.BuildingDTO;
import com.javaweb.model.request.BuildingSearchRequest;
import com.javaweb.model.response.BuildingSearchResponse;
import com.javaweb.model.response.ResponseDTO;
import com.javaweb.model.response.StaffResponseDTO;
import com.javaweb.repository.AssignmentBuildingRepository;
import com.javaweb.repository.BuildingRepository;
import com.javaweb.repository.RentAreaRepository;
import com.javaweb.service.IBuildingService;
import com.javaweb.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.ws.Action;
import java.util.*;

@Service
@Transactional
public class BuildingService implements IBuildingService {
    @Autowired
    private BuildingRepository buildingRepository;

    @Autowired
    private BuildingConverter buildingConverter;

    @Autowired
    private AssignmentBuildingConverter assignmentBuildingConverter;

    @Autowired
    private RentAreaRepository rentAreaRepository;

    @Autowired
    private AssignmentBuildingRepository assignmentBuildingRepository;

    @Autowired
    private IUserService iUserService;

    @Override
    public List<BuildingSearchResponse> findAllBuildingSearch(BuildingSearchRequest buildingSearchRequest) {
        List<BuildingEntity> buildingEntities = buildingRepository.findAllBuildingSearch(buildingSearchRequest);
        List<BuildingSearchResponse> buildingSearchResponses = new ArrayList<>();

        for (BuildingEntity buildingEntity : buildingEntities) {
            BuildingSearchResponse buildingSearchResponse = new BuildingSearchResponse();
            buildingSearchResponse.setName(buildingEntity.getName());
            buildingSearchResponse.setId(buildingEntity.getId());
            String districtName = "";
            try {
                districtName = districtCode.fromString(buildingEntity.getDistrict()).getDistrictName();
            } catch (IllegalArgumentException e) {
                // Handle the case where the district is not found in the enum
                districtName = buildingEntity.getDistrict();
            }
            buildingSearchResponse.setAddress(buildingEntity.getStreet() + ", " + buildingEntity.getWard() + ", " + districtName);
            buildingSearchResponse.setNumberOfBasement(buildingEntity.getNumberOfBasement());
            buildingSearchResponse.setManagerName(buildingEntity.getManagerName());
            buildingSearchResponse.setManagerPhone(buildingEntity.getManagerPhone());
            buildingSearchResponse.setFloorArea(buildingEntity.getFloorArea());
            String areas = "";
            for (RentAreaEntity rentAreaEntity : buildingEntity.getRentAreas()) {
                areas += rentAreaEntity.getValue() + ", ";
            }
            if (!areas.isEmpty()) {
                areas = areas.substring(0, areas.length() - 2);
            }

            buildingSearchResponse.setRentArea(areas);
            buildingSearchResponse.setEmptyArea("");
            buildingSearchResponse.setRentPrice(buildingEntity.getRentPrice());
            buildingSearchResponse.setServiceFee(buildingEntity.getServiceFee());
            buildingSearchResponse.setBrokerageFee(buildingEntity.getBrokerageFee());
            buildingSearchResponses.add(buildingSearchResponse);
        }

        return buildingSearchResponses;
    }

    @Override
    public void addBuilding(BuildingDTO buildingDTO) {
        BuildingEntity buildingEntity = buildingConverter.convertToBuildingEntity(buildingDTO);
        List<RentAreaEntity> rentAreaEntities = buildingEntity.getRentAreas();

        buildingRepository.save(buildingEntity);

        for(RentAreaEntity rentAreaEntity : rentAreaEntities) {
            rentAreaRepository.save(rentAreaEntity);
        }

    }

    @Override
    public void updateBuilding(BuildingDTO buildingDTO) {
        // Tìm tòa nhà hiện tại từ database bằng id
        Optional<BuildingEntity> optionalBuilding = buildingRepository.findById(buildingDTO.getId());
        if (optionalBuilding.isPresent()) {
            BuildingEntity existingBuilding = optionalBuilding.get();

            // Cập nhật các thuộc tính từ DTO sang Entity
            existingBuilding = buildingConverter.convertToBuildingEntity(buildingDTO);
            rentAreaRepository.deleteAllByBuilding(existingBuilding);
            rentAreaRepository.saveAll(existingBuilding.getRentAreas());
            buildingRepository.save(existingBuilding);
        } else {
            throw new RuntimeException("Building not found with id: " + buildingDTO.getId());
        }
    }

    @Override
    public BuildingDTO findById(Long id) {
        BuildingEntity buildingEntity = buildingRepository.findById(id).get();
        BuildingDTO buildingDTO = buildingConverter.convertToBuildingDTO(buildingEntity);
        return buildingDTO;
    }

    @Override
    public void deleteBuildingById(List<Long> ids) {
        for (Long id : ids) {
            BuildingEntity buildingEntity = buildingRepository.findById(id).get();
            rentAreaRepository.deleteAllByBuilding(buildingEntity);
            assignmentBuildingRepository.deleteAllByBuilding(buildingEntity);
        }
        buildingRepository.deleteByIdIn(ids);
    }

    @Override
    public ResponseDTO getStaffs(Long id) {
        // id, fullName
        Map<Long, String> staffs = iUserService.getStaffs(); // All
        List<AssignmentBuildingEntity> assignmentBuildingEntities = assignmentBuildingRepository.findByBuildingId(id); // assigned
        List<Long> assignedStaffIds = new ArrayList<>();

        for (AssignmentBuildingEntity assignmentBuildingEntity : assignmentBuildingEntities) {
            assignedStaffIds.add(assignmentBuildingEntity.getStaff().getId()); // id assigned
        }

        List<StaffResponseDTO> staffAssignment = new ArrayList<>();

        for (Map.Entry<Long, String> entry : staffs.entrySet()) {
            StaffResponseDTO staffResponseDTO = new StaffResponseDTO();
            staffResponseDTO.setStaffId(entry.getKey());
            staffResponseDTO.setFullName(entry.getValue());

            if (assignedStaffIds.contains(entry.getKey())) {
                staffResponseDTO.setChecked("checked");
            }
            else {
                staffResponseDTO.setChecked("");
            }

            staffAssignment.add(staffResponseDTO);
        }

        ResponseDTO result = new ResponseDTO();
        result.setData(staffAssignment);
        result.setMessage("Success");

        return result;
    }

    @Override
    public void updateAssignmentBuilding(AssignmentBuildingDTO assignmentBuildingDTO) {
        BuildingEntity buildingEntity = buildingRepository.findById(assignmentBuildingDTO.getBuildingId()).get();

        assignmentBuildingRepository.deleteAllByBuilding(buildingEntity);

        List<AssignmentBuildingEntity> assignmentBuildingEntities = assignmentBuildingConverter.convertToEntity(assignmentBuildingDTO);
        for (AssignmentBuildingEntity assignmentBuildingEntity : assignmentBuildingEntities) {
            assignmentBuildingRepository.save(assignmentBuildingEntity);
        }
    }
}
