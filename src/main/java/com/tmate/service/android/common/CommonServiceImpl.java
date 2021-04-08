package com.tmate.service.android.common;

import com.tmate.domain.BoardDTO;
import com.tmate.domain.BoardImageDTO;
import com.tmate.mapper.BoardImageMapper;
import com.tmate.mapper.BoardMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Log4j2
@RequiredArgsConstructor
public class CommonServiceImpl implements CommonService {

    // BoardMapper 의존 주입
    private final BoardMapper boardMapper;

    // BoardImageMapper 의존 주입
    private final BoardImageMapper boardImageMapper;


    /*
    * 이벤트 서비스 로직 부분
    * 1. 이벤트 리스트 (진행중인 이벤트 & 종료된 이벤트)
    * 2. 이벤트 상세 페이지
    * */

    // 진행중인 이벤트

    @Override
    public List<BoardDTO> getProgressEventList() {
        log.info("앱으로 진행중인 이벤트 서비스 처리중");
        return boardMapper.findProgressEvent();
    }


    // 종료된 이벤트

    @Override
    public List<BoardDTO> getFinishedEventList() {
        log.info("앱으로 종료된 이벤트 서비스 처리중");
        return boardMapper.findFinishedEvent();
    }

    @Override
    public BoardDTO readEvent(String bd_id) {
        BoardDTO event = boardMapper.findEventByBd_id(bd_id);
        List<BoardImageDTO> image = boardImageMapper.findByBd_id(bd_id);
        if (image != null) {
            event.setImgURL(image.get(0).getImageURL());
        }

        return event;
    }
}