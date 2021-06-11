package com.tmate.service;

import com.fasterxml.jackson.databind.node.BooleanNode;
import com.tmate.domain.*;
import com.tmate.domain.user.ApprovalDTO;

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
    public List<DispatchDTO> getWeeklyHistoryList(String m_id);

    // 일주일 이력 - 포인트
    public List<JoinPointVO> getWeeklyPointList(String m_id);

    // My 이력 - 이용 내역
    public List<DispatchDTO> getMyHistoryList(Criteria cri, String m_id);
    // My 이력 - 포인트 내역
    public List<JoinPointVO> getMyPointList(Criteria cri, String m_id);

    // My 이력 -  결제 이력
    public List<JoinReceiptVO> getMyReceiptList(Criteria cri, String m_id);

    // My 이력 - 결제 이력 개수
    public int totalReceiptCount(String m_id);

    // 프로필 개인 정보
    public MemberDTO getMember(String m_id);

    // 프로필 개인 정보 수정
    public boolean modifyMember(MemberDTO memberDTO);

    // 프로필 개인 정보 탈퇴

    // 프로필 카드 관리 관련
    // 1. 카드 리스트 조회
    public List<PaymentDTO> getPaymentList(String m_id);

    // 2. 카드 상세 조회
    public PaymentDTO readPayment(String customer_uid);

    // 3. 카드 삭제
    public boolean removePayment(String customer_uid);

    // 4. 카드 추가
    public boolean registerPayment(PaymentDTO paymentDTO);

    // 5. 카드 대표 설정
    public boolean modifyRep(String customer_uid, String m_id);

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


    // 지인 알림 관련
    // 1. 나의 지인 리스트
    public List<NotificationDTO> getMyNotifiList(String m_id);

    // 2. 회원 검색 리스트 - by 폰번호
    public List<MemberDTO> getSearchList(String phone);

    // 3. 승인요청 인서트
    public void registerApproval(ApprovalDTO approvalDTO);

    // 4. 나에게 승인 요청한 회원들
    public List<ApprovalDTO> getMyApproValList(String m_id);

    // 5. 거절 버튼 누를때 서비스
    public void removeApproval(String id, String m_id);

    //6 . 승인 시 알림전송 인서트
    Boolean registerNotifi(NotificationDTO notificationDTO);

    // 7. 활성화 비활성화 상태 업데이트
    void modifyN_whether(NotificationDTO notificationDTO);

    // 8. 지인 번호 삭제
    Boolean removeFriendPhoneNo(String m_id, String n_name);

    // 이벤트 리스트
    List<BoardImageDTO> getBoardImageList();
}
