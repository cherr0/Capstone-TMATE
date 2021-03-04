package com.tmate.web;

import com.tmate.domain.JoinDriverVO;
import com.tmate.service.ApprovalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
@Log4j2
public class ApprovalApiController {

    private final ApprovalService approvalService;

    // 승인 모달 창 사용 시 기사 정보 제공
    @GetMapping(value = {"/approval/{d_id}"},
            produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<JoinDriverVO> popup(@PathVariable String d_id) {
        log.info("넘어오는 기사코드 " + d_id);
        JoinDriverVO driver = approvalService.getDrivers(d_id);
        log.info("화면으로 넘어가는 기사정보 " + driver);
        return new ResponseEntity<>(driver, HttpStatus.OK);
    }

    // 관리자 승인
    @PostMapping("/approvalallow")
    public ResponseEntity<Integer> allowApproval(@RequestBody String did) {
        log.info("기사코드: " + did);
        String substring = did.substring(0, 14);
        log.info(substring);
        return new ResponseEntity<>(approvalService.allowApproval(substring), HttpStatus.OK);
    }

    // 관리자 거절
    @DeleteMapping("/approvalremove")
    public ResponseEntity<Integer> removeDriver(String rid) {
        System.out.println("PostMapping removeDriver() driver No : " + rid);
        String substring = rid.substring(0, 14);
        log.info(substring);
        return new ResponseEntity<>(approvalService.removeDriver(substring),HttpStatus.OK);
    }

}
