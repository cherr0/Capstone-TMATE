package com.tmate.web;

import com.tmate.domain.KakaoDTO;
import com.tmate.service.android.user.AppPaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
@Log4j2
public class PaymentApiController {

    private final AppPaymentService appPaymentService;

    @PostMapping("/kakaoReady")
    public ResponseEntity<Boolean> kakaoReady(@RequestBody Map<String, String> map) {
        log.info("안드로이드에서 넘어온 결제 준비 정보 : " + map);

        return new ResponseEntity<>(appPaymentService.kakaoReady(map), HttpStatus.OK);
    }

    @PutMapping("/kakaoApprove")
    public ResponseEntity<Boolean> kakaoApprove(@RequestBody KakaoDTO kakaoDTO) {
        log.info("거래 승인 완료 정보 : " + kakaoDTO);

        return new ResponseEntity<>(appPaymentService.kakaoApprove(kakaoDTO), HttpStatus.OK);
    }
}
