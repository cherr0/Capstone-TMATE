package com.tmate.service;


import com.tmate.domain.*;
import com.tmate.domain.user.ApprovalDTO;
import com.tmate.mapper.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService {

    private final UserMainMapper userMainMapper;
    private final Membermapper membermapper;
    private final JoinMapper joinMapper;
    private final StaticsMapper staticsMapper;
    private final PaymentMapper paymentMapper;
    private final FriendMapper friendMapper;
    private final BoardImageMapper boardImageMapper;

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
    public List<DispatchDTO> getMyHistoryList(Criteria cri, String m_id) {
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

        String credit_no = paymentDTO.getCredit_no().substring(0,12);
        String nohashcredit = paymentDTO.getCredit_no().substring(12);
        log.info("암호화하는번호"+credit_no);

        String hashpw = BCrypt.hashpw(credit_no, BCrypt.gensalt());
        log.info("뒷자리빼고 암호화한값" + hashpw+nohashcredit);
        paymentDTO.setCredit_no(hashpw+nohashcredit);

        String credit_pw = BCrypt.hashpw(paymentDTO.getCredit_pw(), BCrypt.gensalt());
        paymentDTO.setCredit_pw(credit_pw);
        log.info("비밀번호"+credit_pw);
        String cvc = BCrypt.hashpw(paymentDTO.getCredit_cvc(), BCrypt.gensalt());
        log.info("암호화"+cvc);
        paymentDTO.setCredit_cvc(cvc);

        return paymentMapper.insert(paymentDTO) == 1;
    }

    // 카드 대표 활성화

    @Transactional
    @Override
    public boolean modifyRep(String customer_uid, String m_id) {

        String c_id = paymentMapper.findPayment(m_id);

        if (c_id == customer_uid) {
            return true;
        }else if(c_id != null && c_id != customer_uid) {
            paymentMapper.updateDRep(c_id);
            return paymentMapper.updateRep(customer_uid) ==1;
        }else {
            return paymentMapper.updateRep(customer_uid) == 1;
        }

    }

    // 프로필 수정


    @Override
    public boolean modifyMember(MemberDTO memberDTO) {
        return membermapper.updateMember(memberDTO) == 1;
    }

    // 프로필 - 알림 전송
    @Override
    public List<NotificationDTO> getNotifiList(String m_id) {
        return userMainMapper.getNotifiByM_id(m_id);
    }

    @Override
    public List<NotificationDTO> getMyNotifiList(String m_id) {
        return friendMapper.findListMyFriends(m_id);
    }

    // 검색 조회
    @Override
    public List<MemberDTO> getSearchList(String phone) {
        return friendMapper.findByPhone(phone);
    }

    // 승인 요청 클릭시
    @Override
    public void registerApproval(ApprovalDTO approvalDTO) {
        friendMapper.insertMyApproval(approvalDTO);
    }

    // 나에게 승인 요청한 회원들
    @Override
    public List<ApprovalDTO> getMyApproValList(String m_id) {
        return friendMapper.findListMyApproval(m_id);
    }

    // 승인 거절시
    @Override
    public void removeApproval(String id, String m_id) {
        friendMapper.deleteApproval(id,m_id);
    }

    // 승인 버튼 눌렀을 시 알림전송에 들어가게 된다.
    @Override
    public void registerNotifi(NotificationDTO notificationDTO) {
        friendMapper.insertNotifi(notificationDTO);
    }

    // 지인요청 활성화 비활성화 상태 업데이트
    @Override
    public void modifyN_whether(NotificationDTO notificationDTO) {
        friendMapper.updateFlag(notificationDTO);
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

    @Override
    public List<BoardImageDTO> getBoardImageList() {
        return boardImageMapper.findBoardImageList();
    }
}
