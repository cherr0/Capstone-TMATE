package com.tmate.service;

import com.tmate.domain.*;

import java.util.List;

public interface MemberService {

    // 회원 리스트
    List<MemberDTO> getListMembers(Criteria cri);

    // 회원 로그인시 계정 연동한 계정 찾을 때
//    public MemberDTO findSocialMember(String s_email);

    // 회원 눌렀을때 나오는 상세페이지 - 회원정보
    MemberDTO getMember(String m_id);

    // 페이지 처리용 이용내역
    HistoryPageDTO getListPage(Criteria cri, String m_id);

    // 페이지 처리용 포인트 내역
    PointPageDTO getPointListPage(Criteria cri, String m_id);

    // 회원 눌렀을때 나오는 상세페이지 - 포인트 내역
    List<JoinPointVO> getPointList(Criteria cri, String m_id);

    // 회원 리스트 부분 토탈 카운트
    int getTotalCount(Criteria cri);

    // 이용내역 토탈 카운트
    int getTotalHistoryCount(String m_id);

    // 포인트 내역 토탈 카운트
    int getTotalPointCount(String m_id);


}
