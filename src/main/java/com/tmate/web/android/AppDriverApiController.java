package com.tmate.web.android;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tmate.domain.*;
import com.tmate.domain.driver.AttendListVO;
import com.tmate.domain.driver.DriverHistoryVO;
import com.tmate.domain.driver.DriverProfileVO;
import com.tmate.domain.driver.SidebarProfileVO;
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

    // 운행이력 블랙리스트 추가할 때 표시할 데이터 리스트 - GET
    @GetMapping("/history/ban/{dp_id}")
    public ResponseEntity<List<AttendListVO>> historyAttendList(@PathVariable("dp_id") String dp_id) {
        return new ResponseEntity<>(appDriverService.searchAttendList(dp_id),HttpStatus.OK);
    }

    // 기사 사이드바 프로필 가져오기 - GET
    @GetMapping("/profile/side/{d_id}")
    public ResponseEntity<SidebarProfileVO> driverSidebarProfile(@PathVariable("d_id") String d_id) {
        SidebarProfileVO profile = appDriverService.getSidebarProfileById(d_id);

        log.info("AppDriverController 기사 사이드바 프로필 : " + profile);
        return new ResponseEntity<>(profile, HttpStatus.OK);
    }

    // 기사 프로필 확인 - GET
    @GetMapping("/profile/{d_id}")
    public ResponseEntity<DriverProfileVO> searchDriverProfile(@PathVariable("d_id") String d_id) {
        log.info("AppDriverController 기사 프로필 확인 d_id : " + d_id);
        DriverProfileVO driverProfile = appDriverService.getDriverProfile(d_id);

        log.info("AppDriverController 기사 프로필 : " + driverProfile);
        return new ResponseEntity<>(driverProfile, HttpStatus.OK);
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

    // 기사 차량 선택 - PUT
    @PutMapping("/car/{d_id}/{car_no}")
    public ResponseEntity<Boolean> selectDriverCar(@PathVariable("d_id") String d_id,
                                                   @PathVariable("car_no") String car_no) {
        log.info("AppDriverController 기사 차량 선택 d_id : " + d_id);
        return new ResponseEntity<>(appDriverService.selectDriverCar(d_id,car_no), HttpStatus.OK);
    }

    // 기사 차량 제거 - DELETE
    @DeleteMapping("/car/{car_no}")
    public ResponseEntity<Boolean> deleteCar(@PathVariable("car_no") String car_no) {
        return new ResponseEntity<>(appDriverService.deleteDriverCar(car_no), HttpStatus.OK);
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
    @DeleteMapping("/ban/delete/{d_id}/{m_id}")
    public ResponseEntity<Boolean> removeBlacklist(@PathVariable("d_id") String d_id,
                                                   @PathVariable("m_id") String m_id) {
        log.info("AppDriverController 기사 블랙리스트 제거 d_id & m_id : " + d_id + " & " + m_id);
        return new ResponseEntity<>(appDriverService.blacklistRemove(d_id,m_id),HttpStatus.OK);
    }

    // 기사 상태 바꾸기 - PUT
    @PutMapping("/status/set/{d_id}/{d_status}")
    public ResponseEntity<Boolean> setStatus(@PathVariable("d_id") String d_id, @PathVariable("d_status") int d_status) {
        log.info("app에서 넘어오는 기사 정보 d_id : " + d_id + ", d_status : " + d_status);
        return new ResponseEntity<>(appDriverService.driverModStatus(d_id, d_status), HttpStatus.OK);
    }



}
