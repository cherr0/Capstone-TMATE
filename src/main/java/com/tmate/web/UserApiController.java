package com.tmate.web;

import com.tmate.domain.MemberDTO;
import com.tmate.domain.NotificationDTO;
import com.tmate.domain.PaymentDTO;
import com.tmate.domain.user.ApprovalDTO;
import com.tmate.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import oracle.ucp.proxy.annotation.Post;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping(value = "/registercard",
            produces =
                    {MediaType.APPLICATION_JSON_VALUE,
                            MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Boolean> registerCard(@RequestBody PaymentDTO paymentDTO) {
        log.info("등록할려는 카드 정보 : " + paymentDTO);
        return new ResponseEntity<>(userService.registerPayment(paymentDTO), HttpStatus.OK);
    }

    @DeleteMapping("/remove/{customer_uid}")
    public ResponseEntity<Boolean> removeCard(@PathVariable("customer_uid") String customer_uid) {
        log.info("삭제시 넘어오는 빌링키 : " + customer_uid);

        return new ResponseEntity<>(userService.removePayment(customer_uid), HttpStatus.OK);
    }


    // 유저 관리 부분
    @PostMapping("/search")
    public ResponseEntity<List<MemberDTO>> searchMembers(@RequestBody String phone) {
        log.info("넘어오는 전화번호 : " + phone);
        int index = phone.indexOf("=");
        String phoneNo = phone.substring(0, index);

        return new ResponseEntity<>(userService.getSearchList(phoneNo), HttpStatus.OK);
    }

    // 승인 요청 부분
    @PostMapping(value ="/approval",
            produces =
                    {MediaType.APPLICATION_JSON_VALUE,
                            MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Boolean> registerAppro(@RequestBody ApprovalDTO approvalDTO) {
        log.info("넘어오는 정보" + approvalDTO);
        userService.registerApproval(approvalDTO);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @DeleteMapping("/removeAppro/{id}/{m_id}")
    public ResponseEntity<Boolean> removeAppro(@PathVariable("id") String id,@PathVariable("m_id") String m_id) {
        log.info("id: " + id);
        log.info("m_id : " + m_id);

        userService.removeApproval(id,m_id);

        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @Transactional
    @PutMapping(value = "/agree/{m_id}",
            produces =
                    {MediaType.APPLICATION_JSON_VALUE,
                            MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Boolean> agreeAppro(@PathVariable("m_id") String m_id, @RequestBody NotificationDTO notificationDTO) {
        log.info("승인 요청 id: " + m_id);
        log.info("승인 요청 노티피케이션 : " + notificationDTO);
        String id = notificationDTO.getM_id();

        userService.registerNotifi(notificationDTO);
        userService.removeApproval(id, m_id);

        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @PutMapping(value = "/updatestat",
            produces = {MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Boolean> modifyStat(@RequestBody NotificationDTO notificationDTO) {

        log.info("넘어오는 notification: " + notificationDTO);
        userService.modifyN_whether(notificationDTO);

        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}
