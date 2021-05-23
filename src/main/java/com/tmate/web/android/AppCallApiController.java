//package com.tmate.web.android;
//
//import com.tmate.domain.HistoryDTO;
//import com.tmate.service.android.driver.AppDriverMatchService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/call")
//@RequiredArgsConstructor
//@Log4j2
//public class AppCallApiController {
//
//    private final AppDriverMatchService appDriverMatchService;
//
//    // 1. 상태 변경 -> 휴식중, 대기중
//    @PutMapping("/modify/status/{d_id}/{flag}")
//    public ResponseEntity<Boolean> modifyStatusByD_idAndFlag(@PathVariable("d_id") String d_id, @PathVariable("flag") int flag) {
//        log.info("기사의 상태를 변경합니다. flag : 0(휴식중), flag : 1(대기중) : " + flag);
//
//        return new ResponseEntity<>(appDriverMatchService.modifyDriver_status(d_id, flag), HttpStatus.OK);
//    }
//
//    // 기사 2km 콜정보 가져오기
//    @GetMapping("/get/callinfo/{m_lttd}/{m_lngtd}")
//    public ResponseEntity<List<HistoryDTO>> getCallInfoByPosition(@PathVariable("m_lttd") double m_lttd, @PathVariable("m_lngtd") double m_lngtd) {
//
//        log.info("2km 이내 콜정보를 알기위해 기사의 위치를 가져온다. 위도/경도 : " + m_lttd + "/" + m_lngtd);
//
//        return new ResponseEntity<>(appDriverMatchService.getCallList(m_lttd, m_lngtd), HttpStatus.OK);
//    }
//
//    // 기사가 한 이용정보 수락 시
//    @PutMapping("/modify/history/{merchant_uid}/{d_id}/{m_lttd}/{m_lngtd}")
//    public ResponseEntity<Boolean> modifyHistoryByDriver(
//            @PathVariable("merchant_uid") String merchant_uid,
//            @PathVariable("d_id") String d_id,
//            @PathVariable("m_lttd") double m_lttd,
//            @PathVariable("m_lngtd") double m_lngtd
//    ){
//        log.info("기사가 콜 수락시 넘어오는 정보 -> " + "\n"+"이용코드 : " + merchant_uid + "\n"
//        + "\n" + "기사코드 : " + d_id + "\n" + "기사 위치 : " + m_lttd + "& 기사 경도 : " + m_lngtd);
//
//        return new ResponseEntity<>(appDriverMatchService.driverCallAgree(merchant_uid, d_id, m_lttd, m_lngtd), HttpStatus.OK);
//    }
//}
