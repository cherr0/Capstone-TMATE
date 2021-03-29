package com.tmate.web.android;

import com.tmate.service.android.user.AppMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
