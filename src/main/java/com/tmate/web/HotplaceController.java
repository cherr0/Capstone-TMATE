package com.tmate.web;

import com.tmate.domain.PlaceDTO;
import com.tmate.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class HotplaceController {

    private final PlaceService placeService;

    // 핫플레이스 목록 관리
    @GetMapping("/hotplace")
    public String hotPlace(Model model) {
        List<PlaceDTO> placeList = placeService.getHotPlaceList();
        System.out.println("Controller hotPlace() use");
        model.addAttribute("placeList", placeList);
        return "hotplace";
    }


    // 핫플레이스 등록


    // 핫플레이스 삭제
    @DeleteMapping("/api/hotplaceremove")
    public boolean hotplaceRemove(String p_id) {
        System.out.println("DeleteMapping hotplace Remove() place No : " + p_id);
        return placeService.remove(p_id);
    }
}
