package com.tmate.mapper;

import com.tmate.domain.HistoryDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface HistoryMapper {

    // 멤버에 따른 이용내역 보여줄때
    HistoryDTO getHistoryByMember(String m_id);

}
