package com.tmate.web.android;

import com.tmate.service.android.common.AppNoShowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("noshow")
@RequiredArgsConstructor
@Log4j2
public class AppNoShowController {

    private final AppNoShowService appNoShowService;

    //  1. 방장이 동승자 노쇼 버튼 눌렀을 시 사용자  APP
    @PutMapping("/modify/together/noshow/{m_id}/{dp_id}")
    public ResponseEntity<Boolean> modifyTogetherNoshow(
            @PathVariable("m_id") String m_id,
            @PathVariable("dp_id") String dp_id
    ) {
        log.info("방장이 동승자 노쇼 버튼 누를 시 -> " + m_id + " : " + dp_id);

        return new ResponseEntity<>(appNoShowService.MasterClickNoShowBtn(m_id, dp_id), HttpStatus.OK);
    }

    // 2. 기사가 승객 노쇼 버튼 눌렀을 시 기사 APP
    @PutMapping("/modify/driver/noshow/{m_id}/{dp_id}")
    public ResponseEntity<Boolean> modifyDriverNoShow(
            @PathVariable("m_id") String m_id,
            @PathVariable("dp_id") String dp_id
    ) {
        log.info("기사가 동승자 노쇼 버튼 누를 시 -> " + m_id + " : " + dp_id);

        return new ResponseEntity<>(appNoShowService.DriverClickNoShowBtn(m_id, dp_id), HttpStatus.OK);
    }

    // 3. 회원 정보 가져오기 사용자 APP
    @GetMapping("/get/member/status/{m_id}")
    public ResponseEntity<String> getMemberStatus(@PathVariable("m_id") String m_id) {
        log.info("현재 회원의 상태를 가져옵니다." + m_id);

        return new ResponseEntity<>(appNoShowService.getMemberStatus(m_id), HttpStatus.OK);
    }

    // 4. 멤버 정상 상태로 돌리는 것 사용자 APP
    @PutMapping("/modify/member/status/{m_id}")
    public ResponseEntity<Boolean> modifyMemberStatus(@PathVariable("m_id") String m_id) {
        log.info("멤버 정상으로 돌립니다. " + m_id);

        return new ResponseEntity<>(appNoShowService.modifyMemberNormalStatus(m_id), HttpStatus.OK);
    }
}
