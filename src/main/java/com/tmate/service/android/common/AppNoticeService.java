package com.tmate.service.android.common;


import com.tmate.domain.BoardDTO;

import java.util.List;

public interface AppNoticeService {

    // 공지사항 리스트 조회
    public List<BoardDTO> getNoticeList();

    // 공지사항 세부 조회
    public BoardDTO getNoticeDetail(String bd_id);


}
