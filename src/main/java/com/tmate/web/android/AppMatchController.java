package com.tmate.web.android;

import com.tmate.domain.DispatchDTO;
import com.tmate.domain.MemberDTO;
import com.tmate.service.android.user.AppMatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/match")
@RequiredArgsConstructor
@Log4j2
public class AppMatchController {

    private final AppMatchService appMatchService;

    /*
    * 일반 호출
    * */

    // 일반 호출 생성
    @PostMapping("/register/normal")
    public ResponseEntity<String> registerNormalMatching(@RequestBody DispatchDTO dispatchDTO) {
        String dp_id = appMatchService.registerNormalMatch(dispatchDTO);
        log.info("받아온 배차 정보 : " + dispatchDTO);
        return new ResponseEntity<>(dp_id, HttpStatus.OK);
    }

    // 일반 호출 뒤로가기시 삭제
    @DeleteMapping("/remove/normal/call/{dp_id}")
    public ResponseEntity<Boolean> removeNormalMatching(@PathVariable("dp_id") String dp_id) {
        return new ResponseEntity<>(appMatchService.removeNowCall(dp_id),HttpStatus.OK);
    }

    // 일반 호출시 기사 계속 찾기
    @GetMapping("/get/driver/{dp_id}")
    public ResponseEntity<String> getDriver(@PathVariable("dp_id") String dp_id) {

        log.info("기사찾는 중 이용코드 : " + dp_id);
        String d_id = appMatchService.getD_idDuringCall(dp_id);

        return new ResponseEntity<>(d_id, HttpStatus.OK);
    }

    // 호출 매칭시 상태 바꿔주면서 기사 값도 넣어준다.
    @PutMapping("/modify/match/status/{dp_id}/{d_id}")
    public ResponseEntity<Boolean> modifyMatchStatus(
            @PathVariable("dp_id") String dp_id,
            @PathVariable("d_id") String d_id
    ){
        return null;
    }

    // 현재 이용중인 이용정보 보여주기
    @GetMapping("/get/dispatch/{m_id}")
    public ResponseEntity<DispatchDTO> getCurrentDispatch(@PathVariable("m_id") String m_id) {
        log.info("이용중인 이용 요약 정보 : " + m_id);
        return new ResponseEntity<>(appMatchService.getCurrentCallInfo(m_id), HttpStatus.OK);
    }

    // 현재 이용중인 이용정보 클릭할 시 이용 상세정보 가져온다
    @GetMapping("/read/dispatch/{dp_id}")
    public ResponseEntity<DispatchDTO> readCurrentDispatch(@PathVariable("dp_id") String dp_id) {
        log.info("이용 상세정보 가져오기 : " + dp_id );
        return new ResponseEntity<>(appMatchService.getDetailCurrentCallInfo(dp_id), HttpStatus.OK);
    }


    // 기사위치 가져온다. --> 쓰레드
    @GetMapping("/get/driver/position/{dp_id}")
    public ResponseEntity<DispatchDTO> getDriverPosition(@PathVariable("dp_id") String dp_id) {
        log.info("기사의 위치를 가져옵니다." + dp_id);
        return new ResponseEntity<>(appMatchService.getCurrentDriverLocation(dp_id), HttpStatus.OK);
    }
}
