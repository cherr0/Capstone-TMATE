package com.tmate.web.android;

import com.tmate.domain.JoinHistoryVO;
import com.tmate.domain.MemberDTO;
import com.tmate.domain.PhoneDTO;
import com.tmate.service.SMSService;
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

    private final SMSService smsService;

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


        return new ResponseEntity<>(appMemberService.getMemberHistoryList(m_id), HttpStatus.OK);
    }


    // 휴대폰 인증번호
    @PostMapping("/sendsms")
    public ResponseEntity<Boolean> sendSMS(@RequestBody PhoneDTO phoneDTO) {
        log.info("인증번호 전송 sendSMS() Phone Num : " + phoneDTO.toString());

        return new ResponseEntity<>(smsService.certifiedPhoneNumber(phoneDTO), HttpStatus.OK);
    }

    // 휴대폰 인증 확인
    @PostMapping("/confirm")
    public ResponseEntity<Integer> confirm(@RequestBody PhoneDTO phoneDTO) {
        if(smsService.confirmNumber.equals(phoneDTO.getConfirm())){
            log.info("실제 인증번호 : " + smsService.confirmNumber);
            log.info("적은 인증번호 : " + phoneDTO.getConfirm());
            return new  ResponseEntity<>(SMSService.CONFIRM,HttpStatus.OK);
        }else {
            log.info("실제 인증번호 : " + smsService.confirmNumber);
            log.info("적은 인증번호 : " + phoneDTO.getConfirm());

            return  new  ResponseEntity<>(SMSService.REJECT,HttpStatus.BAD_REQUEST);
        }
    }
}
