package com.tmate.web;

import com.tmate.domain.MemberDTO;
import com.tmate.domain.PhoneDTO;
import com.tmate.service.SMSService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
@Log4j2
public class indexApiController {

    private final SMSService smsService;

    @PostMapping("/sendSMS")
    public boolean sendSMS(@RequestBody PhoneDTO phone) {
        System.out.println("인증번호 전송 sendSMS() phone Num : " + phone.toString());

        return smsService.certifiedPhoneNumber(phone);
    }

    @PostMapping("/confirm")
    public int confirm(@RequestBody PhoneDTO phone) {
        if(smsService.confirmNumber.equals(phone.getConfirm())){
            System.out.println("실제 인증번호 : " + smsService.confirmNumber);
            System.out.println("적은 인증번호 : " + phone.getConfirm());

            return SMSService.CONFIRM;
        }else {
            System.out.println("실제 인증번호 : " + smsService.confirmNumber);
            System.out.println("적은 인증번호 : " + phone.getConfirm());

            return SMSService.REJECT;
        }
    }

    @PostMapping("/login")
    public MemberDTO login(@RequestBody PhoneDTO phone, HttpSession httpSession) {
        log.info(phone);
        MemberDTO member = smsService.getPermission(phone);

        System.out.println("login member : " + member);
        httpSession.setAttribute("member", member);
        return member;
    }
}
