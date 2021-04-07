package com.tmate.web.android;


import com.tmate.domain.BoardDTO;
import com.tmate.service.android.common.AppNoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/common")
@RequiredArgsConstructor
@Log4j2
public class AppCommonApiController {

    private final AppNoticeService appNoticeService;

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
}
