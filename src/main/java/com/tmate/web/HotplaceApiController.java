package com.tmate.web;


import com.tmate.domain.MonthlyUsersVO;
import com.tmate.domain.PlaceDTO;
import com.tmate.service.PlaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @DeleteMapping("/hotplaceremove/{pl_id}")
    public boolean hotplaceRemove(@PathVariable("pl_id") String p_id) {
        System.out.println("DeleteMapping hotplace Remove() place No : " + p_id);
        return placeService.remove(p_id);
    }

    // 핫플레이스 리스트 조회
    @GetMapping(value = "/placelist",
            produces =
                    {MediaType.APPLICATION_JSON_VALUE,
                            MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<PlaceDTO>> getMonthlyList() {
        log.info("핫플레이스 리스트 보내는 중");
        return new ResponseEntity<>(placeService.getHotPlaceList(),HttpStatus.OK) ;
    }

}
