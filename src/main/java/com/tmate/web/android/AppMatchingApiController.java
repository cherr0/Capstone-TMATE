package com.tmate.web.android;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tmate.domain.HistoryDTO;
import com.tmate.domain.JoinHistoryVO;
import com.tmate.domain.TogetherDTO;
import com.tmate.domain.user.ApprovalDTO;
import com.tmate.service.android.user.AppMemberMatchService;
import com.tmate.service.android.user.AppMemberMatchServiceImpl;
import com.tmate.service.android.user.AppMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.annotations.UpdateProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@Log4j2
@RequestMapping("/match")
@RequiredArgsConstructor
public class AppMatchingApiController {


    // private final AppMemberService appMemberService;

    private final AppMemberMatchService appMemberMatchService;


    // 로그 찍기 용 TAG
    private final String TAG = "AppMatchingApiController";

    // 검색 내용
    @GetMapping("/getlist/{slttd}/{slngtd}/{flttd}/{flngtd}")
    public ResponseEntity<List<HistoryDTO>> getMatchingList(@PathVariable("slttd") String slttd, @PathVariable("slngtd") String slngtd,
                                                            @PathVariable("flttd") String flttd, @PathVariable("flngtd") String flngtd) {

        log.info("출발지 위도 : " + slttd);
        log.info("출발지 경도 : " + slngtd);
        log.info("도착지 위도 : " + flttd);
        log.info("도착지 경도 : " + flngtd);

        List<HistoryDTO> matchingList = appMemberMatchService.getTogetherMatchingList(slttd, slngtd, flttd, flngtd);
        log.info(TAG + "App으로 넘어가는 거리 기준 매칭 리스트 :" + matchingList);

        return new ResponseEntity<>(matchingList, HttpStatus.OK);
    }


    // 매칭 상세 내역 -> 수정 필요
    @GetMapping("/read/{merchant_uid}")
    public ResponseEntity<HistoryDTO> getMatchingDetail(@PathVariable String merchant_uid) {


        log.info(TAG + "APP에서 넘어오는 이용방 정보 :  " + merchant_uid);

        String real_id = merchant_uid.replace("\"","");

        log.info("바뀐 문자열 : " + real_id);

        HistoryDTO detail = appMemberMatchService.getTogetherMatchingDetail(merchant_uid);

        log.info("넘어가는 이용정보 : " + detail);

        return new ResponseEntity<>(detail, HttpStatus.OK);
    }


    // 삭제 하기 위한 컨트롤러
    @DeleteMapping("/remove/{merchant_uid}")
    public ResponseEntity<Boolean> removeMatchingByMaster(@PathVariable("merchant_uid") String merchant_uid) {

        log.info(TAG + ": 삭제하기 위해 넘어오는 이용 번호 : " + merchant_uid);

        return new ResponseEntity<>(appMemberMatchService.removeTogetherMatch(merchant_uid), HttpStatus.OK);
    }



    @PostMapping("/register/matching")
    public ResponseEntity<Boolean> registerMatchingRegister(@RequestBody HashMap<String,Object> hashMap) {

        ObjectMapper mapper = new ObjectMapper();
        HistoryDTO historyDTO = mapper.convertValue(hashMap.get("history"), new TypeReference<HistoryDTO>() {});
        TogetherDTO togetherDTO = mapper.convertValue(hashMap.get("together"), new TypeReference<TogetherDTO>() {});

        log.info(TAG + ": 동승 매칭 방을 생성하기 위해 넘어오는 HistoryDTO & TogetherDTO" + historyDTO + togetherDTO);

        return new ResponseEntity<>(appMemberMatchService.registerTogether(historyDTO, togetherDTO),HttpStatus.OK);
    }

    @GetMapping("/select/using/history/{m_id}")
    public ResponseEntity<JoinHistoryVO> getUsingHistory(@PathVariable("m_id") String m_id) {

        log.info(TAG + ": 이용중인 서비스 보여주기 " + m_id);

        JoinHistoryVO usingService = appMemberMatchService.getUsingService(m_id);

        return new ResponseEntity<>(usingService, HttpStatus.OK);
    }

    // 동승 매칭 신청시 이미 선택된 좌석을 보여주기 위함
    @GetMapping("/display/seatNum/{merchant_uid}")
    public ResponseEntity<List<TogetherDTO>> getCurrentSeatNums(@PathVariable("merchant_uid") String merchant_uid) {

        log.info(TAG + ": 이미 선택된 좌석 보여주기 : " + merchant_uid);

        List<TogetherDTO> seatNums = appMemberMatchService.getCurrnetSeatNums(merchant_uid);

        return new ResponseEntity<>(seatNums, HttpStatus.OK);
    }

    // 동승 신청 현황 보기
    @GetMapping("/display/apply/list/{merchant_uid}")
    public ResponseEntity<List<ApprovalDTO>> displayApplyList(
            @PathVariable("merchant_uid") String merchant_uid) {

        log.info(TAG + ": 동승 리스트 신청 현황 보기 : " + merchant_uid);

        List<ApprovalDTO> applyerList = appMemberMatchService.getApplyerList(merchant_uid);

        return new ResponseEntity<>(applyerList, HttpStatus.OK);
    }


    // 동승 신청 하기
    @PostMapping("/register/apply")
    public ResponseEntity<Boolean> registerApply(@RequestBody ApprovalDTO approvalDTO) {

        log.info(TAG + " : 동승 신청하기 : " + approvalDTO);

        return new ResponseEntity<>(appMemberMatchService.applyTogetherMatching(approvalDTO), HttpStatus.OK);
    }

    // 나의 신청 현황 보기
}
