package com.tmate.web;

import com.tmate.domain.KakaoDTO;
import com.tmate.service.android.user.AppPaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/kakao")
@Log4j2
public class PaymentController {

    private final AppPaymentService appPaymentService;

    @GetMapping("/success")
    public String approve (String partner_user_id,String pg_token, Model model) {

        log.info("pg_token : "+pg_token);
        log.info("partner_user_id : " + partner_user_id);

        KakaoDTO result = appPaymentService.kakaoReadyRes(partner_user_id);
        model.addAttribute("result", result);
        model.addAttribute("token",pg_token);
        return "user/payApproval";
    }

    @GetMapping("/fail")
    public String payFalse() {
        return "user/payFalse";
    }

    @GetMapping("/cancel")
    public String payCancel() {
        return "user/payCancel";
    }
}
