package com.javaweb.repository;

import com.javaweb.entity.BuildingEntity;
import com.javaweb.entity.RentAreaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentAreaRepository extends JpaRepository<RentAreaEntity, Long> {
    public void deleteAllByBuilding(BuildingEntity buildingEntity);
}
