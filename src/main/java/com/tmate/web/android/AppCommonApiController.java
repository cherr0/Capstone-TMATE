package com.tmate.web.android;

import com.tmate.domain.BoardDTO;
import com.tmate.service.android.common.AppNoticeService;
import com.tmate.service.android.common.CommonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/common")
@RequiredArgsConstructor
@Log4j2
public class AppCommonApiController {

    // App 공통 서비스
    private final AppNoticeService appNoticeService;
  
    /* -----------------------
         App 공지사항 컨트롤러
       ----------------------- */

    // 공지 리스트 조회
    @GetMapping("/noticeList")
    public ResponseEntity<List<BoardDTO>> getNoticeList() {
        log.info("어플 공지사항 리스트 불러오는 중");

        return new ResponseEntity<>(appNoticeService.getNoticeList(), HttpStatus.OK);
    }

    // 공지 세부사항 조회
    @GetMapping("/notice/{bd_id}")
    public ResponseEntity<BoardDTO> getNoticeDetail(@PathVariable("bd_id") String bd_id) {
        log.info("어플 공지 세부사항 불러오는 중");

        return new ResponseEntity<>(appNoticeService.getNoticeDetail(bd_id), HttpStatus.OK);
    }
    
    /* -----------------------
       사용자 App 이벤트 컨트롤러
       ----------------------- */
    // 진행중인 이벤트
    @GetMapping("/eventplist")
    public ResponseEntity<List<BoardDTO>> getEventList() {
        log.info("앱 진행중인 이벤트 넘기기위한 컨트롤러 진입 완료");
        return new ResponseEntity<>(commonService.getProgressEventList(), HttpStatus.OK);
    }

    // 종료된 이벤트
    @GetMapping("/eventflist")
    public ResponseEntity<List<BoardDTO>> getFinishEventList() {
        log.info("앱 종료된 이벤트 넘기기위한 컨트롤러 진입 완료");
        return new ResponseEntity<>(commonService.getFinishedEventList(), HttpStatus.OK);
    }

    @GetMapping("/readevent/{bd_id}")
    public ResponseEntity<BoardDTO> readEvent(@PathVariable("bd_id") String bd_id) {
        log.info("App에서 상세보기를 위한 이벤트 코드" + bd_id);
        return new ResponseEntity<>(commonService.readEvent(bd_id),HttpStatus.OK);
    }
}
