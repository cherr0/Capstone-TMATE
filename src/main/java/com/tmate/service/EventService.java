package com.tmate.service;

import com.tmate.domain.Criteria;
import com.tmate.domain.EventDTO;

import java.util.List;

public interface EventService {

    // 리스트 페이지
    public List<EventDTO> getListEvent(Criteria cri);

    // 삭제
    public boolean remove(String e_id);


    // 수정
    public boolean modify(EventDTO eventDTO);

    // 등록
    public boolean register(EventDTO eventDTO);

    // 상세페이지
    public EventDTO get(String e_id);

    // 페이지 네이션을 위한 이벤트 토탈 로직
    public int getTotalCount(Criteria cri);
}
