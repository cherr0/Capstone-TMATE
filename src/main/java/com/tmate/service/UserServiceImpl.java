package com.tmate.service;

import com.tmate.domain.*;
import com.tmate.mapper.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMainMapper userMainMapper;
    private final Membermapper membermapper;
    private final JoinMapper joinMapper;
    private final StaticsMapper staticsMapper;
    private final PaymentMapper paymentMapper;

    @Override
    public MemberDTO getMainPage(String m_id) {

        MemberDTO member = membermapper.getMemberByM_id(m_id);

        return member;
    }

    @Override
    public int totalCountLike(String m_id) {
        return userMainMapper.getCountLike(m_id);
    }

    @Override
    public int totalCountDislike(String m_id) {
        return userMainMapper.getCountDisLike(m_id);
    }

    @Override
    public int totalCountBan(String m_id) {
        return userMainMapper.getCountBan(m_id);
    }

    // My 이력 - 이용 내역
    @Override
    public List<JoinHistoryVO> getMyHistoryList(Criteria cri, String m_id) {
        return joinMapper.getHistoryByMember(cri,m_id);
    }

    // My 이력 - 포인트 내역
    @Override
    public List<JoinPointVO> getMyPointList(Criteria cri, String m_id) {
        return joinMapper.getPointByMember(cri, m_id);
    }

    // My 이력 - 결제 이력
    @Override
    public List<JoinReceiptVO> getMyReceiptList(Criteria cri, String m_id) {
        return userMainMapper.getReceiptListPaging(cri, m_id);
    }

    // My 이력 - 결제 이력 개수

    @Override
    public int totalReceiptCount(String m_id) {
        return userMainMapper.getCountReceipt(m_id);
    }

    // 프로필 수정 - 개인 정보
    @Override
    public MemberDTO getMember(String m_id) {
        return membermapper.getMemberByM_id(m_id);
    }

    // 메인 페이지 - 일주일 이용 내역
    @Override
    public List<JoinHistoryVO> getWeeklyHistoryList(String m_id) {
        return userMainMapper.getMainWeeklyHistoryList(m_id);
    }

    // 메인페이지 - 일주일 포인트 내역
    @Override
    public List<JoinPointVO> getWeeklyPointList(String m_id) {
        return userMainMapper.getMainWeeklyPointList(m_id);
    }

    // 프로필 - 카드 관리
    @Override
    public List<PaymentDTO> getPaymentList(String m_id) {
        return paymentMapper.getPaymentList(m_id);
    }

    // 프로필 - 카드 상세 조회
    @Override
    public PaymentDTO readPayment(String customer_uid) {
        return paymentMapper.getPaymentByCustomer(customer_uid);
    }

    @Override
    public boolean removePayment(String customer_uid) {
        return paymentMapper.delete(customer_uid) == 1;
    }

    @Override
    public boolean registerPayment(PaymentDTO paymentDTO) {
        return paymentMapper.insert(paymentDTO) == 1;
    }

    // 프로필 - 알림 전송
    @Override
    public List<NotificationDTO> getNotifiList(String m_id) {
        return userMainMapper.getNotifiByM_id(m_id);
    }


    @Override
    public List<ChartDTO> monthlyUsePoint(String m_id) {
        return staticsMapper.getCountUsePoint(m_id);
    }

    @Override
    public List<ChartDTO> monthlyGetPoint(String m_id) {
        return staticsMapper.getCountGetPoint(m_id);
    }

    @Override
    public List<ChartDTO> monthlyTogether(String m_id) {
        return staticsMapper.getCountTogether(m_id);
    }

    @Override
    public List<ChartDTO> monthlyNormal(String m_id) {
        return staticsMapper.getCountNormal(m_id);
    }

    @Override
    public List<ChartDTO> monthlyUseCard(String m_id) {
        return staticsMapper.getCountUseCard(m_id);
    }

    @Override
    public List<ChartDTO> monthlyUseCash(String m_id) {
        return staticsMapper.getCountUseCash(m_id);
    }
}
