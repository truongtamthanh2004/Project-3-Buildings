package com.javaweb.converter;

import com.javaweb.entity.BuildingEntity;
import com.javaweb.entity.RentAreaEntity;
import com.javaweb.model.dto.BuildingDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class BuildingConverter {
    @Autowired
    private ModelMapper molMapper;

    String rentAreasToString(List<RentAreaEntity> rentAreas) {
        if (rentAreas == null || rentAreas.isEmpty()) {
            return "";
        }

        List<String> values = new ArrayList<>();
        for (RentAreaEntity rentArea : rentAreas) {
            // Ensure rentArea and its value are not null
            if (rentArea != null && rentArea.getValue() != null) {
                values.add(rentArea.getValue().toString());
            }
        }

        // Delete the redundant , at the end of the string
        String result = String.join(", ", values);
        if (!result.isEmpty() && result.endsWith(",")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    public BuildingEntity convertToBuildingEntity(BuildingDTO buildingDTO) {
        BuildingEntity buildingEntity =  molMapper.map(buildingDTO, BuildingEntity.class);

        buildingEntity.setType(String.join(",", buildingDTO.getTypeCode()));

        if(buildingDTO.getRentArea().equals("")) {
            return buildingEntity;
        }

        List<Long> rentAreas = new ArrayList<>();

        String[] rentAreasString = buildingDTO.getRentArea().split(",");

        for (String area : rentAreasString) {
            rentAreas.add(Long.parseLong(area.trim())); // trim() to ignore leading and ending white space
        }

        List<RentAreaEntity> rentAreaEntities = new ArrayList<>();

        for (Long area : rentAreas)
        {
            RentAreaEntity rentAreaEntity = new RentAreaEntity();
            rentAreaEntity.setValue(area);
            rentAreaEntity.setBuilding(buildingEntity);
            rentAreaEntities.add(rentAreaEntity);
        }

        buildingEntity.setRentAreas(rentAreaEntities);

        return buildingEntity;
    }

    public BuildingDTO convertToBuildingDTO(BuildingEntity buildingEntity) {
        // Ensure buildingEntity is not null
        if (buildingEntity == null) {
            throw new IllegalArgumentException("buildingEntity cannot be null");
        }

        BuildingDTO buildingDTO = molMapper.map(buildingEntity, BuildingDTO.class);

        // Ensure getType() is not null
        if (buildingEntity.getType() != null) {
            List<String> typeCode = Arrays.asList(buildingEntity.getType().split(","));
            buildingDTO.setTypeCode(typeCode);
        } else {
            buildingDTO.setTypeCode(Collections.emptyList());
        }

        // Ensure getRentAreas() is not null
        buildingDTO.setRentArea(rentAreasToString(buildingEntity.getRentAreas() != null ? buildingEntity.getRentAreas() : Collections.emptyList()));

        return buildingDTO;
    }
}
