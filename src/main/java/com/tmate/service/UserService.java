package com.tmate.service;

import com.tmate.domain.*;

import java.util.List;
import java.util.Map;

public interface UserService {

    // 메인페이지 보여줄때
    // 멤버 DTO,
    public Map<String, Object> getMainPage(String m_id);

    // My 이력 - 이용 내역
    public List<JoinHistoryVO> getMyHistoryList(Criteria cri, String m_id);
    // My 이력 - 포인트 내역
    public List<JoinPointVO> getMyPointList(Criteria cri, String m_id);

    // My 이력 -  결제 이력
    public List<JoinReceiptVO> getMyReceiptList(Criteria cri, String m_id);

    // 프로필 개인 정보
    public MemberDTO getMember(String m_id);

    // 프로필 개인 정보 수정

    // 프로필 개인 정보 탈퇴



}
