package com.tmate.service;

import com.tmate.domain.*;

import java.util.List;
import java.util.Map;

public interface UserService {

    // 메인페이지 보여줄때
    // 멤버 DTO,
    public MemberDTO getMainPage(String m_id);

    // 좋아요 횟수
    public int totalCountLike(String m_id);

    // 싫어요 횟수
    public int totalCountDislike(String m_id);

    // 블랙 횟수
    public int totalCountBan(String m_id);

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

    // My 이력 - 결제 이력 개수
    public int totalReceiptCount(String m_id);

    // 프로필 개인 정보
    public MemberDTO getMember(String m_id);

    // 프로필 개인 정보 수정

    // 프로필 개인 정보 탈퇴

    // 프로필 카드 관리
    public List<PaymentDTO> getPaymentList(String m_id);

    // 프로필 지인 알림전송 관리
    public List<NotificationDTO> getNotifiList(String m_id);

    // 차트 관련
    // 월간 포인트 - 사용
    public List<ChartDTO> monthlyUsePoint(String m_id);

    // 월간 포인트 - 적립
    public List<ChartDTO> monthlyGetPoint(String m_id);

    // 월간 이용 내역 - 동승
    public List<ChartDTO> monthlyTogether(String m_id);

    // 월간 이용 내역 - 일반
    public List<ChartDTO> monthlyNormal(String m_id);

    // 월간 결제 이력 - 카드
    public List<ChartDTO> monthlyUseCard(String m_id);

    // 월간 결제 이력 - 현금
    public List<ChartDTO> monthlyUseCash(String m_id);
}
