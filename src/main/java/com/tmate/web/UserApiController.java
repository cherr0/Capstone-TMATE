package com.tmate.web;

import com.tmate.domain.PaymentDTO;
import com.tmate.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/tuser/*")
@Log4j2
@RequiredArgsConstructor
public class UserApiController {


    private final UserService userService;

    @PostMapping("/mycard")
    public ResponseEntity<PaymentDTO> myCard(@RequestBody String customer_uid) {
        log.info("넘어오는 빌링키 : " + customer_uid);
        int index = customer_uid.indexOf("=");
        String customer_id = customer_uid.substring(0, index);
        log.info("데이터 확인 : " + customer_id);
        PaymentDTO payment = userService.readPayment(customer_id);
        log.info("넘어가는 카드 정보 : " + payment);
        return new ResponseEntity<>(payment, HttpStatus.OK);
    }

    @DeleteMapping("/remove/{customer_uid}")
    public ResponseEntity<Boolean> removeCard(@PathVariable("customer_uid") String customer_uid) {
        log.info("삭제시 넘어오는 빌링키 : " + customer_uid);

        return new ResponseEntity<>(userService.removePayment(customer_uid), HttpStatus.OK);
    }
}
