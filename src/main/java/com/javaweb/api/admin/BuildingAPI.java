package com.javaweb.api.admin;

import com.javaweb.entity.UserEntity;
import com.javaweb.model.dto.AssignmentBuildingDTO;
import com.javaweb.model.dto.BuildingDTO;
import com.javaweb.model.response.ResponseDTO;
import com.javaweb.model.response.StaffResponseDTO;
import com.javaweb.repository.UserRepository;
import com.javaweb.service.IBuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/buildings")
public class BuildingAPI {
    private final UserRepository userRepository;

    @Autowired
    IBuildingService buildingService;

    public BuildingAPI(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping
    public String addOrUpdateBuilding(@RequestBody BuildingDTO buildingDTO) {
        // Xuong DB lay data
        System.out.println(buildingDTO.getId());
        if (buildingDTO.getId() == null || buildingDTO.getId() == 0) {
            // Nếu không có ID, thực hiện thêm mới
            buildingService.addBuilding(buildingDTO);
            return "Add Building Success";
        } else {
            // Nếu có ID, thực hiện cập nhật
            buildingService.updateBuilding(buildingDTO);
            return "Update Building Success";
        }
    }

    @DeleteMapping
    public String deleteBuilding(@RequestBody List<Long> ids) {
        // Xuong DB xoa data
        buildingService.deleteBuildingById(ids);
        return new String("Delete Building Success");
    }

    @GetMapping("/{id}/staffs")
    public ResponseDTO loadStaff(@PathVariable("id") Long id) {
        // Lay tat ca nhan vien (co giao va chua duoc giao)
//        List<UserEntity> userEntities = userRepository.findByStatusAndRoles_Code(1, "STAFF");
        // Lay tat ca nhan vien quan ly cua toa nha co id gui ve List<UserEntity> userEntities
//        List<StaffResponseDTO> staffAssignment = new ArrayList<>();
//        StaffResponseDTO staff1 = new StaffResponseDTO();
//        staff1.setStaffId(1L);
//        staff1.setFullName("Nguyen Van A");
//        staff1.setChecked("checked");
//
//        StaffResponseDTO staff2 = new StaffResponseDTO();
//        staff2.setStaffId(2L);
//        staff2.setFullName("Luc Van T");
//        staff2.setChecked("");
//
//        StaffResponseDTO staff3 = new StaffResponseDTO();
//        staff3.setStaffId(4L);
//        staff3.setFullName("Pham Thi H");
//        staff3.setChecked("checked");
//
//        staffAssignment.add(staff1);
//        staffAssignment.add(staff2);
//        staffAssignment.add(staff3);
//
//        ResponseDTO result = new ResponseDTO();
//        result.setData(staffAssignment);
//        result.setMessage("Success");

        ResponseDTO result = buildingService.getStaffs(id);


        return result;
    }

    @PutMapping()
    public void updateAssignmentBuilding(@RequestBody AssignmentBuildingDTO assignmentBuildingDTO) {
        // Xuong Service thao tac
        buildingService.updateAssignmentBuilding(assignmentBuildingDTO);
        System.out.println("Assign staffs to manage building successfully!!!");
    }
}
