package com.tmate.service;

import com.tmate.domain.*;
import com.tmate.mapper.JoinMapper;
import com.tmate.mapper.Membermapper;
import com.tmate.mapper.UserMainMapper;
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

    @Override
    public Map<String, Object> getMainPage(String m_id) {

        Map<String, Object> mainMap = new HashMap<>();

        mainMap.put("member", membermapper.getMemberByM_id(m_id));
        mainMap.put("weeklyHistory", userMainMapper.getMainWeeklyHistoryList(m_id));
        mainMap.put("weeklyPoint", userMainMapper.getMainWeeklyPointList(m_id));
        mainMap.put("countBan",userMainMapper.getCountBan(m_id));
        mainMap.put("countGood", userMainMapper.getCountLike(m_id));
        mainMap.put("countBad", userMainMapper.getCountDisLike(m_id));

        return mainMap;
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
        return userMainMapper.getPaymentByM_id(m_id);
    }

    // 프로필 - 알림 전송
    @Override
    public List<NotificationDTO> getNotifiList(String m_id) {
        return userMainMapper.getNotifiByM_id(m_id);
    }
}