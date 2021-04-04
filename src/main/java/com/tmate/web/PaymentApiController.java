package com.tmate.web;

import com.tmate.service.android.user.AppPaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
@Log4j2
public class PaymentApiController {

    private final AppPaymentService appPaymentService;

    @PostMapping("/kakaoReady")
    public Boolean kakaoReady(@RequestBody Map<String, String> map) {
        log.info("안드로이드에서 넘어온 결제 준비 정보 : " + map);

        return appPaymentService.kakaoReady(map);
    }
}
