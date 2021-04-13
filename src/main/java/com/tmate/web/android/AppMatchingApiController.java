package com.tmate.web.android;

import com.tmate.domain.HistoryDTO;
import com.tmate.service.android.user.AppMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.annotations.UpdateProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Log4j2
@RequestMapping("/match")
@RequiredArgsConstructor
public class AppMatchingApiController {


    private final AppMemberService appMemberService;

    @GetMapping("/getlist/{slttd}/{slngtd}/{flttd}/{flngtd}")
    public ResponseEntity<List<HistoryDTO>> getMatchingList(@PathVariable("slttd") String slttd, @PathVariable("slngtd") String slngtd,
                                                            @PathVariable("flttd") String flttd, @PathVariable("flngtd") String flngtd) {

        log.info("출발지 위도 : " + slttd);
        log.info("출발지 경도 : " + slngtd);
        log.info("도착지 위도 : " + flttd);
        log.info("도착지 경도 : " + flngtd);

        List<HistoryDTO> matchingList = appMemberService.getTogetherMatchingList(slttd, slngtd, flttd, flngtd);
        log.info("App으로 넘어가는 거리 기준 매칭 리스트 :" + matchingList);

        return new ResponseEntity<>(matchingList, HttpStatus.OK);
    }


    @PostMapping("/read")
    public ResponseEntity<HistoryDTO> getMatchingDetail(@RequestBody String merchant_uid) {

        log.info("APP에서 넘어오는 이용방 정보 :  " + merchant_uid);

        HistoryDTO detail = appMemberService.getTogetherMatchingDetail(merchant_uid);

        return new ResponseEntity<>(detail, HttpStatus.OK);
    }
}
