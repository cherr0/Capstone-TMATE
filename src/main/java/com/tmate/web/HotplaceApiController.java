package com.tmate.web;


import com.tmate.domain.PlaceDTO;
import com.tmate.service.PlaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
@Log4j2
public class HotplaceApiController {

    private final PlaceService placeService;

    // 핫플레이스 등록
    @PostMapping("/hotplaceregister")
    public ResponseEntity<Boolean> hotplaceregister(@RequestBody PlaceDTO placeDTO) {
        log.info("넘어오는 플레이스 정보: " + placeDTO);
        return new ResponseEntity<>(placeService.register(placeDTO), HttpStatus.OK);
    }

    // 핫플레이스 삭제
    @DeleteMapping("/hotplaceremove")
    public boolean hotplaceRemove(String p_id) {
        System.out.println("DeleteMapping hotplace Remove() place No : " + p_id);
        return placeService.remove(p_id);
    }
}
