package com.tmate.web;

import com.tmate.domain.DriverDTO;
import com.tmate.domain.JoinDriverVO;
import com.tmate.domain.SMSDTO;
import com.tmate.domain.MemberDTO;
import com.tmate.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class ApiController {

    private ApprovalService approvalService;

    // 승인 모달 창 사용 시 기사 정보 제공
    @PostMapping("/approval/{d_id}")
    public ResponseEntity<JoinDriverVO> popup(String d_id) {
        System.out.println("PostMapping popup() d_id : " + d_id);
        JoinDriverVO driver = approvalService.getDrivers(d_id);

        return new ResponseEntity<>(driver, HttpStatus.OK);
    }

}
