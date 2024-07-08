package com.javaweb.converter;

import com.javaweb.entity.AssignmentBuildingEntity;
import com.javaweb.entity.BuildingEntity;
import com.javaweb.entity.UserEntity;
import com.javaweb.model.dto.AssignmentBuildingDTO;
import com.javaweb.repository.BuildingRepository;
import com.javaweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AssignmentBuildingConverter {
    @Autowired
    private BuildingRepository buildingRepository;

    @Autowired
    private UserRepository userRepository;

    public List<AssignmentBuildingEntity> convertToEntity(AssignmentBuildingDTO assignmentBuildingDTO) {
        List<AssignmentBuildingEntity> assignmentBuildingEntities = new ArrayList<>();
        List<Long> staffs = assignmentBuildingDTO.getStaffs();
        Long buildingId = assignmentBuildingDTO.getBuildingId();
        BuildingEntity buildingEntity = buildingRepository.findById(buildingId).get();

        for (Long staff : staffs) {
            UserEntity userEntity = userRepository.findById(staff).get();
            AssignmentBuildingEntity assignmentBuildingEntity = new AssignmentBuildingEntity();
            assignmentBuildingEntity.setStaff(userEntity);
            assignmentBuildingEntity.setBuilding(buildingEntity);
            assignmentBuildingEntities.add(assignmentBuildingEntity);
        }

        return assignmentBuildingEntities;
    }
}
