package com.tmate.service;

import com.tmate.domain.*;

import java.util.List;
import java.util.Map;

public interface UserService {

    // 메인페이지 보여줄때
    // 멤버 DTO,
    public Map<String, Object> getMainPage(String m_id);

    // 일주일 이력 - 사용자
    public List<JoinHistoryVO> getWeeklyHistoryList(String m_id);

    // 일주일 이력 - 포인트
    public List<JoinPointVO> getWeeklyPointList(String m_id);

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

    // 프로필 카드 관리
    public List<PaymentDTO> getPaymentList(String m_id);

    // 프로필 지인 알림전송 관리
    public List<NotificationDTO> getNotifiList(String m_id);

}