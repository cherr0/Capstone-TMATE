package com.tmate.web.android;

import com.tmate.domain.JoinHistoryVO;
import com.tmate.domain.MemberDTO;
import com.tmate.service.android.user.AppMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Log4j2
public class AndroidApiController {

    private final AppMemberService appMemberService;

    @PostMapping("/register")
    public Boolean registerMember(@RequestBody Map<String, String> memberDTO) {
        log.info("app에서 넘어오는 회원가입 요청한 회원 정보 : " + memberDTO);


        return appMemberService.registerMember(memberDTO);
    }

    @GetMapping("/select/{m_id}")
    public ResponseEntity<MemberDTO> getProfile(@PathVariable("m_id") String m_id) {
        log.info("넘어오는 회원 번호 : " + m_id);

        return new ResponseEntity<>(appMemberService.getMemberProfile(m_id), HttpStatus.OK);
    }

    @GetMapping("/historys/{m_id}")
    public ResponseEntity<List<JoinHistoryVO>> getHistory(@PathVariable("m_id") String m_id) {
        log.info("이용내역 요청 시 넘어오는 회원 번호 : " + m_id);


        return new ResponseEntity<>(appMemberService.getMemberHistoryList(m_id),HttpStatus.OK);
    }
}
