package com.tmate.web;

import com.tmate.domain.PlaceDTO;
import com.tmate.service.PlaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
@Log4j2
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



}
