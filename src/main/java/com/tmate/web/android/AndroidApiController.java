package com.tmate.web.android;

import com.tmate.domain.*;
import com.tmate.domain.user.ApprovalDTO;
import com.tmate.service.SMSService;
import com.tmate.service.UserService;
import com.tmate.service.android.common.CommonService;
import com.tmate.service.android.user.AppMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Log4j2
public class AndroidApiController {

    // App 사용자 서비스
    private final AppMemberService appMemberService;

    // 유저 서비스
    private final UserService userService;

    // 휴대폰 인증 서비스
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
        if (smsService.confirmNumber.equals(phoneDTO.getConfirm())) {
            log.info("실제 인증번호 : " + smsService.confirmNumber);
            log.info("적은 인증번호 : " + phoneDTO.getConfirm());
            return new ResponseEntity<>(SMSService.CONFIRM, HttpStatus.OK);
        } else {
            log.info("실제 인증번호 : " + smsService.confirmNumber);
            log.info("적은 인증번호 : " + phoneDTO.getConfirm());

            return new ResponseEntity<>(SMSService.REJECT, HttpStatus.BAD_REQUEST);
        }
    }


    /*
    * APP - 사용자용
    * 지인 알림 서비스
    * */


    // 지인 알림 -> 검색 부분
    // 유저 관리 부분
    @PostMapping("/search")
    public ResponseEntity<List<MemberDTO>> searchMembers(@RequestBody String phone) {
        log.info("넘어오는 전화번호 : " + phone);


        return new ResponseEntity<>(userService.getSearchList(phone.substring(1,12)), HttpStatus.OK);
    }

    // 지인 알림 -> 나의 지인들
    @GetMapping("/friend/{m_id}")
    public ResponseEntity<List<NotificationDTO>> friendByUser(@PathVariable("m_id") String m_id) {

        log.info("나의 지인 리스트 위한 코드 : " + m_id);

        return new ResponseEntity<>(userService.getMyNotifiList(m_id), HttpStatus.OK);
    }

    // 지인 알림 -> 나에게 요청한 리스트
    @GetMapping("/myapproval/{m_id}")
    public ResponseEntity<List<ApprovalDTO>> myApprovalList(@PathVariable("m_id") String m_id) {
        log.info("나에게 지인 요청한 리스트 위한 코드 : " + m_id);

        return new ResponseEntity<>(userService.getMyApproValList(m_id), HttpStatus.OK);
    }

    // 내가 지인에게 승인을  요청 할시
    @PostMapping("/approval")
    public ResponseEntity<Boolean> approvalFriend(@RequestBody ApprovalDTO approvalDTO) {

        log.info("내가 승인을 요청한 지인 정보 : " + approvalDTO);
        userService.registerApproval(approvalDTO);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    // 지인 요청 거절 누를 시
    @DeleteMapping("/removeAppro/{id}/{m_id}")
    public ResponseEntity<Boolean> removeApproval(@PathVariable("id") String id, @PathVariable("m_id") String m_id) {
        log.info("승인 요청 아이디 : " + id);
        log.info("내 아이디 : " + m_id);

        userService.removeApproval(id, m_id);

        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    // 승인 허용할 시
    @Transactional
    @PutMapping("/agree/{id}")
    public ResponseEntity<Boolean> agreeAppro(@PathVariable("id") String id,@RequestBody NotificationDTO notificationDTO) {

        log.info("승인 요청 id : " + id);
        log.info("승인 요청 노티피 케이션 : " + notificationDTO);
        String m_id = notificationDTO.getM_id();

        userService.registerNotifi(notificationDTO);
        userService.removeApproval(id, m_id);

        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @PutMapping("/updatestat")
    public ResponseEntity<Boolean> modifyStat(@RequestBody NotificationDTO notificationDTO) {

        log.info("과연 활성화가 될까");
        log.info("넘어오는 notification : " + notificationDTO);
        userService.modifyN_whether(notificationDTO);

        return new ResponseEntity<>(true, HttpStatus.OK);
    }


    @PostMapping("/social")
    public ResponseEntity<Boolean> regiterSocial(@RequestBody SocialDTO socialDTO) {
        log.info("App에서 소셜 연동 할 시: " + socialDTO);

        return new ResponseEntity<>(appMemberService.registerSocialEmail(socialDTO), HttpStatus.OK);
    }


    @GetMapping("/point/{m_id}")
    public ResponseEntity<List<PointDTO>> getPointList(@PathVariable("m_id") String m_id) {
        log.info("포인트 이력 가져오기 :" + m_id);

        return new ResponseEntity<>(appMemberService.getPointListByM_id(m_id), HttpStatus.OK);
    }

    @GetMapping("/bookmark/{m_id}")
    public ResponseEntity<List<BookmarkDTO>> getBookmarkList(@PathVariable("m_id") String m_id) {
        log.info("App 즐겨 찾기 찾습니다. 준비하세요 : " + m_id);

        return new ResponseEntity<>(appMemberService.getBookmarkListByM_id(m_id), HttpStatus.OK);
    }


    /*
     *  사용자 APP 카드 관리 컨트롤러
     * */
    // 카드 리스트 얻기
    @GetMapping("/card/{m_id}")
    public ResponseEntity<List<PaymentDTO>> getUserCard(@PathVariable("m_id") String m_id) {
        log.info("카드 정보 얻고싶어요 APP : " + m_id);

        return new ResponseEntity<>(userService.getPaymentList(m_id), HttpStatus.OK);
    }


    // 카드 등록
    @PostMapping("/regcard")
    public ResponseEntity<Boolean> registerCardApp(@RequestBody PaymentDTO paymentDTO) {
        log.info("등록할려는 카드 정보 : " + paymentDTO);
        return new ResponseEntity<>(userService.registerPayment(paymentDTO), HttpStatus.OK);
    }

    // 카드 삭제
    @DeleteMapping("/remove/{customer_uid}")
    public ResponseEntity<Boolean> removeCard(@PathVariable("customer_uid") String customer_uid) {
        log.info("삭제할려는 카드 빌링키 : " + customer_uid);

        return new ResponseEntity<>(userService.removePayment(customer_uid), HttpStatus.OK);
    }

    // 카드 업데이트 대표 비대표
    @PutMapping("/updaterep/{customer_uid}/{m_id}")
    public ResponseEntity<Boolean> modifyRep(@PathVariable("customer_uid") String customer_uid, @PathVariable("m_id") String m_id) {

        log.info("넘어오는 카드 빌링키 : " + customer_uid);
        log.info("넘어오는 회원 번호 :" + m_id);


        return new ResponseEntity<>(userService.modifyRep(customer_uid, m_id),HttpStatus.OK);
    }


}
