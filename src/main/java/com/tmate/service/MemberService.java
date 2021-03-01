package com.tmate.service;

import com.tmate.domain.*;

import java.util.List;

public interface MemberService {

    // 회원 리스트
    public List<MemberDTO> getListMembers(Criteria cri);

    // 회원 눌렀을때 나오는 상세페이지 - 회원정보
    public MemberDTO getMember(String m_id);

    // 회원 눌렀을때 나오는 상세페이지 - 이용내역
    public List<JoinHistoryVO> getHistoryList(Criteria cri, String m_id);

    // 페이지 처리용 이용내역
    public HistoryPageDTO getListPage(Criteria cri, String m_id);

    // 페이지 처리용 포인트 내역
    public PointPageDTO getPointListPage(Criteria cri, String m_id);

    // 회원 눌렀을때 나오는 상세페이지 - 포인트 내역
    public List<JoinPointVO> getPointList(Criteria cri, String m_id);


    // 회원 리스트 부분 토탈 카운트
    public int getTotalCount(Criteria cri);

    // 이용내역 토탈 카운트
    public int getTotalHistoryCount(String m_id);

    // 포인트 내역 토탈 카운트
    public int getTotalPointCount(String m_id);


}
