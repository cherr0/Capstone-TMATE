package com.tmate.mapper;


import com.tmate.domain.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface JoinMapper {

    // 관리자페이지 이용내역 정보 가져오는 메서드
    List<JoinHistoryVO> getHistoryByMember(@Param("cri") Criteria cri, @Param("m_id") String m_id);

    // 포인트 내역 조인 관계로 가져오기
    List<JoinPointVO> getPointByMember(@Param("cri") Criteria cri,@Param("m_id") String m_id);

    // 기사 관리
    JoinDriverVO getDriver(String d_id);

    // 승인관리
    List<JoinApprovalVO> getApprovalDrivers(Criteria cri);

    // 기사테이블 삭제
    int deleteDriver(String d_id);

    // 승인관리 기사 목록 총 갯수 페이지네이션 처리 위함
    int getTotalApproCount(Criteria cri);

    // 회원 상세페이지 이용 내역 토탈 카운트
    int getTotalHistoryCount(String m_id);

    // 회원 상세페이지 포인트 토탈 카운트
    int getTotalPointCount(String m_id);
}
