package com.tmate.web.android;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tmate.domain.AttendDTO;
import com.tmate.domain.DispatchDTO;;
import com.tmate.domain.ReviewVO;
import com.tmate.service.android.user.AppMatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
        return new ResponseEntity<>(appMatchService.removeNowCall(dp_id), HttpStatus.OK);
    }

    // 일반 호출시 기사 계속 찾기
    @GetMapping("/get/driver/{dp_id}")
    public ResponseEntity<String> getDriver(@PathVariable("dp_id") String dp_id) {

        String d_id = appMatchService.getD_idDuringCall(dp_id);

        return new ResponseEntity<>(d_id, HttpStatus.OK);
    }

    // 호출 매칭시 상태 바꿔주면서 기사 값도 넣어준다.
    @PutMapping("/modify/match/status/{dp_id}/{d_id}")
    public ResponseEntity<Boolean> modifyMatchStatus(
            @PathVariable("dp_id") String dp_id,
            @PathVariable("d_id") String d_id
    ) {
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
        log.info("이용 상세정보 가져오기 : " + dp_id);
        return new ResponseEntity<>(appMatchService.getDetailCurrentCallInfo(dp_id), HttpStatus.OK);
    }


    // 기사위치 가져온다. --> 쓰레드
    @GetMapping("/get/driver/position/{dp_id}")
    public ResponseEntity<DispatchDTO> getDriverPosition(@PathVariable("dp_id") String dp_id) {
        return new ResponseEntity<>(appMatchService.getCurrentDriverLocation(dp_id), HttpStatus.OK);
    }

    /*
     *  -------------------
     *       동승 호출
     *  -------------------
     * */

    // 출발지 800m, 목적지 가까운 순으로 리스트 뽑아오기
    @GetMapping("/get/together/list/{s_lat}/{s_lng}/{f_lat}/{f_lng}")
    public ResponseEntity<List<DispatchDTO>> getTogetherList(
            @PathVariable("s_lat") double s_lat,
            @PathVariable("s_lng") double s_lng,
            @PathVariable("f_lat") double f_lat,
            @PathVariable("f_lng") double f_lng
    ) {
        log.info("동승 리스트 검색 : " + s_lat + " : " + s_lng + " : " + f_lat + " : " + f_lng);

        return new ResponseEntity<>(appMatchService.getNearMatchList(s_lat, s_lng, f_lat, f_lng), HttpStatus.OK);
    }

    // 맘에 드는 리스트가 없을 시 자기가 방을 만든다.
    @PostMapping("/register/together/match")
    public ResponseEntity<String> registerTogetherMatch(@RequestBody Map<String, Object> hashMap) {
        ObjectMapper mapper = new ObjectMapper();
        DispatchDTO dispatchDTO = mapper.convertValue(hashMap.get("dispatch"), new TypeReference<DispatchDTO>() {
        });
        AttendDTO attendDTO = mapper.convertValue(hashMap.get("attend"), new TypeReference<AttendDTO>() {
        });

        log.info("동승 매칭 방을 생성하기 위해 넘어오는 배차 정보 & 참여 정보 : " + dispatchDTO + "," + attendDTO);

        return new ResponseEntity<>(appMatchService.registerMatch(dispatchDTO, attendDTO), HttpStatus.OK);
    }

    //  배차 정보 삭제
    @DeleteMapping("/remove/together/match/{dp_id}")
    public ResponseEntity<Boolean> removeTogetherMatch(@PathVariable("dp_id") String dp_id) {
        log.info("배차 정보 삭제 : " + dp_id);

        return new ResponseEntity<>(appMatchService.removeMatch(dp_id), HttpStatus.OK);
    }

    // 동승 참가 버튼
    @PostMapping("/register/apply/match")
    public ResponseEntity<Boolean> registerApplyMatch(@RequestBody AttendDTO attendDTO) {
        log.info("동승 참가 시 : " + attendDTO);

        return new ResponseEntity<>(appMatchService.registerApplyButton(attendDTO), HttpStatus.OK);
    }

    // 매칭 대기시 동승 정보
    @GetMapping("/get/current/dispatch/{dp_id}")
    public ResponseEntity<DispatchDTO> getCurrentDispatchBeforeSuccess(@PathVariable("dp_id") String dp_id) {

        log.info("매칭 대기 중 동승 정보 : " + dp_id);
        return new ResponseEntity<>(appMatchService.getCurrendDispatch(dp_id),HttpStatus.OK);
    }

    // 동승 거절 버튼
    @PutMapping("/reject/apply/match/{dp_id}/{m_id}")
    public ResponseEntity<Boolean> rejectApplyMatch(
            @PathVariable("dp_id") String dp_id,
            @PathVariable("m_id") String m_id
    ) {
        log.info("동승 거절 버튼 누를 시 : " + dp_id + ", " + m_id);

        return new ResponseEntity<>(appMatchService.modifyRejectMatching(dp_id, m_id), HttpStatus.OK);
    }

    // 동승 수락 버튼
    @PutMapping("/agree/apply/match/{dp_id}/{m_id}")
    public ResponseEntity<Boolean> agreeApplyMatch(
            @PathVariable("dp_id") String dp_id,
            @PathVariable("m_id") String m_id
    ) {
        log.info("동승 수락 버튼 누를 시 : " + dp_id + ", " + m_id);

        return new ResponseEntity<>(appMatchService.modifyAggreeMatching(dp_id, m_id), HttpStatus.OK);
    }

    // 동승자 신청 리스트
    @GetMapping("/get/applyer/list/{dp_id}")
    public ResponseEntity<List<AttendDTO>> getApplyerList(@PathVariable("dp_id") String dp_id) {
        log.info("동승자 신청 리스트 검색 : " + dp_id);

        return new ResponseEntity<>(appMatchService.getApplyerList(dp_id), HttpStatus.OK);
    }

    // 동승자 정보들
    @GetMapping("/get/passenger/list/{dp_id}")
    public ResponseEntity<List<AttendDTO>> getPassengerList(@PathVariable("dp_id") String dp_id) {
        log.info("동승자 정보 리스트 검색 : " + dp_id);

        return new ResponseEntity<>(appMatchService.getPassengerList(dp_id), HttpStatus.OK);
    }

    // 이미 참가된 승객들의 좌석 보여주기
    @GetMapping("/get/choice/seat/{dp_id}")
    public ResponseEntity<List<AttendDTO>> getChoiceSeatNo(@PathVariable("dp_id") String dp_id) {
        log.info("이미 선택된 좌석 검색 : " + dp_id);
        return new ResponseEntity<>(appMatchService.alreadyChoiceSeatNO(dp_id), HttpStatus.OK);
    }

    // 운행 완료 후 리뷰 데이터 삽입
    @PutMapping("/finish/review")
    public ResponseEntity<Boolean> updateReview(@RequestBody ReviewVO reviewVO) {
        log.info("사용자 리뷰 목록 업데이트 : " + reviewVO);
        return new ResponseEntity<>(appMatchService.attendReviewUpdate(reviewVO), HttpStatus.OK);
    }

    // 동승시 방장이 버틑클릭시 바뀌는 동승 이용정보의 힘
    @PostMapping("/modify/together/status")
    public ResponseEntity<Boolean> modifyTogetherStatus(@RequestBody DispatchDTO dispatchDTO) {
        log.info("버튼클릭시 스탯이 바뀌어요 ! " + dispatchDTO.getDp_status());

        return new ResponseEntity<>(appMatchService.modifyTogetherStatus(dispatchDTO), HttpStatus.OK);
    }

    // 사용자가 호출 이용시 일반 or 동승 인지에 따라 횟수 추가 시킨다.
    @PutMapping("/modify/call/cnt/{m_id}/{flag}")
    public ResponseEntity<Boolean> modifyCallCnt(
            @PathVariable("m_id") String m_id,
            @PathVariable("flag") int flag
    ) {
        log.info("이용 횟수 추가하기 위한 : " + m_id + " : " + flag);

        return new ResponseEntity<>(appMatchService.modifyCallCnt(m_id, flag), HttpStatus.OK);
    }

    // 사용자의 탑승자 일때 지인 안심문자위해 지인번호 가져오기
    @GetMapping("/get/friend/phone/{m_id}")
    public ResponseEntity<List<String>> getFriendPhoneNo(@PathVariable("m_id") String m_id) {
        log.info("지인번호 가져오는 중 : " + m_id);

        return new ResponseEntity<>(appMatchService.getMyFriend(m_id), HttpStatus.OK);
    }

}
