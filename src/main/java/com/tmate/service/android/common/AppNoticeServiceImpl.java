package com.tmate.service.android.common;

import com.tmate.domain.BoardDTO;
import com.tmate.mapper.BoardMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;


@Log4j2
@RequiredArgsConstructor
@Service
public class AppNoticeServiceImpl implements  AppNoticeService {

    private final BoardMapper boardMapper;

    @Override
    public List<BoardDTO> getNoticeList() {
        log.info("[NoticeList] app service send");
        return boardMapper.getNoticeList();
    }

    @Override
    public BoardDTO getNoticeDetail(String bd_id) {
        log.info("[NoticeDetail] app service send bd_id : " + bd_id);
        return boardMapper.read(bd_id);
    }
}
