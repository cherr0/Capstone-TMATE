package com.tmate.web.android;

import com.tmate.domain.DispatchDTO;
import com.tmate.domain.DriverDTO;
import com.tmate.service.android.driver.AppCallService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/call")
@RequiredArgsConstructor
@Log4j2
public class AppCallController {

    private final AppCallService appCallService;

    // 1. 상태 변경 -> 휴식중, 대기중
    @PutMapping("/modify/status/{d_id}/{flag}")
    public ResponseEntity<Boolean> modifyStatusByD_idAndFlag(
            @PathVariable("d_id") String d_id,
            @PathVariable("flag") int flag) {
        DriverDTO driverDTO = new DriverDTO();
        driverDTO.setD_id(d_id);
        driverDTO.setD_status(String.valueOf(flag));

        return new ResponseEntity<>(appCallService.modifyDriverStatus(driverDTO), HttpStatus.OK);
    }

    // 2. 기사 2km 콜정보 가져오기
    @GetMapping("/get/callInfo/{m_lat}/{m_lng}")
    public ResponseEntity<List<DispatchDTO>> getCallInfoByPosition(
            @PathVariable("m_lat") double m_lat,
            @PathVariable("m_lng") double m_lng
    ){
        return new ResponseEntity<>(appCallService.getCallList(m_lat, m_lng), HttpStatus.OK);
    }

    // 3. 기사가 이용정보 수락시
    @PutMapping("/modify/dispatch/{dp_id}/{d_id}/{m_lat}/{m_lng}")
    public ResponseEntity<Boolean> modifyDispatchByDriver(
            @PathVariable("dp_id") String dp_id,
            @PathVariable("d_id") String d_id,
            @PathVariable("m_lat") double m_lat,
            @PathVariable("m_lng") double m_lng
    ){
        return new ResponseEntity<>(appCallService.driverCallAgree(dp_id, d_id, m_lat, m_lng), HttpStatus.OK);
    }

    // 4. 기사 위치 최신화
    @PutMapping("/modify/driver/postion/{m_lat}/{m_lng}/{d_id}")
    public ResponseEntity<Boolean> modifyDriverPostion(
            @PathVariable("m_lat") double m_lat,
            @PathVariable("m_lng") double m_lng,
            @PathVariable("d_id") String d_id
    ){
        return new ResponseEntity<>(appCallService.modifyRealTimeDriverLocation(m_lat, m_lng, d_id), HttpStatus.OK);
    }

    // 5. 탑승 완료 시 -> 운행 시간 , 탑승 중으로 상태 변경
    @PutMapping("/modify/dispatch/boarding/{dp_id}")
    public ResponseEntity<Boolean> modifyDispatchBoarding(@PathVariable("dp_id") String dp_id) {
        return new ResponseEntity<>(appCallService.modifyDispatchBoarding(dp_id), HttpStatus.OK);
    }

    // 6. 탑승 종료 시 -> 운행 종료 시간, 탑승 완료로 상태 변경
    @PutMapping("/modify/dispatch/boardingends/{dp_id}")
    public ResponseEntity<Boolean> modifyDispatchBoardingEnds(@PathVariable("dp_id") String dp_id) {
        return new ResponseEntity<>(appCallService.modifyDispatchBoardingEnds(dp_id), HttpStatus.OK);
    }
}
