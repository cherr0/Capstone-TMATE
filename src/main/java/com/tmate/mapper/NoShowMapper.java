package com.tmate.mapper;

import com.tmate.domain.AttendDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface NoShowMapper {

    // 1. 참여 상태 바꾸는 것  '3'
    int updateAttendStatus(@Param("m_id") String m_id, @Param("dp_id") String dp_id);

    // 2. 배차 정보 현재 인원 줄이는 것  -1
    int updateCurPeople(String dp_id);

    // 3. 노쇼한 사용자의 노쇼 카운트 +1 , 이용 횟수 -1, 멤버 상태 '2'
    int updateUserCnt(String m_id);

    // 5. 배차 정보 상태 '6'
    int updateNoshowDpStatus(String dp_id);

    // 6. 다시 멤버 상태 '0'
    int updateUserNormalStatus(String m_id);

    // 7. 멤버 상태 가져오는 것.
    String selectMemberStatus(String m_id);
}
