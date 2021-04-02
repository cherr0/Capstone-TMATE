package com.tmate.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/kakao")
@Log4j2
public class PaymentController {


    @GetMapping("/payment")
    public String approve (String pg_token, Model model) {

        log.info("잘 넘어오나보자 : "+pg_token);

        model.addAttribute("token",pg_token);
        return "user/payApproval";
    }
}
