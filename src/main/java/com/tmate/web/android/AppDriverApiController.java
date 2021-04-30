package com.tmate.web.android;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tmate.domain.*;
import com.tmate.domain.driver.DriverHistoryVO;
import com.tmate.domain.driver.DriverProfileVO;
import com.tmate.service.DriverService;
import com.tmate.service.android.driver.AppDriverService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/driver")
@RequiredArgsConstructor
@Log4j2
public class AppDriverApiController {

    private final DriverService driverService;

    private final AppDriverService appDriverService;

    // 기사 회원가입 - POST
    @PostMapping("/register")
    public ResponseEntity<Boolean> registerDriver(@RequestBody Map<String, String> map) {
        log.info("AppDriverController 넘어오는 기사 정보 map: " + map);
        return new ResponseEntity<>(appDriverService.saveDriverProfile(map),HttpStatus.OK);
    }

    // 기사 승인 확인 - GET
    @GetMapping("/register/approve/{d_id}")
    public ResponseEntity<Boolean> approveSearch(@PathVariable("d_id") String d_id) {
        Boolean approve = appDriverService.searchApprove(d_id);
        log.info("AppDriverController 기사 승인 상태 : " + approve);
        return new ResponseEntity<>(approve, HttpStatus.OK);
    }

    // 운행 기록 리스트 - GET
    // 쿼리문에 따라 VO 값 생성 필요
    @GetMapping("/history/{d_id}")
    public ResponseEntity<List<DriverHistoryVO>> driveHistoryList(@PathVariable("d_id") String d_id) {
        log.info("AppDriverController 기사 운행기록 리스트 d_id : " + d_id);
        return new ResponseEntity<>(appDriverService.historyList(d_id), HttpStatus.OK);
    }

    // 운행 기록 세부 리뷰 리스트 - GET
    @GetMapping("/history/{merchant_uid}")
    public ResponseEntity<List<ReviewDTO>> driverReviewList(@PathVariable("merchant_uid") String merchant_uid) {
        log.info("AppDriverController 기사 기록 리뷰 리스트 merchant_uid : " + merchant_uid);
        return new ResponseEntity<>(appDriverService.getDriverReviewList(merchant_uid), HttpStatus.OK);
    }

    // 기사 프로필 확인 - GET
    @GetMapping("/profile/{d_id}")
    public ResponseEntity<DriverProfileVO> searchDriverProfile(@PathVariable("d_id") String d_id) {
        log.info("AppDriverController 기사 프로필 확인 d_id : " + d_id);
        return new ResponseEntity<>(appDriverService.getDriverProfile(d_id), HttpStatus.OK);
    }

    // 기사 이메일 수정 - PUT
    @PutMapping("/profile/modifyemail")
    public ResponseEntity<Boolean> modifyEmail(@RequestBody MemberDTO memberDTO) {
        log.info("AppDriverController 기사 이메일 수정 memberDTO : " + memberDTO);
        return new ResponseEntity<>(appDriverService.modifyDriverEmail(memberDTO), HttpStatus.OK);
    }

    // 기사 차량 리스트 - GET
    @GetMapping("/car/list/{d_id}")
    public ResponseEntity<List<CarDTO>> driverCarList(@PathVariable("d_id") String d_id) {
        log.info("AppDriverController 기사 차량 리스트 d_id : " + d_id);
        return new ResponseEntity<>(appDriverService.getDriverCarList(d_id), HttpStatus.OK);
    }

    // 기사 차량 추가 - POST
    @PostMapping("/car")
    public ResponseEntity<Boolean> insertCar(@RequestBody CarDTO carDTO) {
        log.info("AppDriverController 기사 차량 추가 CarDTO : " + carDTO);
        return new ResponseEntity<>(appDriverService.insertDriverCar(carDTO),HttpStatus.OK);
    }

    // 블랙리스트 확인 - GET
    @GetMapping("/ban/list/{d_id}")
    public ResponseEntity<List<JoinBanVO>> getBlacklist(@PathVariable("d_id") String d_id) {
        log.info("AppDriverController 기사 블랙리스트 확인 d_id : " + d_id);
        return new ResponseEntity<>(driverService.getBanList(d_id),HttpStatus.OK);
    }

    // 블랙리스트 추가 - POST
    @PostMapping("/ban/insert")
    public ResponseEntity<Boolean> addBlacklist(@RequestBody BanDTO banDTO) {
        log.info("AppDriverController 기사 블랙리스트 추가 BanDTO : " + banDTO);
        return new ResponseEntity<>(appDriverService.blacklistRegister(banDTO), HttpStatus.OK);
    }

    // 블랙리스트 제거 - DELETE
    @DeleteMapping("/ban/delete")
    public ResponseEntity<Boolean> removeBlacklist(@RequestBody BanDTO banDTO) {
        log.info("AppDriverController 기사 블랙리스트 제거 BanDTO : " + banDTO);
        return new ResponseEntity<>(appDriverService.blacklistRemove(banDTO),HttpStatus.OK);
    }

    // 기사 상태 바꾸기 - PUT
    @PutMapping("/status/set")
    public ResponseEntity<Boolean> setStatus(@RequestBody DriverDTO driverDTO) {
        log.info("app에서 넘어오는 기사 정보 DriverDTO : " + driverDTO);
        return new ResponseEntity<>(appDriverService.driverModStatus(driverDTO), HttpStatus.OK);
    }



}
