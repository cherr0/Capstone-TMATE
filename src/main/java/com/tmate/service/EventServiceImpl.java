package com.tmate.service;

import com.tmate.domain.Criteria;
import com.tmate.domain.EventDTO;
import com.tmate.mapper.EventMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {


    private final EventMapper eventMapper;

    // 이벤트 리스트
    @Override
    public List<EventDTO> getListEvent(Criteria cri) {
        return eventMapper.getListEvent(cri);
    }

    // 이벤트 제거
    @Override
    public boolean remove(String e_id) {
        return eventMapper.delete(e_id) == 1;
    }

    // 이벤트 수정
    @Override
    public boolean modify(EventDTO eventDTO) {
        return eventMapper.update(eventDTO) == 1;
    }

    // 이벤트 등록
    @Override
    public boolean register(EventDTO eventDTO) {
        return eventMapper.insert(eventDTO) == 1;
    }


    // 글 상세 페이지
    @Override
    public EventDTO get(String e_id) {
        return eventMapper.read(e_id);
    }

    // 이벤트 총 토탈 갯수
    @Override
    public int getTotalCount(Criteria cri) {
        return eventMapper.getTotalCount(cri);
    }
}
