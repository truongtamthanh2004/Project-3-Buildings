package com.javaweb.controller.admin;



import com.javaweb.enums.districtCode;
import com.javaweb.enums.typeCode;
import com.javaweb.model.dto.BuildingDTO;
import com.javaweb.model.request.BuildingSearchRequest;
import com.javaweb.model.response.BuildingSearchResponse;
import com.javaweb.service.IBuildingService;
import com.javaweb.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@RestController(value="buildingControllerOfAdmin")
public class BuildingController {
    @Autowired
    IUserService userService;
    @Autowired
    IBuildingService buildingService;

    @GetMapping(value="/admin/building-list")
    public ModelAndView buildingList(@ModelAttribute("modelSearch")BuildingSearchRequest buildingSearchRequest){
        ModelAndView modelAndView = new ModelAndView("admin/building/list");
        modelAndView.addObject("staffs", userService.getStaffs());
        modelAndView.addObject("districtCode", districtCode.district());
        modelAndView.addObject("typeCodes", typeCode.getTypeCode());

        // Xuong DB Lay Data len
//        List<BuildingSearchResponse> result = new ArrayList<>();
//
//        BuildingSearchResponse building1 = new BuildingSearchResponse();
//        building1.setId(1L);
//        building1.setName("DEV Building");
//        building1.setAddress("15 Quang Trung, Quận 10");
//        building1.setManagerName("Nguyễn Văn A");
//        building1.setManagerPhone("03734646432");
//
//        BuildingSearchResponse building2 = new BuildingSearchResponse();
//        building2.setId(3L);
//        building2.setName("ABC Building");
//        building2.setAddress("15 Nguyễn Huệ, Quận 11");
//        building2.setManagerName("Nguyễn Văn B");
//        building2.setManagerPhone("03734646432");
//
//        result.add(building1);
//        result.add(building2);

        List<BuildingSearchResponse> result = buildingService.findAllBuildingSearch(buildingSearchRequest);

        modelAndView.addObject("buildings", result);

        return modelAndView;
    }

    @GetMapping(value="/admin/building-edit")
    public ModelAndView addBuilding(@ModelAttribute("buildingEdit") BuildingDTO buildingDTO){
        ModelAndView modelAndView = new ModelAndView("admin/building/edit");
        modelAndView.addObject("districtCode", districtCode.district());
        modelAndView.addObject("typeCodes", typeCode.getTypeCode());

        return modelAndView;
    }

    @GetMapping(value="/admin/building-edit-{id}")
    public ModelAndView addBuilding(@PathVariable Long id){
        ModelAndView modelAndView = new ModelAndView("admin/building/edit");
        // findByBuildingId
//        BuildingDTO buildingDTO = new BuildingDTO();
//        buildingDTO.setId(id);
//        buildingDTO.setDistrict("QUAN_11");
//        buildingDTO.setWard("Phuong 6");
//        buildingDTO.setStreet("Man Thien");
//        buildingDTO.setNumberOfBasement(3L);
//        List<String> typeCodes = new ArrayList<>();
//        typeCodes.add("NGUYEN_CAN");
//        typeCodes.add("TANG_TRET");
//        buildingDTO.setTypeCode(typeCodes);
        BuildingDTO buildingDTO = buildingService.findById(id);
        System.out.println(buildingDTO.getId());
        modelAndView.addObject("buildingEdit", buildingDTO);
        modelAndView.addObject("districtCode", districtCode.district());
        modelAndView.addObject("typeCodes", typeCode.getTypeCode());
        return modelAndView;
    }
}
