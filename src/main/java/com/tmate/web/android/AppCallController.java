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
    @GetMapping("/get/callinfo/{m_lat}/{m_lng}")
    public ResponseEntity<List<DispatchDTO>> getCallInfoByPosition(
            @PathVariable("m_lat") double m_lat,
            @PathVariable("m_lng") double m_lng
    ){
        log.info("콜 정보를 찾습니다." + m_lat + m_lng);
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
    @PutMapping("/modify/driver/position/{m_lat}/{m_lng}/{d_id}")
    public ResponseEntity<Boolean> modifyDriverPosition(
            @PathVariable("m_lat") double m_lat,
            @PathVariable("m_lng") double m_lng,
            @PathVariable("d_id") String d_id
    ){
        log.info("기사정보를 최신화 시키기위한 것 : " + m_lat + ":" + m_lng);
        return new ResponseEntity<>(appCallService.modifyRealTimeDriverLocation(m_lat, m_lng, d_id), HttpStatus.OK);
    }

    // 5. 탑승 완료 시 -> 운행 시간 , 탑승 중으로 상태 변경
    @GetMapping("/modify/dispatch/boarding/{d_id}")
    public ResponseEntity<DispatchDTO> modifyDispatchBoarding(@PathVariable("d_id") String d_id) {
        log.info("승객 잘태우는지 확인합니다." + d_id);
        return new ResponseEntity<>(appCallService.modifyDispatchBoarding(d_id), HttpStatus.OK);
    }

    // 6. 탑승 종료 시 -> 운행 종료 시간, 탑승 완료로 상태 변경
    @PutMapping("/modify/dispatch/boardingends/{dp_id}")
    public ResponseEntity<Boolean> modifyDispatchBoardingEnds(@PathVariable("dp_id") String dp_id) {
        return new ResponseEntity<>(appCallService.modifyDispatchBoardingEnds(dp_id), HttpStatus.OK);
    }

    // 7. 네비 앱으로 넘어 갈 시 현재 이용중인 승객에게 전화하기 위해 회원코드를 반환
    @GetMapping("/get/using/m_id/{d_id}")
    public ResponseEntity<DispatchDTO> getUsingM_idByD_id(@PathVariable("d_id") String d_id) {
        log.info("기사가 승객에게 전화하기 위해 넘어오는 기사코드 : " + d_id);

        return new ResponseEntity<>(appCallService.getUsingServiceM_id(d_id),HttpStatus.OK);
    }

    // 8. 결제 미터기 화면시 미터기 넣고 입력을 누르면 all_fare 업데이트
    @PutMapping("/modify/payment/dp_status/{dp_id}/{all_fare}/{dp_status}")
    public ResponseEntity<Boolean> modifyPaymentDpStatus(
            @PathVariable("dp_id") String dp_id,
            @PathVariable("all_fare") int all_fare,
            @PathVariable("dp_status") String dp_status
    ){
        log.info("기사가 결제화면에서 미터기 입력시 : "+ dp_id + " : " + all_fare + ":" + dp_status);

        return new ResponseEntity<>(appCallService.modifyFareDuringPayment(dp_id, all_fare, dp_status), HttpStatus.OK);

    }

}
