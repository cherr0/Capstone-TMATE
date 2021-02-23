package com.tmate.mapper;


import com.tmate.domain.JoinApprovalVO;
import com.tmate.domain.JoinDriverVO;
import com.tmate.domain.JoinHistoryVO;
import com.tmate.domain.JoinPointVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface JoinMapper {

    // 관리자페이지 이용내역 정보 가져오는 메서드
    List<JoinHistoryVO> getHistoryByMember(String m_id);

    // 포인트 내역 조인 관계로 가져오기
    List<JoinPointVO> getPointByMember(String m_id);

    // 기사 관리
    JoinDriverVO getDriver(String d_id);

    // 승인관리
    List<JoinApprovalVO> getApprovalDrivers();

    // 기사테이블 삭제
    int deleteDriver(String d_id);


}
